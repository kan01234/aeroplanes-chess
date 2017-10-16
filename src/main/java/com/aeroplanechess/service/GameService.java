package com.aeroplanechess.service;

import org.springframework.stereotype.Service;

import com.aeroplanechess.model.Game;

@Service
public interface GameService<T extends Game> {

	T newGame();

	void playerWin(String gameId, int lastPlayerIndex);

	void playerLeaved(T t, int i);

	void start(String gameId);

}
