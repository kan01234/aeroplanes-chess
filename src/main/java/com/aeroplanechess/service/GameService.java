package com.aeroplanechess.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aeroplanechess.model.Aeroplane;
import com.aeroplanechess.model.Game;
import com.aeroplanechess.model.Player;
import com.aeroplanechess.repository.GameRepository;
import com.aeroplanechess.utils.GameUtils;

@Service
public class GameService extends AbstractWebSocketService {

	Logger logger = LoggerFactory.getLogger(GameService.class);

	@Autowired
	GameRepository gameRepository;

	@Autowired
	GameUtils gameUtils;

	@Value(value = "${game.config.numof.aeroplane}")
	int numOfAeroplane;

	@Value(value = "${game.config.dice.max}")
	int diceMax;

	public void start(String gameId) {
		send("start", gameId, "start", true);
		nextTurn(gameRepository.addPlayingGame(gameId, gameRepository.removeWaitingGame(gameId)), false);
	}

	public void roll(String sessionId, String gameId) {
		logger.info("roll, sessionId: " + sessionId + ", gameId: " + gameId);
		Game game = gameRepository.getPlayingGame(gameId);
		if (game == null)
			return;
		int rollResult = gameUtils.roll();
		// send roll result to all players
		game.setLastRoll(rollResult);
		send("roll-result", game.getId(), new String[] { "roll", "current" }, new Object[] { rollResult, game.getCurrentPlayerIndex() });

		// send move notification to current player
		if (rollResult == 6 && game.getContinued() == 2) {
			thridSix(game);
		} else {
			sendTo("move", sessionId, gameId, "move", true);
		}
	}

	public void move(String sessionId, String gameId, int aeroplaneIndex) {
		logger.info("move, sessionId: " + sessionId + ", gameId: " + gameId + ", aeroplaneIndex: " + aeroplaneIndex);
		Game game = gameRepository.getPlayingGame(gameId);
		if (game == null)
			return;
		int rollResult = game.getLastRoll();
		int currentPlayer = game.getCurrentPlayerIndex();
		Aeroplane[] aeroplanes = game.getAeroplanes();
		// move
		List<Integer> encountered = gameUtils.move(aeroplanes, currentPlayer * numOfAeroplane + aeroplaneIndex, rollResult);
		send("move-result", gameId, new String[] { "aeroplanes", "encountered" }, new Object[] { aeroplanes, encountered });
		// check win
		if (gameUtils.isWin(aeroplanes, currentPlayer))
			playerWin(gameId, currentPlayer);
		else
			nextTurn(game, rollResult == diceMax);
	}

	void thridSix(Game game) {
		logger.info("thridSix, game: " + game);
		gameUtils.allBackToBase(game.getAeroplanes(), game.getCurrentPlayerIndex());
		send("move-result", game.getId(), new String[] { "aeroplanes", "thrid-six" }, new Object[] { game.getAeroplanes(), true });
		nextTurn(game, false);
	}

	// TODO review herer, continued not thread safe
	void nextTurn(Game game, boolean isContinue) {
		logger.info("nextTurn, game:" + game + ", isContinue: " + isContinue);
		if (isContinue)
			game.setContinued(game.getContinued() + 1);
		else {
			Player[] players = game.getPlayers();
			do
				game.getTurnCount().incrementAndGet();
			while (players[game.getCurrentPlayerIndex()] == null);
			game.setContinued(0);
		}
		sendTo("your-turn", game.getPlayers()[game.getCurrentPlayerIndex()].getSessionId(), game.getId(), "your-turn", true);
	}

	public void playerWin(String gameId, int playerIndex) {
		logger.info("playerWin, gameId: " + gameId + ", playerIndex: " + playerIndex);
		gameRepository.removePlayingGame(gameId);
		send("won", gameId, "player-won", playerIndex);
	}

	public void playerLeaved(Game game, int playerIndex) {
		logger.info("playerLeaved, game: " + game + ", playerIndex: " + playerIndex);
		gameUtils.allBackToBase(game.getAeroplanes(), playerIndex);
		send("move-result", game.getId(), new String[] { "aeroplanes", "leaved" }, new Object[] { game.getAeroplanes(), playerIndex });
		if (playerIndex == game.getCurrentPlayerIndex())
			nextTurn(game, false);
	}

}
