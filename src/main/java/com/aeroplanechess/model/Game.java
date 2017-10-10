package com.aeroplanechess.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {

	String id;
	Player[] players;
	int lastRoll = -1;
	int currentPlayer = -1;
	int continued = 0;
	Map<String, Boolean> readyMap = new HashMap<String, Boolean>();
	AtomicInteger readyCount = new AtomicInteger(0);
	AtomicInteger joinCount = new AtomicInteger(0);
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

	public Map<String, Boolean> getReadyMap() {
		return readyMap;
	}

	public void setReadyMap(Map<String, Boolean> readyMap) {
		this.readyMap = readyMap;
	}

	public AtomicInteger getReadyCount() {
		return readyCount;
	}

	public void setReadyCount(AtomicInteger readyCount) {
		this.readyCount = readyCount;
	}

	public AtomicInteger getJoinCount() {
		return joinCount;
	}

	public void setJoinCount(AtomicInteger joinCount) {
		this.joinCount = joinCount;
	}

	public TaskQueueRunner getTaskQueueRunner() {
		return taskQueueRunner;
	}

	public void setTaskQueueRunner(TaskQueueRunner taskQueueRunner) {
		this.taskQueueRunner = taskQueueRunner;
	}

	public void addTask(Runnable runnable) {
		taskQueueRunner.add(runnable);
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", players=" + Arrays.toString(players) + ", lastRoll=" + lastRoll + ", currentPlayer=" + currentPlayer + ", continued=" + continued + ", readyMap=" + readyMap + ", readyCount=" + readyCount + ", aeroplanes=" + Arrays.toString(aeroplanes) + ", taskQueueRunner=" + taskQueueRunner + "]";
	}

}
