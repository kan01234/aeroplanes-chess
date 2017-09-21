package com.aeroplanechess.enums;

public enum Color {

	Yellow(0), Blue(1), Green(2), Red(3), None(4);

	private int colorNum;

	private Color(int colorNum) {
		this.colorNum = colorNum;
	}

	public int getColorNum() {
		return colorNum;
	}

	public void setColorNum(int colorNum) {
		this.colorNum = colorNum;
	}

}
