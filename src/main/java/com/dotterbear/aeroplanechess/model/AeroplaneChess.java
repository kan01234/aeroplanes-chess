package com.dotterbear.aeroplanechess.model;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import com.dotterbear.websocket.gameroom.model.AbstractGame;

public class AeroplaneChess extends AbstractGame {

	private int lastRoll = -1;
	private int continued = 0;
	private Aeroplane[] aeroplanes;
	private AtomicInteger turnCount = new AtomicInteger(-1);

	public int getLastRoll() {
		return lastRoll;
	}

	public void setLastRoll(int lastRoll) {
		this.lastRoll = lastRoll;
	}

	public Aeroplane[] getAeroplanes() {
		return aeroplanes;
	}

	public void setAeroplanes(Aeroplane[] aeroplanes) {
		this.aeroplanes = aeroplanes;
	}

	public int getContinued() {
		return continued;
	}

	public void setContinued(int continued) {
		this.continued = continued;
	}

	public AtomicInteger getTurnCount() {
		return turnCount;
	}

	public void setTurnCount(AtomicInteger turnCount) {
		this.turnCount = turnCount;
	}

	public int getCurrentPlayerIndex() {
		return turnCount.get() % players.length;
	}

	@Override
	public String toString() {
		return "AeroplaneChess [lastRoll=" + lastRoll + ", continued=" + continued + ", aeroplanes=" + Arrays.toString(aeroplanes) + "]";
	}

}
