package com.aeroplanechess.model;

import java.util.Arrays;

public class Game {

	String id;
	Player[] players;
	int lastRoll = -1;
	int currentPlayer = -1;
	int continued = 0;
	boolean ready = false;
	Aeroplane[] aeroplanes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getLastRoll() {
		return lastRoll;
	}

	public void setLastRoll(int lastRoll) {
		this.lastRoll = lastRoll;
	}

	public boolean getReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
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
		return "Game [id=" + id + ", players=" + Arrays.toString(players) + ", lastRoll=" + lastRoll + ", currentPlayer=" + currentPlayer + ", continued=" + continued + ", ready=" + ready + ", aeroplanes=" + Arrays.toString(aeroplanes)
				+ "]";
	}

}
