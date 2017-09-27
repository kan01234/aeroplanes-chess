package com.aeroplanechess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.aeroplanechess.service.GameService;

@Controller
public class GameController {

	@Autowired
	GameService gameService;

	// @RequestMapping("/new-game")
	// public @ResponseBody String newGame() {
	// gameService.newGame();
	// return "done";
	// }

	@MessageMapping("/join/{gameId}")
	public void join(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId) {
		if (gameId.equals("null")) {
			gameService.addPlayer(sessionId);
		} else {
			gameService.addPlayer(sessionId, gameId);
		}
		// gameService.checkStart();
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