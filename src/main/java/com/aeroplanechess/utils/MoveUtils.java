package com.aeroplanechess.utils;

import java.util.ArrayList;
import java.util.List;

import com.aeroplanechess.enums.CellPrefix;
import com.aeroplanechess.model.Aeroplane;

public class MoveUtils {

	public Aeroplane[] move(Aeroplane[] aeroplanes, int aeroplaneIndex, int rollResult) {
		Aeroplane aeroplane = aeroplanes[aeroplaneIndex];
		int color = aeroplane.getColor();
		String cellId = aeroplane.getInCellId();
		String destPrefix = cellId.substring(0, 2);
		int destNum = Integer.parseInt(cellId.substring(2));
		List<Integer> encountered = new ArrayList<Integer>();

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

		// TODO check eat, may change the return type?
		for (int i = 0; i < color * 4; i++) {
			if (aeroplanes[i].getInCellId().equals(destPrefix + destNum)) {
				encountered.add(i);
			}
		}

		for (int i = color * 4 + 4; i < aeroplanes.length; i++) {
			if (aeroplanes[i].getInCellId().equals(destPrefix + destNum)) {
				encountered.add(i);
			}
		}

		// TODO review here
		if (!encountered.isEmpty()) {
			if (encountered.size() > 1) {
				destPrefix = CellPrefix.Base.getPrefix();
				destNum = color;
			} else {
				int i = encountered.get(0);
				Aeroplane a = aeroplanes[i];
				a.setInCellId(CellPrefix.Base.getPrefix() + a.getColor());
				aeroplanes[i] = a;
			}
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
		aeroplanes[aeroplaneIndex] = aeroplane;
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
