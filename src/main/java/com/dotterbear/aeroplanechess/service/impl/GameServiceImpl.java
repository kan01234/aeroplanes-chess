package com.dotterbear.aeroplanechess.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dotterbear.aeroplanechess.builder.AeroplaneChessBuilder;
import com.dotterbear.aeroplanechess.model.Aeroplane;
import com.dotterbear.aeroplanechess.model.AeroplaneChess;
import com.dotterbear.aeroplanechess.utils.AeroplaneChessUtils;
import com.dotterbear.websocket.gameroom.model.Player;
import com.dotterbear.websocket.gameroom.repository.GameRepository;
import com.dotterbear.websocket.gameroom.service.AbstractWebSocketService;
import com.dotterbear.websocket.gameroom.service.GameService;

@Service
public class GameServiceImpl extends AbstractWebSocketService implements GameService<AeroplaneChess> {

	private Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

	@Autowired
	private GameRepository<AeroplaneChess> gameRepository;

	@Autowired
	private AeroplaneChessUtils aeroplaneChessUtils;

	@Autowired
	private AeroplaneChessBuilder aeroplaneChessBuilder;

	@Value(value = "${websocket.aeroplanechess.config.numof.aeroplane}")
	private int numOfAeroplane;

	@Value(value = "${websocket.aeroplanechess.config.dice.max}")
	private int diceMax;

	public void start(String gameId) {
		send("start", gameId, "start", true);
		nextTurn(gameRepository.addPlayingGame(gameId, gameRepository.removeWaitingGame(gameId)), false);
	}

	public void roll(String sessionId, String gameId) {
		logger.info("roll, sessionId: ", sessionId, ", gameId: ", gameId);
		AeroplaneChess game = gameRepository.getPlayingGame(gameId);
		if (game == null)
			return;
		synchronized (game) {
			int rollResult = aeroplaneChessUtils.roll();
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
	}

	public void move(String sessionId, String gameId, int aeroplaneIndex) {
		logger.info("move, sessionId: ", sessionId, ", gameId: ", gameId, ", aeroplaneIndex: ", aeroplaneIndex);
		AeroplaneChess game = gameRepository.getPlayingGame(gameId);
		if (game == null)
			return;
		synchronized (game) {
			int rollResult = game.getLastRoll();
			int currentPlayer = game.getCurrentPlayerIndex();
			Aeroplane[] aeroplanes = game.getAeroplanes();
			// move
			List<Integer> encountered = aeroplaneChessUtils.move(aeroplanes, currentPlayer * numOfAeroplane + aeroplaneIndex, rollResult);
			sendMoveResult(game.getId(), aeroplanes, "encountered", encountered);
			// check win
			if (aeroplaneChessUtils.isWin(aeroplanes, currentPlayer))
				playerWin(gameId, currentPlayer);
			else
				nextTurn(game, rollResult == diceMax);
		}
	}

	private void thridSix(AeroplaneChess game) {
		logger.info("thridSix, game: ", game);
		aeroplaneChessUtils.allBackToBase(game.getAeroplanes(), game.getCurrentPlayerIndex());
		sendMoveResult(game.getId(), game.getAeroplanes(), "thrid-six", true);
		nextTurn(game, false);
	}

	private void nextTurn(AeroplaneChess game, boolean isContinue) {
		logger.info("nextTurn, game:", game, ", isContinue: ", isContinue);
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
		logger.info("playerWin, gameId: ", gameId, ", playerIndex: ", playerIndex);
		gameRepository.removePlayingGame(gameId);
		send("won", gameId, "player-won", playerIndex);
	}

	public void playerLeaved(AeroplaneChess game, int playerIndex) {
		logger.info("playerLeaved, game: ", game, ", playerIndex: ", playerIndex);
		aeroplaneChessUtils.allBackToBase(game.getAeroplanes(), playerIndex);
		sendMoveResult(game.getId(), game.getAeroplanes(), "leaved", playerIndex);
		if (playerIndex == game.getCurrentPlayerIndex())
			nextTurn(game, false);
	}

	public AeroplaneChess newGame() {
		return aeroplaneChessBuilder.build();
	}

	private void sendMoveResult(String gameId, Aeroplane[] aeroplanes, String key, Object value) {
		send("move-result", gameId, new String[] { "aeroplanes", key }, new Object[] { aeroplanes, value });
	}
}
