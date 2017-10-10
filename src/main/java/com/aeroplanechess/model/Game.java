package com.aeroplanechess.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Game {

	String id;
	Player[] players;
	int lastRoll = -1;
	int currentPlayer = -1;
	int continued = 0;
	Map<String, Boolean> readyMap = new HashMap<String, Boolean>();
	boolean full = false;
	Aeroplane[] aeroplanes;
	TaskQueueRunner taskQueueRunner = new TaskQueueRunner();

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

	public Map<String, Boolean> getReadyMap() {
		return readyMap;
	}

	public void setReadyMap(Map<String, Boolean> readyMap) {
		this.readyMap = readyMap;
	}

	public void addTask(Runnable runnable) {
		taskQueueRunner.add(runnable);
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", players=" + Arrays.toString(players) + ", lastRoll=" + lastRoll + ", currentPlayer=" + currentPlayer + ", continued=" + continued + ", readyMap=" + readyMap + ", full=" + full + ", aeroplanes=" + Arrays.toString(aeroplanes) + "]";
	}

}
