package com.aeroplanechess.builder;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import com.aeroplanechess.model.Aeroplane;
import com.aeroplanechess.model.Game;
import com.aeroplanechess.model.Player;

public class GameBuilder {

	@Value(value = "${game.config.numof.player}")
	int numOfPlayer;

	@Value(value = "${game.config.numof.aeroplane}")
	int numOfAeroplane;

	Game game;

	public Game build() {
		game = new Game();
		game.setId(UUID.randomUUID().toString());
		initPlayers();
		initAeroPlane();
		return game;
	}

	private void initPlayers() {
		game.setPlayers(new Player[numOfPlayer]);
	}

	private void initAeroPlane() {
		Aeroplane[] aeroplanes = new Aeroplane[numOfPlayer * numOfAeroplane];
		Aeroplane aeroplane;
		int indexCount = -1;
		for (int i = 0; i < aeroplanes.length; i++) {
			if (i % numOfPlayer == 0)
				indexCount++;
			aeroplane = new Aeroplane();
			aeroplane.setColor(indexCount);
			aeroplane.setInCellId("ba" + indexCount);
			aeroplanes[i] = aeroplane;
		}

		game.setAeroplanes(aeroplanes);
	}

}
