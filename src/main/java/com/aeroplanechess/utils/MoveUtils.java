package com.aeroplanechess.utils;

import com.aeroplanechess.enums.CellPrefix;
import com.aeroplanechess.model.Aeroplane;

public class MoveUtils {

	public Aeroplane[] move(Aeroplane[] aeroplanes, int index, int rollResult) {
		Aeroplane aeroplane = aeroplanes[index];
		int color = aeroplane.getColor();
		String cellId = aeroplane.getInCellId();
		String destPrefix = cellId.substring(0, 2);
		int destNum = Integer.parseInt(cellId.substring(2));

		// teleport from base to takeoff
		if (destPrefix.equals(CellPrefix.Base.getPrefix())) {
			if (rollResult % 2 == 0) {
				destPrefix = CellPrefix.TakeOff.getPrefix();
				destNum = color;
			}
			rollResult = 0;
		} else if (destPrefix.equals(CellPrefix.TakeOff.getPrefix())) {
			destPrefix = CellPrefix.Sky.getPrefix();
			destNum = (color * 13 + 3);
			rollResult--;
		}

		// walk
		while (rollResult > 0) {
			if (destPrefix.equals(CellPrefix.Sky.getPrefix()) && isTurn(destNum, color)) {
				destPrefix = CellPrefix.Landing.getPrefix();
				destNum = color * 5;
			} else {
				destNum += 1;
				destNum %= 52;
			}
			rollResult--;
		}

		// jump
		if (destPrefix.equals(CellPrefix.Sky.getPrefix()) && destNum % 4 == color && !isTurn(destNum, color)) {
			destNum += 4;
			destNum %= 52;
		}

		// to goal
		if (destPrefix.equals(CellPrefix.Landing.getPrefix())) {
			if (destNum == (color + 1) * 5) {
				destPrefix = CellPrefix.Goal.getPrefix();
				destNum = 0;
			} else if (destNum > (color + 1) * 5) {
				destNum = destNum - (destNum - (color + 1) * 5) * 2;
			}
		}

		aeroplane.setInCellId(destPrefix + destNum);
		aeroplanes[index] = aeroplane;
		return aeroplanes;
	}

	boolean isTurn(int cellNum, int color) {
		return cellNum == color * 13;
	}

	public Aeroplane[] allBackToBase(Aeroplane[] aeroplanes, int playerIndex) {
		int start = playerIndex * 4;
		for (int i = start; i < start + 4; i++)
			aeroplanes[i].setInCellId(CellPrefix.Base.getPrefix() + playerIndex);
		return aeroplanes;
	}

}
