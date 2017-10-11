package com.aeroplanechess.repository;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	@Autowired
	PlayerRepository playerRepository;

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

	public Game removePlayingGame(String gameId) {
		Game game = playingGameMap.remove(gameId);
		if (game != null)
			playerRepository.removePlayer(Stream.of(game.getPlayers()).map(p -> p.getSessionId()).collect(Collectors.toList()));
		return game;
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
