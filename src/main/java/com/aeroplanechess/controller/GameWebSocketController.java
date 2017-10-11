package com.aeroplanechess.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.aeroplanechess.service.GameService;
import com.aeroplanechess.service.PlayerService;

@Controller
public class GameWebSocketController {

	@Autowired
	GameService gameService;

	@Autowired
	PlayerService playerService;

	Logger logger = LoggerFactory.getLogger(GameWebSocketController.class);

	@MessageMapping("/join/{gameId}/{name}")
	public void join(@Header("simpSessionId") String sessionId, @DestinationVariable(value = "gameId") String gameId, @DestinationVariable(value = "name") String name) {
		if (gameId.equals("null")) {
			playerService.addPlayer(sessionId, name);
		} else {
			playerService.addPlayer(sessionId, gameId, name);
		}
	}

	@MessageMapping("/ready/{gameId}")
	public void ready(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId) {
		playerService.ready(sessionId, gameId);
	}

	@MessageMapping("/roll/{gameId}")
	public void roll(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId) {
		gameService.roll(sessionId, gameId);
	}

	@MessageMapping("/move/{gameId}/{aeroplaneIndex}")
	public void move(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @DestinationVariable("aeroplaneIndex") int aeroplaneIndex) {
		gameService.move(sessionId, gameId, aeroplaneIndex);
	}
}
