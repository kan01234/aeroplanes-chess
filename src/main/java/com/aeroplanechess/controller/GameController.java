package com.aeroplanechess.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.aeroplanechess.service.GameService;

@Controller
public class GameController {

	@Autowired
	GameService gameService;

	Logger logger = LoggerFactory.getLogger(GameController.class);

	// @RequestMapping("/new-game")
	// public @ResponseBody String newGame() {
	// gameService.newGame();
	// return "done";
	// }

	@RequestMapping(value = "/")
	public String index() {
		return "index";
	}

	@RequestMapping("/test")
	public @ResponseBody String test(HttpServletRequest request, SessionStatus status) {
		status.setComplete();
		return request.getSession().getId();
	}

	@MessageMapping("/join/{gameId}")
	public void join(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, SimpMessageHeaderAccessor headerAccessor) {
		logger.info(headerAccessor.getSessionId().toString());
		logger.info(headerAccessor.getSessionAttributes().get("sessionId").toString());
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