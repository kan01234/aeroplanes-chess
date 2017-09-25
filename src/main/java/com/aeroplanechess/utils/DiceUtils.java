package com.aeroplanechess.utils;

import java.util.Random;

public class DiceUtils {

	Random random;
	int min, max;

	public DiceUtils(int min, int max) {
		this.min = min;
		this.max = max;
		random = new Random();
	}

	public int roll() {
		return random.nextInt(max + 1 - min) + min;
//		return 6;
	}

}
