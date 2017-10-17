package com.dotterbear.aeroplanechess.model;

import java.util.Arrays;

public class AeroplaneChess extends Game {

	int lastRoll = -1;
	int continued = 0;
	Aeroplane[] aeroplanes;

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

	@Override
	public String toString() {
		return "AeroplaneChess [lastRoll=" + lastRoll + ", continued=" + continued + ", aeroplanes=" + Arrays.toString(aeroplanes) + "]";
	}

}
