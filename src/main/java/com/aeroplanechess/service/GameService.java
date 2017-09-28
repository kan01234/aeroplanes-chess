package com.aeroplanechess.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aeroplanechess.enums.CellPrefix;
import com.aeroplanechess.model.Aeroplane;
import com.aeroplanechess.model.Game;
import com.aeroplanechess.model.Player;
import com.aeroplanechess.utils.DiceUtils;
import com.aeroplanechess.utils.GameBuilder;
import com.aeroplanechess.utils.MoveUtils;

@Service
public class GameService {

	// TODO move to redis ?
	@Autowired
	Map<String, Game> waitingGames;

	// TODO move to redis ?
	@Autowired
	Map<String, Game> playingGames;

	@Autowired
	Map<String, String> playerGameMap;

	Logger logger = LoggerFactory.getLogger(GameService.class);

	@Autowired
	MessagingService messagingService;

	@Autowired
	GameBuilder gameBuilder;

	@Autowired
	DiceUtils diceUtils;

	@Autowired
	MoveUtils moveUtils;

	public void roll(String sessionId, String gameId) {
		if (!isPlayingGame(gameId))
			return;

		Game game = playingGames.get(gameId);
		int rollResult = diceUtils.roll();

		// send roll result to all players
		game.setLastRoll(rollResult);
		messagingService.send("roll-result", game.getId(), new String[] { "roll", "current" }, new Object[] { rollResult, game.getCurrentPlayer() });

		// send move notification to current player
		if (rollResult == 6 && game.getContinued() == 2) {
			thridSix(game);
		} else {
			messagingService.sendTo("move", sessionId, gameId, "move", true);
		}
	}

	public void addPlayer(String sessionId) {
		addPlayer(sessionId, waitingGames.isEmpty() ? gameBuilder.build() : waitingGames.get(waitingGames.keySet().iterator().next()));
	}

	public void addPlayer(String sessionId, String gameId) {
		addPlayer(sessionId, waitingGames.containsKey(gameId) ? waitingGames.get(gameId) : null);
	}

	void addPlayer(String sessionId, Game game) {
		if (game == null)
			messagingService.sendTo("joined", sessionId, new String[] { "error", "message" }, new Object[] { true, "game id not found" });

		Player[] players = game.getPlayers();
		boolean added = false;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				Player player = new Player();
				player.setColor(i);
				player.setName("Player " + (i + 1));
				player.setSessionId(sessionId);
				players[i] = player;
				if (i == players.length - 1)
					game.setReady(true);
				added = true;
				break;
			}
		}
		if (added) {
			game.setPlayers(players);
			checkStart(game);
			messagingService.sendTo("joined", sessionId, new String[] { "error", "game-id" }, new Object[] { false, game.getId() });
			messagingService.send("player-list", game.getId(), "players", players);
		}
	}

	public void removePlayer(String sessionId) {
		if (!playerGameMap.containsKey(sessionId))
			return;

		String gameId = playerGameMap.get(sessionId);
		if (waitingGames.containsKey(gameId)) {
			removePlayer(sessionId, waitingGames.get(gameId), true);
		} else if (playingGames.containsKey(gameId)) {
			removePlayer(sessionId, playingGames.get(gameId), false);
		}
	}

	void removePlayer(String sessionId, Game game, boolean isWaiting) {
		Player[] players = game.getPlayers();
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null)
				continue;
			if (players[i].getSessionId().equals(sessionId)) {
				players[i] = null;
				break;
			}
		}
		game.setPlayers(players);
		if (isWaiting)
			waitingGames.put(game.getId(), game);
		else
			playingGames.put(game.getId(), game);
		messagingService.send("player-list", game.getId(), "players", players);
	}

	void checkStart(Game game) {
		if (!game.getReady()) {
			waitingGames.put(game.getId(), game);
			return;
		}
		String gameId = game.getId();
		waitingGames.remove(gameId);
		playingGames.put(gameId, game);
		messagingService.send("start", gameId, "start", true);
		nextTurn(game, false);
	}

	public void move(String sessionId, String gameId, int aeroplaneIndex) {
		if (!isPlayingGame(gameId))
			return;

		Game game = playingGames.get(gameId);
		int rollResult = game.getLastRoll();
		int currentPlayer = game.getCurrentPlayer();
		Aeroplane[] aeroplanes = game.getAeroplanes();
		// move
		aeroplanes = moveUtils.move(aeroplanes, currentPlayer * 4 + aeroplaneIndex, rollResult);
		game.setAeroplanes(aeroplanes);
		messagingService.send("move-result", gameId, "aeroplanes", aeroplanes);
		// check win
		if (isWin(aeroplanes, currentPlayer)) {
			playingGames.remove(gameId);
			messagingService.send("won", gameId, "player-won", currentPlayer);
		} else {
			nextTurn(game, rollResult == 6);
		}
	}

	void thridSix(Game game) {
		Aeroplane[] aeroplanes = moveUtils.allBackToBase(game.getAeroplanes(), game.getCurrentPlayer());
		game.setAeroplanes(aeroplanes);
		messagingService.send("move-result", game.getId(), new String[] { "aeroplanes", "thrid-six" }, new Object[] { aeroplanes, true });
		nextTurn(game, false);
	}

	void nextTurn(Game game, boolean isContinue) {
		if (!isContinue) {
			game.setCurrentPlayer((game.getCurrentPlayer() + 1) % 4);
			game.setContinued(0);
		} else {
			game.setContinued(game.getContinued() + 1);
		}

		playingGames.put(game.getId(), game);
		messagingService.sendTo("your-turn", game.getPlayers()[game.getCurrentPlayer()].getSessionId(), game.getId(), "your-turn", true);
	}

	boolean isWin(Aeroplane[] aeroplanes, int currentPlayer) {
		int count = 0;
		for (int i = currentPlayer * 4; i < i + 4; i++) {
			if (aeroplanes[i].getInCellId().substring(0, 2).equals(CellPrefix.Goal.getPrefix()))
				count++;
			else
				break;
		}
		return count == 4;
	}

	boolean isPlayingGame(String gameId) {
		if (!playingGames.containsKey(gameId)) {
			messagingService.send("error", gameId, "message", "game not foundin playing games list");
			return false;
		}
		return true;
	}

}
