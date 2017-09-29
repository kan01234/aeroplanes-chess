package com.aeroplanechess.model;

import java.util.Arrays;

public class Game {

	String id;
	Player[] players;
	int lastRoll = -1;
	int currentPlayer = -1;
	int continued = 0;
	int readyCount = 0;
	boolean full = false;
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

	public int getReadyCount() {
		return readyCount;
	}

	public void setReadyCount(int readyCount) {
		this.readyCount = readyCount;
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

	public boolean isFull() {
		return full;
	}

	public void setFull(boolean full) {
		this.full = full;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", players=" + Arrays.toString(players) + ", lastRoll=" + lastRoll + ", currentPlayer=" + currentPlayer + ", continued=" + continued + ", readyCount=" + readyCount + ", full=" + full + ", aeroplanes=" + Arrays.toString(aeroplanes) + "]";
	}

}
