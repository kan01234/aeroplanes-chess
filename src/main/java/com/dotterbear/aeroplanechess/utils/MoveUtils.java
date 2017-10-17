package com.dotterbear.aeroplanechess.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.dotterbear.aeroplanechess.enums.CellPrefix;
import com.dotterbear.aeroplanechess.model.Aeroplane;

public class MoveUtils {

	final int MAX_NUM_OF_CIRCLE = 52;

	@Value(value = "${game.config.numof.aeroplane}")
	int numOfAeroplane;

	@Value(value = "${game.config.numof.player}")
	int numOfPlayer;

	public List<Integer> move(Aeroplane[] aeroplanes, int aeroplaneIndex, int rollResult) {
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
				destNum %= MAX_NUM_OF_CIRCLE;
			}
			rollResult--;
		}

		// shortcut > jump
		if (destPrefix.equals(CellPrefix.Sky.getPrefix()) && destNum % numOfPlayer == color) {
			int shortcutFrom = getShortcutFrom(destNum, color);
			if (shortcutFrom == destNum) {
				destNum += 12;
				destNum %= MAX_NUM_OF_CIRCLE;
			} else if (!isTurn(destNum, color)) {
				destNum += numOfPlayer;
				destNum %= MAX_NUM_OF_CIRCLE;
			}
		}

		for (int i = 0; i < color * numOfAeroplane; i++) {
			if (aeroplanes[i].getInCellId().equals(destPrefix + destNum)) {
				encountered.add(i);
			}
		}

		for (int i = (color + 1) * numOfAeroplane; i < aeroplanes.length; i++) {
			if (aeroplanes[i].getInCellId().equals(destPrefix + destNum)) {
				encountered.add(i);
			}
		}

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
		return encountered;
	}

	int getShortcutFrom(int destNum, int color) {
		return (++color * 13 + 7) % 52;
	}

	boolean isTurn(int cellNum, int color) {
		return cellNum == color * 13;
	}

	public void allBackToBase(Aeroplane[] aeroplanes, int playerIndex) {
		int start = playerIndex * numOfAeroplane;
		for (int i = start; i < start + numOfAeroplane; i++)
			aeroplanes[i].setInCellId(CellPrefix.Base.getPrefix() + playerIndex);
	}

}
