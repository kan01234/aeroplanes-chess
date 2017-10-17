package com.dotterbear.aeroplanechess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dotterbear.websocket.gameroom.config.WebSocketConfig;

@SpringBootApplication()
@EnableConfigurationProperties(WebSocketConfig.class)
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class);
	}

}
