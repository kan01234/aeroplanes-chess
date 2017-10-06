// package com.aeroplanechess.event.listener;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.ApplicationListener;
// import org.springframework.context.annotation.Lazy;
// import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.messaging.SessionSubscribeEvent;
//
// import com.aeroplanechess.service.GameService;
//
// @Component
// public class StompSubscribeEventListener implements
// ApplicationListener<SessionSubscribeEvent> {
//
// private static final Logger logger =
// LoggerFactory.getLogger(StompSubscribeEventListener.class);
//
// @Autowired
// @Lazy
// GameService gameService;
//
// @Override
// public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
// StompHeaderAccessor headerAccessor =
// StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
// logger.info(headerAccessor.toString());
// }
// }
