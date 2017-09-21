package com.aeroplanechess.utils;

import com.aeroplanechess.model.Aeroplane;

public class MoveUtils {

	public Aeroplane[] move(Aeroplane[] aeroplanes, int index, int rollResult) {
		Aeroplane aeroplane = aeroplanes[index];
		int color = aeroplane.getColor();
		String cellId = aeroplane.getInCellId();
		String destPrefix = cellId.substring(0, 2);
		int destNum = Integer.parseInt(cellId.substring(2));

		// teleport from base to takeoff
		switch (destPrefix) {
		case "ba":
			if (rollResult % 2 == 0) {
				destPrefix = "to";
				destNum = color;
			}
			rollResult = 0;
			break;
		case "to":
			destPrefix = "sk";
			destNum = (color * 13 + 3);
			rollResult--;
			break;
		default:
			break;
		}

		// walk
		while (rollResult > 0) {
			if (destPrefix.equals("sk") && isTurn(destNum, color)) {
				destPrefix = "ld";
				destNum = color * 5;
			} else {
				destNum += 1;
				destNum %= 52;
			}
			rollResult--;
		}

		// jump
		if (destPrefix.equals("sk") && destNum % 4 == color && !isTurn(destNum, color)) {
			destNum += 4;
			destNum %= 52;
		}

		// to goal
		if (destPrefix.equals("ld")) {
			if (destNum == (color + 1) * 5) {
				destPrefix = "go";
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

}
