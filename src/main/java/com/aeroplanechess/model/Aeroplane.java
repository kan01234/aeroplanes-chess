package com.aeroplanechess.model;

public class Aeroplane {

	int color;
	String inCellId;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getInCellId() {
		return inCellId;
	}

	public void setInCellId(String inCellId) {
		this.inCellId = inCellId;
	}

	@Override
	public String toString() {
		return "Aeroplane [color=" + color + ", inCellId=" + inCellId + "]";
	}

}
