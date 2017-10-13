package com.aeroplanechess.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.aeroplanechess.enums.CellPrefix;
import com.aeroplanechess.model.Aeroplane;

public class GameUtils {

	@Autowired
	DiceUtils diceUtils;

	@Autowired
	MoveUtils moveUtils;

	@Value(value = "${game.config.numof.aeroplane}")
	int numOfAeroplane;

	public int roll() {
		return diceUtils.roll();
	}

	public void allBackToBase(Aeroplane[] aeroplanes, int playerIndex) {
		moveUtils.allBackToBase(aeroplanes, playerIndex);
	}

	public List<Integer> move(Aeroplane[] aeroplanes, int aeroplaneIndex, int rollResult) {
		return moveUtils.move(aeroplanes, aeroplaneIndex, rollResult);
	}

	public boolean isWin(Aeroplane[] aeroplanes, int currentPlayer) {
		int count = 0;
		for (int i = currentPlayer * numOfAeroplane; i < i + numOfAeroplane; i++) {
			if (aeroplanes[i].getInCellId().substring(0, 2).equals(CellPrefix.Goal.getPrefix()))
				count++;
			else
				break;
		}
		return count == numOfAeroplane;
	}

}
