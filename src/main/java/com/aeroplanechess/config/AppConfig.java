package com.aeroplanechess.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aeroplanechess.builder.GameBuilder;
import com.aeroplanechess.model.Game;
import com.aeroplanechess.utils.DiceUtils;
import com.aeroplanechess.utils.GameUtils;
import com.aeroplanechess.utils.MoveUtils;

@Configuration
public class AppConfig {

	@Value(value = "${game.config.dice.min}")
	int diceMin;

	@Value(value = "${game.config.dice.min}")
	int diceMax;

	@Bean(name = "playerGameMap")
	public Map<String, String> getPlayerGameMap() {
		return new ConcurrentHashMap<String, String>();
	}

	@Bean(name = "playingGames")
	public Map<String, Game> getPlayingGame() {
		return new ConcurrentHashMap<String, Game>();
	}

	@Bean(name = "waitingGames")
	public Map<String, Game> getWaitingGames() {
		return new ConcurrentHashMap<String, Game>();
	}

	@Bean
	public DiceUtils getDiceUtils() {
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
	public GameBuilder getGameBuilder() {
		return new GameBuilder();
	}

}
