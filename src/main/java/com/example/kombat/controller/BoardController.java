package com.example.kombat.controller;

import com.example.kombat.backend.GameState.Game;
import com.example.kombat.models.BoardUpdate;
import com.example.kombat.service.GameStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class BoardController {

    @Autowired
    private GameStateService gameStateService;

    @MessageMapping("/board/update") // Handles messages sent to /app/board/update
    @SendTo("/topic/board") // Sends the response to /topic/board
    public BoardUpdate handleBoardUpdate(BoardUpdate update) {
        // Process the update (e.g., save to database, validate, etc.)
        System.out.println("Received board update: " + update);
        return update; // Broadcast the update to all subscribers
    }
}