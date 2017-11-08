package com.dotterbear.aeroplanechess.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.dotterbear.aeroplanechess.enums.CellPrefix;
import com.dotterbear.aeroplanechess.model.Aeroplane;

public class MoveUtils {

	static final int MAX_NUMOF_CIRCLE = 52;

	@Value(value = "${websocket.aeroplanechess.config.numof.aeroplane}")
	private int numOfAeroplane;

	@Value(value = "${websocket.gameroom.config.numof.player}")
	private int numOfPlayer;

	public List<Integer> move(Aeroplane[] aeroplanes, int aeroplaneIndex, int rollResult) {
		Aeroplane aeroplane = aeroplanes[aeroplaneIndex];
		int color = aeroplane.getColor();
		String cellId = aeroplane.getInCellId();
		String destPrefix = cellId.substring(0, 2);
		int destNum = Integer.parseInt(cellId.substring(2));
		List<Integer> encountered = new ArrayList<>();

		if (destPrefix.equals(CellPrefix.BASE.getPrefix()))
			return fromBase(aeroplane, rollResult);

		if (destPrefix.equals(CellPrefix.TAKEOFF.getPrefix())) {
			destPrefix = CellPrefix.SKY.getPrefix();
			destNum = (color * 13 + 3);
			count--;
			return fromTakeOff();
		}

		// shortcut > jump
		if (destPrefix.equals(CellPrefix.SKY.getPrefix()) && destNum % numOfPlayer == color) {
			int shortcutFrom = getShortcutFrom(color);
			if (shortcutFrom == destNum) {
				destNum += 12;
				destNum %= MAX_NUMOF_CIRCLE;
			} else if (!isTurn(destPrefix, destNum, color)) {
				destNum += numOfPlayer;
				destNum %= MAX_NUMOF_CIRCLE;
			}
		}

		encountered = findEncountered(aeroplanes, destPrefix + destNum, color);

		if (!encountered.isEmpty()) {
			if (encountered.size() > 1) {
				backToBase(aeroplane);
				return encountered;
			} else
				backToBase(aeroplanes[encountered.get(0)]);
		}

		// to goal
		if (destPrefix.equals(CellPrefix.LANDING.getPrefix())) {
			if (destNum == (color + 1) * 5) {
				destPrefix = CellPrefix.GOAL.getPrefix();
				destNum = 0;
			} else if (destNum > (color + 1) * 5) {
				destNum = destNum - (destNum - (color + 1) * 5) * 2;
			}
		}

		aeroplane.setInCellId(destPrefix + destNum);
		return encountered;

	}

	private ArrayList<Integer> fromBase(Aeroplane aeroplane, int rollResult) {
		if (rollResult % 2 == 0)
			aeroplane.setInCellId(CellPrefix.TAKEOFF.getPrefix() + aeroplane.getColor());
		return new ArrayList<Integer>();
	}

	private ArrayList<Integer> fromTakeOff(Aeroplane[] aeroplanes, Aeroplane aeroplane, int rollResult) {
		if (destPrefix.equals(CellPrefix.TAKEOFF.getPrefix())) {
			destPrefix = CellPrefix.SKY.getPrefix();
			destNum = (color * 13 + 3);
			return fromTakeOff();
		}
		walk(aeroplane, rollResult);
	}

	private String walk(Aeroplane aeroplane, int rollResult) {
		String destPrefix = aeroplane.getInCellId().substring(0, 2);
		int destNum = Integer.parseInt(aeroplane.getInCellId().substring(2));
		int color = aeroplane.getColor();
		// walk
		for (int i = 0; i < rollResult; i++) {
			if (isTurn(destPrefix, destNum, color)) {
				destPrefix = CellPrefix.LANDING.getPrefix();
				destNum = color * 5;
			} else {
				destNum += 1;
				destNum %= MAX_NUMOF_CIRCLE;
			}
		}
		return destPrefix + destNum;
	}

	private List<Integer> findEncountered(Aeroplane[] aeroplanes, String cellId, int color) {
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < color * numOfAeroplane; i++) {
			if (aeroplanes[i].getInCellId().equals(cellId)) {
				result.add(i);
			}
		}

		for (int i = (color + 1) * numOfAeroplane; i < aeroplanes.length; i++) {
			if (aeroplanes[i].getInCellId().equals(cellId)) {
				result.add(i);
			}
		}
		return result;
	}

	private int getShortcutFrom(int color) {
		return ((color + 1) * 13 + 7) % 52;
	}

	private boolean isTurn(String destPrefix, int cellNum, int color) {
		return destPrefix.equals(CellPrefix.SKY.getPrefix()) && cellNum == color * 13;
	}

	private void backToBase(Aeroplane aeroplane) {
		aeroplane.setInCellId(CellPrefix.BASE.getPrefix() + aeroplane.getColor());
	}

	public void allBackToBase(Aeroplane[] aeroplanes, int playerIndex) {
		int start = playerIndex * numOfAeroplane;
		for (int i = start; i < start + numOfAeroplane; i++)
			backToBase(aeroplanes[i]);
	}

}
