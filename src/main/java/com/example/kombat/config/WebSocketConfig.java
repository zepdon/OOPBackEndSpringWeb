// src/main/java/com/example/kombat/config/WebSocketConfig.java
package com.example.kombat.config;

import com.example.kombat.websocket.BoardWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private BoardWebSocketHandler boardWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(boardWebSocketHandler, "/ws/board")
                .setAllowedOrigins("*"); // Adjust origins as needed
    }
}
