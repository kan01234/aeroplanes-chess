package com.dotterbear.aeroplanechess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.dotterbear" })
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class);
	}

}
