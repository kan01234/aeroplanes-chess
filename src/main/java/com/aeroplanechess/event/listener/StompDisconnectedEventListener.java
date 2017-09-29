package com.aeroplanechess.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.aeroplanechess.service.GameService;

@Component
public class StompDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {

	private static final Logger logger = LoggerFactory.getLogger(StompDisconnectedEventListener.class);

	@Autowired
	@Lazy
	GameService gameService;

	@Override
	public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
		gameService.removePlayer(headerAccessor.getSessionId());
	}
}
