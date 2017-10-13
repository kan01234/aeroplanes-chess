package com.aeroplanechess.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aeroplanechess.model.Aeroplane;

public class GameUtils {

	@Autowired
	DiceUtils diceUtils;

	@Autowired
	MoveUtils moveUtils;

	@Autowired
	WinUtils winUtils;

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
		return winUtils.isWin(aeroplanes, currentPlayer);
	}

}
