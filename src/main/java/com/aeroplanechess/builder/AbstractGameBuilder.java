package com.aeroplanechess.builder;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import com.aeroplanechess.model.Game;
import com.aeroplanechess.model.Player;

public abstract class AbstractGameBuilder<T extends Game> {

	@Value(value = "${game.config.numof.player}")
	int numOfPlayer;

	public T build(T game) {
		game.setId(UUID.randomUUID().toString());
		game.setPlayers(new Player[numOfPlayer]);
		// initAeroPlane();
		return game;
	}

}
