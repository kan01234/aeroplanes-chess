package com.aeroplanechess.utils;

import org.springframework.beans.factory.annotation.Autowired;

import com.aeroplanechess.model.Aeroplane;
import com.aeroplanechess.model.Player;

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

	public Aeroplane[] allBackToBase(Aeroplane[] aeroplanes, int playerIndex) {
		return moveUtils.allBackToBase(aeroplanes, playerIndex);
	}

	public Aeroplane[] move(Aeroplane[] aeroplanes, int aeroplaneIndex, int rollResult) {
		return moveUtils.move(aeroplanes, aeroplaneIndex, rollResult);
	}

	public boolean isWin(Aeroplane[] aeroplanes, int currentPlayer) {
		return winUtils.isWin(aeroplanes, currentPlayer);
	}

	public int lastPlayerIndex(Player[] players) {
		return winUtils.lastPlayerIndex(players);
	}

}
