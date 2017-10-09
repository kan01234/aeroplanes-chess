package com.aeroplanechess.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aeroplanechess.builder.GameBuilder;
import com.aeroplanechess.model.Aeroplane;
import com.aeroplanechess.model.Game;
import com.aeroplanechess.model.Player;
import com.aeroplanechess.utils.GameUtils;

@Service
public class GameService {

	// TODO move to redis ?
	@Autowired
	Map<String, Game> waitingGames;

	// TODO move to redis ?
	@Autowired
	Map<String, Game> playingGames;

	// TODO move to redis ?
	@Autowired
	Map<String, String> playerGameMap;

	Logger logger = LoggerFactory.getLogger(GameService.class);

	@Autowired
	MessagingService messagingService;

	@Autowired
	GameBuilder gameBuilder;

	@Autowired
	GameUtils gameUtils;

	public void roll(String sessionId, String gameId) {
		logger.info("roll, sessionId: " + sessionId + ", gameId: " + gameId);
		if (!isPlayingGame(gameId))
			return;

		Game game = playingGames.get(gameId);
		int rollResult = gameUtils.roll();

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

	public synchronized String addPlayer(String sessionId, String name) {
		logger.info("addPlayer, sessionId: " + sessionId);
		Game game = null;
		if (waitingGames.isEmpty()) {
			game = gameBuilder.build();
			waitingGames.put(game.getId(), game);
		} else {
			for (Game g : waitingGames.values()) {
				if (!g.isFull()) {
					game = g;
					break;
				}
			}
		}
		return addPlayer(sessionId, name, game);
	}

	public synchronized String addPlayer(String sessionId, String gameId, String name) {
		logger.info("addPlayer, sessionId: " + sessionId + ", gameId: " + gameId + ", name: " + name);
		if (waitingGames.containsKey(gameId))
			return addPlayer(sessionId, name, waitingGames.get(gameId));
		else
			messagingService.sendTo("joined", sessionId, "error", true);
		return null;
	}

	synchronized String addPlayer(String sessionId, String name, Game game) {
		logger.info("addPlayer, sessionId: " + sessionId + ", game: " + game + ", name: " + name);
		if (game == null)
			return null;

		String gameId = null;
		int i = 0;
		Player[] players = game.getPlayers();
		for (; i < players.length; i++) {
			if (players[i] == null) {
				players[i] = new Player(name, i, sessionId);
				if (i == players.length - 1)
					game.setFull(true);
				break;
			}
		}

		gameId = game.getId();
		playerGameMap.put(sessionId, gameId);
		messagingService.sendTo("joined", sessionId, new String[] { "error", "game-id", "index" }, new Object[] { false, gameId, i });
		return gameId;
	}

	public void removePlayer(String sessionId) {
		logger.info("removePlayer, sessionId: " + sessionId);
		if (!playerGameMap.containsKey(sessionId))
			return;

		String gameId = playerGameMap.get(sessionId);
		playerGameMap.remove(sessionId);
		if (waitingGames.containsKey(gameId)) {
			removePlayer(sessionId, waitingGames.get(gameId), true);
		} else if (playingGames.containsKey(gameId)) {
			removePlayer(sessionId, playingGames.get(gameId), false);
		}
	}

	void removePlayer(String sessionId, Game game, boolean isWaiting) {
		logger.info("removePlayer, sessionId: " + sessionId + ", game: " + game + ", isWaiting: " + isWaiting);
		Player[] players = game.getPlayers();
		String gameId = game.getId();
		int i;
		for (i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].getSessionId().equals(sessionId)) {
				players[i] = null;
				Map<String, Boolean> readyMap = game.getReadyMap();
				readyMap.remove(sessionId);
				if (readyMap.size() == 1) {
					playingGames.remove(gameId);
					messagingService.send("won", gameId, "player-won", gameUtils.lastPlayerIndex(players));
				}
				break;
			}
		}

		if (isWaiting)
			game.setFull(false);
		else {
			gameUtils.allBackToBase(game.getAeroplanes(), i);
			messagingService.send("move-result", game.getId(), new String[] { "aeroplanes", "leaved" }, new Object[] { game.getAeroplanes(), i, i });
			if (i == game.getCurrentPlayer())
				nextTurn(game, false);
		}
		messagingService.send("player-list", game.getId(), "players", players);
	}

	void checkStart(Game game) {
		logger.info("checkStart, game: " + game);
		messagingService.send("start", game.getId(), "start", true);
		nextTurn(game, false);
	}

	public void ready(String sessionId, String gameId) {
		logger.info("ready, sessionId: " + sessionId + " , gam");
		if (!isWaitingGame(gameId)) {
			isWaitingGame(gameId);
			return;
		}
		Game game = waitingGames.get(gameId);
		messagingService.send("player-list", gameId, "players", game.getPlayers());
		Map<String, Boolean> readyMap = game.getReadyMap();
		readyMap.put(sessionId, true);
		if (readyMap.size() == 4) {
			waitingGames.remove(gameId);
			playingGames.put(gameId, game);
			messagingService.send("start", gameId, "start", true);
			nextTurn(game, false);
		}
	}

	public void move(String sessionId, String gameId, int aeroplaneIndex) {
		logger.info("move, sessionId: " + sessionId + ", gameId: " + gameId + ", aeroplaneIndex: " + aeroplaneIndex);
		if (!isPlayingGame(gameId))
			return;

		Game game = playingGames.get(gameId);
		int rollResult = game.getLastRoll();
		int currentPlayer = game.getCurrentPlayer();
		Aeroplane[] aeroplanes = game.getAeroplanes();
		// move
		aeroplanes = gameUtils.move(aeroplanes, currentPlayer * 4 + aeroplaneIndex, rollResult);
		messagingService.send("move-result", gameId, "aeroplanes", aeroplanes);
		// check win
		if (gameUtils.isWin(aeroplanes, currentPlayer)) {
			playingGames.remove(gameId);
			messagingService.send("won", gameId, "player-won", currentPlayer);
		} else {
			nextTurn(game, rollResult == 6);
		}
	}

	void thridSix(Game game) {
		logger.info("thridSix, game: " + game);
		gameUtils.allBackToBase(game.getAeroplanes(), game.getCurrentPlayer());
		messagingService.send("move-result", game.getId(), new String[] { "aeroplanes", "thrid-six" }, new Object[] { game.getAeroplanes(), true });
		nextTurn(game, false);
	}

	// maybe bug here?
	void nextTurn(Game game, boolean isContinue) {
		logger.info("nextTurn, game:" + game + ", isContinue: " + isContinue);
		if (isContinue)
			game.setContinued(game.getContinued() + 1);
		else {
			int currentPlayer = game.getCurrentPlayer();
			do
				currentPlayer = ++currentPlayer % 4;
			while (game.getPlayers()[currentPlayer] == null);
			game.setCurrentPlayer(currentPlayer);
			game.setContinued(0);
		}

		messagingService.sendTo("your-turn", game.getPlayers()[game.getCurrentPlayer()].getSessionId(), game.getId(), "your-turn", true);
	}

	boolean isPlayingGame(String gameId) {
		if (!playingGames.containsKey(gameId)) {
			messagingService.send("error", gameId, "message", "game not found in playing games list!!!");
			return false;
		}
		return true;
	}

	boolean isWaitingGame(String gameId) {
		if (!waitingGames.containsKey(gameId)) {
			messagingService.send("error", gameId, "message", "game not found in waiting games list!!!");
			return false;
		}
		return true;
	}

	public Map<String, Game> getWaitingGames() {
		return waitingGames;
	}

	public Map<String, Game> getPlayingGames() {
		return playingGames;
	}

}
