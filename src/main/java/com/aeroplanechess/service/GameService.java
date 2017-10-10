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
import com.aeroplanechess.repository.GameRepository;
import com.aeroplanechess.utils.GameUtils;

@Service
// TODO remove player id before remove game
public class GameService {

	Logger logger = LoggerFactory.getLogger(GameService.class);

	@Autowired
	GameRepository gameRepository;

	@Autowired
	MessagingService messagingService;

	@Autowired
	GameBuilder gameBuilder;

	@Autowired
	GameUtils gameUtils;

	public void roll(String sessionId, String gameId) {

		Game game = gameRepository.getPlayingGame(gameId);

		if (game == null)
			return;

		game.addTask(new Runnable() {

			@Override
			public void run() {
				logger.info("roll, sessionId: " + sessionId + ", gameId: " + gameId);

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

		});
	}

	public void move(String sessionId, String gameId, int aeroplaneIndex) {
		Game game = gameRepository.getPlayingGame(gameId);

		if (game == null)
			return;

		game.addTask(new Runnable() {

			@Override
			public void run() {
				logger.info("move, sessionId: " + sessionId + ", gameId: " + gameId + ", aeroplaneIndex: " + aeroplaneIndex);

				int rollResult = game.getLastRoll();
				int currentPlayer = game.getCurrentPlayer();
				Aeroplane[] aeroplanes = game.getAeroplanes();
				// move
				aeroplanes = gameUtils.move(aeroplanes, currentPlayer * 4 + aeroplaneIndex, rollResult);
				messagingService.send("move-result", gameId, "aeroplanes", aeroplanes);
				// check win
				if (gameUtils.isWin(aeroplanes, currentPlayer)) {
					gameRepository.removePlayingGame(gameId);
					messagingService.send("won", gameId, "player-won", currentPlayer);
				} else {
					nextTurn(game, rollResult == 6);
				}
			}

		});
	}

	void thridSix(Game game) {
		logger.info("thridSix, game: " + game);
		gameUtils.allBackToBase(game.getAeroplanes(), game.getCurrentPlayer());
		messagingService.send("move-result", game.getId(), new String[] { "aeroplanes", "thrid-six" }, new Object[] { game.getAeroplanes(), true });
		nextTurn(game, false);
	}

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

	public void removePlayer(String sessionId) {
		logger.info("removePlayer, sessionId: " + sessionId);
		Map<String, String> playerGameMap = gameRepository.getPlayerGameMap();

		if (!playerGameMap.containsKey(sessionId))
			return;

		String gameId = playerGameMap.get(sessionId);
		Game game = gameRepository.getWaitingGame(gameId);
		boolean isWaiting = true;
		if (game == null) {
			game = gameRepository.getPlayingGame(gameId);
			isWaiting = false;
		}
		removePlayer(sessionId, game, isWaiting);
	}

	void removePlayer(String sessionId, Game game, boolean isWaiting) {
		game.addTask(new Runnable() {

			@Override
			public void run() {
				logger.info("removePlayer, sessionId: " + sessionId + ", game: " + game + ", isWaiting: " + isWaiting);
				gameRepository.removePlayer(sessionId);
				Player[] players = game.getPlayers();
				String gameId = game.getId();
				int i;
				for (i = 0; i < players.length; i++) {
					if (players[i] != null && players[i].getSessionId().equals(sessionId)) {
						players[i] = null;
						break;
					}
				}

				if (isWaiting) {
					if (game.getReadyCount().get() > game.getJoinCount().decrementAndGet())
						game.getReadyCount().decrementAndGet();
				} else {
					if (game.getReadyCount().decrementAndGet() == 1) {
						gameRepository.removePlayingGame(gameId);
						messagingService.send("won", gameId, "player-won", gameUtils.lastPlayerIndex(players));
						return;
					}
					gameUtils.allBackToBase(game.getAeroplanes(), i);
					messagingService.send("move-result", game.getId(), new String[] { "aeroplanes", "leaved" }, new Object[] { game.getAeroplanes(), i, i });
					if (i == game.getCurrentPlayer())
						nextTurn(game, false);
				}
				messagingService.send("player-list", game.getId(), "players", players);
			}

		});
	}

	public String addPlayer(String sessionId, String name) {
		logger.info("addPlayer, sessionId: " + sessionId);
		Map<String, Game> waitingGameMap = gameRepository.getWaitingGameMap();
		Game game = null;
		if (waitingGameMap.isEmpty()) {
			game = gameBuilder.build();
			waitingGameMap.put(game.getId(), game);
		} else {
			for (Game g : waitingGameMap.values()) {
				if (g.getJoinCount().incrementAndGet() <= 4) {
					game = g;
					break;
				}
			}
		}
		return addPlayer(sessionId, name, game);
	}

	public String addPlayer(String sessionId, String gameId, String name) {
		logger.info("addPlayer, sessionId: " + sessionId + ", gameId: " + gameId + ", name: " + name);
		Game game = gameRepository.getWaitingGame(gameId);
		if (game != null && game.getJoinCount().incrementAndGet() <= 4)
			return addPlayer(sessionId, name, game);
		else
			messagingService.sendTo("joined", sessionId, "error", true);
		return null;
	}

	String addPlayer(String sessionId, String name, Game game) {
		logger.info("addPlayer, sessionId: " + sessionId + ", game: " + game + ", name: " + name);
		if (game == null)
			return null;

		String gameId = null;
		int i = 0;
		Player[] players = game.getPlayers();
		for (; i < players.length; i++) {
			if (players[i] == null) {
				players[i] = new Player(name, i, sessionId);
				break;
			}
		}

		gameId = game.getId();
		gameRepository.getPlayerGameMap().put(sessionId, gameId);
		messagingService.sendTo("joined", sessionId, new String[] { "error", "game-id", "index" }, new Object[] { false, gameId, i });
		return gameId;
	}

	void start(Game game) {
		logger.info("start, game: " + game);
		messagingService.send("start", game.getId(), "start", true);
		nextTurn(game, false);
	}

	public void ready(String sessionId, String gameId) {
		logger.info("ready, sessionId: " + sessionId + " , gam");
		Game game = gameRepository.getWaitingGame(gameId);

		if (game == null)
			return;

		messagingService.send("player-list", gameId, "players", game.getPlayers());
		if (game.getReadyCount().incrementAndGet() == 4) {
			gameRepository.addPlayingGame(gameId, gameRepository.removeWaitingGame(gameId));
			messagingService.send("start", gameId, "start", true);
			nextTurn(game, false);
		}
	}
}
