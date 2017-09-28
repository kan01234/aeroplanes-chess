package com.aeroplanechess.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aeroplanechess.model.Game;
import com.aeroplanechess.utils.DiceUtils;
import com.aeroplanechess.utils.GameBuilder;
import com.aeroplanechess.utils.MoveUtils;

@Configuration
public class AppConfig {

	@Bean(name = "playerGameMap")
	public Map<String, String> getPlayerGameMap() {
		return new HashMap<String, String>();
	}

	@Bean(name = "playingGames")
	public Map<String, Game> getPlayingGame() {
		return new HashMap<String, Game>();
	}

	@Bean(name = "waitingGames")
	public Map<String, Game> getWaitingGames() {
		return new HashMap<String, Game>();
	}

	@Bean
	public DiceUtils getDiceUtils() {
		return new DiceUtils(1, 6);
	}

	@Bean
	public MoveUtils getMoveUtils() {
		return new MoveUtils();
	}

	@Bean
	public GameBuilder getGameBuilder() {
		return new GameBuilder();
	}

}
