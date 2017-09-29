// package com.aeroplanechess.utils;
//
// import java.util.Map;
//
// import org.springframework.http.server.ServerHttpRequest;
// import org.springframework.http.server.ServerHttpResponse;
// import org.springframework.http.server.ServletServerHttpRequest;
// import org.springframework.web.socket.WebSocketHandler;
// import org.springframework.web.socket.server.HandshakeInterceptor;
//
// public class HttpHandshakeInterceptor implements HandshakeInterceptor {
//
// @Override
// public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse
// response, WebSocketHandler wsHandler, Map attributes) throws Exception {
// if (request instanceof ServletServerHttpRequest) {
// attributes.put("sessionId", ((ServletServerHttpRequest)
// request).getServletRequest().getSession().getId());
// }
// return true;
// }
//
// public void afterHandshake(ServerHttpRequest request, ServerHttpResponse
// response, WebSocketHandler wsHandler, Exception ex) {
// }
// }