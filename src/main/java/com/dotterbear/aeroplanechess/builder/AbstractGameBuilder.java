package com.dotterbear.aeroplanechess.builder;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import com.dotterbear.aeroplanechess.model.Game;
import com.dotterbear.aeroplanechess.model.Player;

public abstract class AbstractGameBuilder<T extends Game> {

	@Value(value = "${game.config.numof.player}")
	int numOfPlayer;

	public T build(T game) {
		game.setId(UUID.randomUUID().toString());
		game.setPlayers(new Player[numOfPlayer]);
		return game;
	}

}
