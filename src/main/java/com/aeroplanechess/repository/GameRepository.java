package com.aeroplanechess.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aeroplanechess.model.Game;

@Repository
public class GameRepository {

	// TODO move to redis ?
	@Autowired
	Map<String, Game> waitingGameMap;

	// TODO move to redis ?
	@Autowired
	Map<String, Game> playingGameMap;

	// TODO move to redis ?
	@Autowired
	Map<String, String> playerGameMap;

	public Game getPlayingGame(String gameId) {
		return playingGameMap.containsKey(gameId) ? playingGameMap.get(gameId) : null;
	}

	public Game getWaitingGame(String gameId) {
		return waitingGameMap.containsKey(gameId) ? waitingGameMap.get(gameId) : null;
	}

	public Map<String, Game> getWaitingGameMap() {
		return waitingGameMap;
	}

	public Map<String, Game> getPlayingGameMap() {
		return playingGameMap;
	}

	public Map<String, String> getPlayerGameMap() {
		return playerGameMap;
	}

	public Game removePlayingGame(String gameId) {
		return playingGameMap.remove(gameId);
	}

	public Game removeWaitingGame(String gameId) {
		return waitingGameMap.remove(gameId);
	}

	public Game addPlayingGame(String gameId, Game game) {
		return playingGameMap.put(gameId, game);
	}

	public Game addWaitingGame(String gameId, Game game) {
		return waitingGameMap.put(gameId, game);
	}

}
