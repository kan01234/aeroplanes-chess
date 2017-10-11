package com.aeroplanechess.model;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {

	String id;
	Player[] players;
	int lastRoll = -1;
	int continued = 0;
	AtomicInteger readyCount = new AtomicInteger(0);
	AtomicInteger joinCount = new AtomicInteger(0);
	AtomicInteger turnCount = new AtomicInteger(-1);
	Aeroplane[] aeroplanes;
	// TaskQueueRunner taskQueueRunner = new TaskQueueRunner();

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

	public AtomicInteger getTurnCount() {
		return turnCount;
	}

	public void setTurnCount(AtomicInteger turnCount) {
		this.turnCount = turnCount;
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

	// public TaskQueueRunner getTaskQueueRunner() {
	// return taskQueueRunner;
	// }
	//
	// public void setTaskQueueRunner(TaskQueueRunner taskQueueRunner) {
	// this.taskQueueRunner = taskQueueRunner;
	// }
	//
	// public void addTask(Runnable runnable) {
	// taskQueueRunner.add(runnable);
	// }

	public int getCurrentPlayerIndex() {
		return turnCount.get() % 4;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", players=" + Arrays.toString(players) + ", lastRoll=" + lastRoll + ", continued=" + continued + ", readyCount=" + readyCount + ", joinCount=" + joinCount + ", turnCount=" + turnCount + ", aeroplanes=" + Arrays.toString(aeroplanes) + "]";
	}

}
