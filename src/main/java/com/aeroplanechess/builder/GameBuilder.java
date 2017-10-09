package com.aeroplanechess.builder;

import java.util.UUID;

import com.aeroplanechess.model.Aeroplane;
import com.aeroplanechess.model.Game;
import com.aeroplanechess.model.Player;

public class GameBuilder {

	Game game;

	public Game build() {
		game = new Game();
		game.setId(UUID.randomUUID().toString());
		initPlayers();
		initAeroPlane();
		return game;
	}

	private void initPlayers() {
		game.setPlayers(new Player[4]);
	}

	private void initAeroPlane() {
		Aeroplane[] aeroplanes = new Aeroplane[16];
		Aeroplane aeroplane;
		int indexCount = -1;
		for (int i = 0; i < aeroplanes.length; i++) {
			if (i % 4 == 0)
				indexCount++;
			aeroplane = new Aeroplane();
			aeroplane.setColor(indexCount);
			aeroplane.setInCellId("ba" + indexCount);
			aeroplanes[i] = aeroplane;
		}

		// for (int i = 0; i < 4; i++) {
		// aeroplane = aeroplanes[i];
		// aeroplane.setInCellId("ld0");
		// aeroplanes[i] = aeroplane;
		// }

		game.setAeroplanes(aeroplanes);
	}

}
