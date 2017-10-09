package com.aeroplanechess.utils;

import com.aeroplanechess.enums.CellPrefix;
import com.aeroplanechess.model.Aeroplane;
import com.aeroplanechess.model.Player;

public class WinUtils {

	boolean isWin(Aeroplane[] aeroplanes, int currentPlayer) {
		int count = 0;
		for (int i = currentPlayer * 4; i < i + 4; i++) {
			if (aeroplanes[i].getInCellId().substring(0, 2).equals(CellPrefix.Goal.getPrefix()))
				count++;
			else
				break;
		}
		return count == 4;
	}

	public int lastPlayerIndex(Player[] players) {
		int j = 0;
		for (; j < players.length; j++)
			if (players[j] != null)
				break;
		return j >= players.length ? -1 : j;
	}

}
