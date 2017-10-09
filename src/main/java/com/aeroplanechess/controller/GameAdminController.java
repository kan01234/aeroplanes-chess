package com.aeroplanechess.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aeroplanechess.model.Game;
import com.aeroplanechess.service.GameService;

@RestController
public class GameAdminController {

	@Autowired
	GameService gameService;

	@RequestMapping(value = "/waiting-game-list")
	public @ResponseBody Map<String, Game> waitinGameList() {
		return gameService.getWaitingGames();
	}

	@RequestMapping(value = "/playing-game-list")
	public @ResponseBody Map<String, Game> playingGameList() {
		return gameService.getPlayingGames();
	}

}
