package com.aeroplanechess.model;

public class Player {

	String name;
	int color;
	String sessionId;

	public Player() {
		super();
	}

	public Player(String name, int color, String sessionId) {
		super();
		this.name = name;
		this.color = color;
		this.sessionId = sessionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", color=" + color + ", sessionId=" + sessionId + "]";
	}

}
