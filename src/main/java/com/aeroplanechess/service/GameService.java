package com.aeroplanechess.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

	// TODO review here!
	public static Game game;

	Logger logger = LoggerFactory.getLogger(GameService.class);
	Map<String, Object> responseMap;

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	GameBuilder gameBuilder;

	@Autowired
	DiceUtils diceUtils;

	@Autowired
	MoveUtils moveUtils;

	public void roll(String sessionId) {
		int rollResult = diceUtils.roll();
		responseMap = new HashMap<String, Object>();

		// send roll result to all players
		game.setLastRoll(rollResult);
		responseMap.put("roll", game.getLastRoll());
		responseMap.put("current", game.getCurrentPlayer());
		simpMessagingTemplate.convertAndSend("/game/roll-result", responseMap);

		// send move notification to current player
		if (rollResult == 6 && game.getContinued() == 2) {
			thridSix();
		} else {
			responseMap = new HashMap<String, Object>();
			responseMap.put("move", true);
			simpMessagingTemplate.convertAndSend("/game/move-" + sessionId, responseMap);
		}
	}

	public void newGame() {
		game = gameBuilder.build();
		// TODO return game id?
	}

	public void addPlayer(String sessionId) {
		if (game == null)
			newGame();
		Player player;
		Player[] players = game.getPlayers();
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				player = new Player();
				player.setColor(i);
				player.setName("Player " + (i + 1));
				player.setSessionId(sessionId);
				players[i] = player;
				if (i == players.length - 1)
					game.setReady(true);
				break;
			}
		}
		game.setPlayers(players);
		simpMessagingTemplate.convertAndSend("/game/player-list", game.getPlayers());
	}

	public void removePlayer(String sessionId) {
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
		simpMessagingTemplate.convertAndSend("/game/player-list", game.getPlayers());
	}

	public void checkStart() {
		if (!game.getReady())
			return;
		responseMap = new HashMap<String, Object>();
		responseMap.put("start", true);
		simpMessagingTemplate.convertAndSend("/game/start", responseMap);
		nextTurn(false);
	}

	public void move(String sessionId, int aeroplaneIndex) {
		int rollResult = game.getLastRoll();
		Aeroplane[] aeroplanes = game.getAeroplanes();
		responseMap = new HashMap<String, Object>();

		// move
		aeroplanes = moveUtils.move(aeroplanes, game.getCurrentPlayer() * 4 + aeroplaneIndex, rollResult);
		game.setAeroplanes(aeroplanes);
		responseMap.put("aeroplanes", aeroplanes);
		simpMessagingTemplate.convertAndSend("/game/move-result", responseMap);

		// check win
		if (isWin()) {
			responseMap = new HashMap<String, Object>();
			responseMap.put("playerWon", game.getCurrentPlayer());
			simpMessagingTemplate.convertAndSend("/game/won", responseMap);
		} else {
			nextTurn(rollResult == 6);
		}
	}

	void thridSix() {
		responseMap = new HashMap<String, Object>();
		Aeroplane[] aeroplanes = moveUtils.allBackToBase(game.getAeroplanes(), game.getCurrentPlayer());
		game.setAeroplanes(aeroplanes);
		responseMap.put("aeroplanes", aeroplanes);
		responseMap.put("thrid-six", true);
		simpMessagingTemplate.convertAndSend("/game/move-result", responseMap);
		nextTurn(false);
	}

	void nextTurn(boolean isContinue) {
		if (!isContinue) {
			game.setCurrentPlayer((game.getCurrentPlayer() + 1) % 4);
			game.setContinued(0);
		} else {
			game.setContinued(game.getContinued() + 1);
		}

		responseMap = new HashMap<String, Object>();
		responseMap.put("your-turn", true);
		simpMessagingTemplate.convertAndSend("/game/your-turn-" + game.getPlayers()[game.getCurrentPlayer()].getSessionId(), responseMap);
	}

	boolean isWin() {
		Aeroplane[] aeroplanes = game.getAeroplanes();
		int count = 0;
		for (int i = game.getCurrentPlayer() * 4; i < i + 4; i++) {
			if (aeroplanes[i].getInCellId().substring(0, 2).equals(CellPrefix.Goal.getPrefix()))
				count++;
			else
				break;
		}
		return count == 4;
	}

}
