package com.aeroplanechess.utils;

import com.aeroplanechess.model.Aeroplane;

public class MoveUtils {

	public Aeroplane[] move(Aeroplane[] aeroplanes, int index, int rollResult) {
		Aeroplane aeroplane = aeroplanes[index];
		int color = aeroplane.getColor();
		String cellId = aeroplane.getInCellId();
		String destPrefix = cellId.substring(0, 2);
		int destNum = Integer.parseInt(cellId.substring(2));

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

		while (rollResult > 0) {
			if (destPrefix.equals("sk") && destNum == color * 13) {
				destPrefix = "ld";
				destNum = color * 5;
			} else {
				destNum += 1;
				destNum %= 52;
			}
			rollResult--;
		}

		// TODO add jump
		if (destNum % 4 == color) {
			destNum += 4;
			destNum %= 52;
		}

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

}
