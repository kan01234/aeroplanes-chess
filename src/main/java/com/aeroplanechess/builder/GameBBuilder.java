package com.aeroplanechess.builder;

import org.springframework.beans.factory.annotation.Value;

import com.aeroplanechess.model.Aeroplane;
import com.aeroplanechess.model.GameB;

public class GameBBuilder extends AbstractGameBuilder<GameB> {

	@Value(value = "${game.config.numof.aeroplane}")
	int numOfAeroplane;

	public GameB build() {
		GameB game = super.build(new GameB());
		game.setAeroplanes(getAeroplanes());
		return game;
	}

	private Aeroplane[] getAeroplanes() {
		Aeroplane[] aeroplanes = new Aeroplane[numOfPlayer * numOfAeroplane];
		int indexCount = -1;
		for (int i = 0; i < aeroplanes.length; i++) {
			if (i % numOfPlayer == 0)
				indexCount++;
			aeroplanes[i] = new Aeroplane(indexCount, "ba" + indexCount);
		}
		return aeroplanes;
	}

}
