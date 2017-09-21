package com.aeroplanechess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aeroplanechess.service.GameService;

@Controller
public class GameController {

	@Autowired
	GameService gameService;

	@RequestMapping("/new-game")
	public @ResponseBody String newGame() {
		return gameService.newGame();
	}

	@RequestMapping("/game")
	public String gameIndex(@Header("simpSessionId") String sessionId) {
		return "game";
	}

	@MessageMapping("/join")
	public void join(@Header("simpSessionId") String sessionId) {
		gameService.addPlayer(sessionId);
		gameService.checkStart();
	}

	@MessageMapping("/roll")
	public void roll(@Header("simpSessionId") String sessionId) {
		gameService.roll(sessionId);
	}

	@MessageMapping("/move/{aeroplaneIndex}")
	public void move(@Header("simpSessionId") String sessionId,
			@DestinationVariable("aeroplaneIndex") int aeroplaneIndex) {
		gameService.move(sessionId, aeroplaneIndex);
	}

}