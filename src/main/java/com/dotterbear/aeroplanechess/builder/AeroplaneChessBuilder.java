package com.dotterbear.aeroplanechess.builder;

import org.springframework.beans.factory.annotation.Value;

import com.dotterbear.aeroplanechess.model.Aeroplane;
import com.dotterbear.aeroplanechess.model.AeroplaneChess;

public class AeroplaneChessBuilder extends AbstractGameBuilder<AeroplaneChess> {

	@Value(value = "${game.config.numof.aeroplane}")
	int numOfAeroplane;

	public AeroplaneChess build() {
		AeroplaneChess aeroplaneChess = super.build(new AeroplaneChess());
		aeroplaneChess.setAeroplanes(getAeroplanes());
		return aeroplaneChess;
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
