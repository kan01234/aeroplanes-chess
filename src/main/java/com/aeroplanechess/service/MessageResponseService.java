package com.aeroplanechess.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class MessageResponseService {

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	public void send(String path, String gameId, String key, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(key, value);
		send(path, gameId, map);
	}

	public void send(String path, String gameId, String[] keys, Object[] values) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < keys.length; i++) {
			map.put(keys[i], values[i]);
		}
		send(path, gameId, map);
	}

	public void sendTo(String path, String sessionId, String gameId, String key, Object value) {
		send(path + "-" + sessionId, gameId, key, value);
	}

	void send(String path, String gameId, Map<String, Object> map) {
		simpMessagingTemplate.convertAndSend("/game/" + gameId + "/" + path, map);
	}

}
