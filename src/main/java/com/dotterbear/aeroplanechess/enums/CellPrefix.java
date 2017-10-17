package com.dotterbear.aeroplanechess.enums;

public enum CellPrefix {

	Base("ba"), TakeOff("to"), Sky("sk"), Landing("ld"), Goal("go");

	private String prefix;

	private CellPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
