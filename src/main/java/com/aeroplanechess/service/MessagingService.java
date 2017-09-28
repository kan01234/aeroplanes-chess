package com.aeroplanechess.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	public void send(String path, String gameId, String key, Object value) {
		send(path, gameId, new String[] { key }, new Object[] { value });
	}

	public void send(String path, String gameId, String[] keys, Object[] values) {
		send(gameId + "/" + path, keys, values);
	}

	public void sendTo(String path, String sessionId, String gameId, String key, Object value) {
		sendTo(gameId + "/" + path, sessionId, new String[] { key }, new Object[] { value });
	}

	public void sendTo(String path, String sessionId, String[] keys, Object[] values) {
		send(path + "-" + sessionId, keys, values);
	}

	void send(String path, String[] keys, Object[] values) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < keys.length; i++) {
			map.put(keys[i], values[i]);
		}
		simpMessagingTemplate.convertAndSend("/game/" + path, map);
	}

}
