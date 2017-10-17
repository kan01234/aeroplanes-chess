package com.dotterbear.aeroplanechess.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dotterbear.aeroplanechess.builder.AeroplaneChessBuilder;
import com.dotterbear.aeroplanechess.model.Game;
import com.dotterbear.aeroplanechess.service.GameService;
import com.dotterbear.aeroplanechess.service.impl.GameServiceImpl;
import com.dotterbear.aeroplanechess.utils.DiceUtils;
import com.dotterbear.aeroplanechess.utils.GameUtils;
import com.dotterbear.aeroplanechess.utils.MoveUtils;
import com.dotterbear.aeroplanechess.utils.PlayerUtils;

@Configuration
public class AppConfig {

	@Bean(name = "playerGameMap")
	public Map<String, String> getPlayerGameMap() {
		return new ConcurrentHashMap<String, String>();
	}

	@Bean(name = "playingGameMap")
	public Map<String, Game> getPlayingGameMap() {
		return new ConcurrentHashMap<String, Game>();
	}

	@Bean(name = "waitingGameMap")
	public Map<String, Game> getWaitingGameMap() {
		return new ConcurrentHashMap<String, Game>();
	}

	@Bean
	public DiceUtils getDiceUtils(@Value(value = "${websocket.aeroplanechess.config.dice.min}") int diceMin, @Value(value = "${websocket.aeroplanechess.config.dice.max}") int diceMax) {
		return new DiceUtils(diceMin, diceMax);
	}

	@Bean
	public MoveUtils getMoveUtils() {
		return new MoveUtils();
	}

	@Bean
	public GameUtils getGameUtils() {
		return new GameUtils();
	}

	@Bean
	public PlayerUtils getPlayerUtils() {
		return new PlayerUtils();
	}

	@Bean
	public GameService<?> getGameService() {
		return new GameServiceImpl();
	}

	@Bean
	public AeroplaneChessBuilder getGameBuilder() {
		return new AeroplaneChessBuilder();
	}

}
