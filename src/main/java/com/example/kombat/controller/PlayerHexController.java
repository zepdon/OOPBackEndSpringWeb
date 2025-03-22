package com.example.kombat.controller;

import com.example.kombat.service.GameStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.List;

@Controller
public class PlayerHexController {

    @Autowired
    private GameStateService gameStateService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Handle requests for player 1's owned hexes
    @MessageMapping("/board/request-player1-hexes")
    public void handlePlayer1HexesRequest() {
        List<String> player1Hexes = gameStateService.getPlayer1OwnedHexes();
        messagingTemplate.convertAndSend("/topic/player1-hexes", player1Hexes);
    }

    // Handle requests for player 2's owned hexes
    @MessageMapping("/board/request-player2-hexes")
    public void handlePlayer2HexesRequest() {
        List<String> player2Hexes = gameStateService.getPlayer2OwnedHexes();
        messagingTemplate.convertAndSend("/topic/player2-hexes", player2Hexes);
    }
    // Handle requests for player 1's budget
    @MessageMapping("/board/request-player1-budget")
    public void handlePlayer1BudgetRequest() {
        int budget = gameStateService.getPlayer1Budget();
        messagingTemplate.convertAndSend("/topic/player1-budget", budget);
    }

    // Handle requests for player 2's budget
    @MessageMapping("/board/request-player2-budget")
    public void handlePlayer2BudgetRequest() {
        int budget = gameStateService.getPlayer2Budget();
        messagingTemplate.convertAndSend("/topic/player2-budget", budget);
    }
    // Handle requests for current turn
    @MessageMapping("/board/request-current-turn")
    public void handleCurrentTurnRequest() {
        int currentTurn = gameStateService.getCurrentTurn();
        messagingTemplate.convertAndSend("/topic/current-turn", currentTurn);
    }
    // Send initial player-owned hexes data when a client connects
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        List<String> player1Hexes = gameStateService.getPlayer1OwnedHexes();
        List<String> player2Hexes = gameStateService.getPlayer2OwnedHexes();
        messagingTemplate.convertAndSend("/topic/player1-hexes", player1Hexes);
        messagingTemplate.convertAndSend("/topic/player2-hexes", player2Hexes);
//        System.out.println("sent player1 hex "+player1Hexes);
//        System.out.println("sent player2 hex "+player2Hexes);
        int budget1 = gameStateService.getPlayer1Budget();
        messagingTemplate.convertAndSend("/topic/player1-budget", budget1);
//        System.out.println("sent player1 budget "+budget1);
        int budget2 = gameStateService.getPlayer2Budget();
        messagingTemplate.convertAndSend("/topic/player2-budget", budget2);
//        System.out.println("sent player2 budget "+budget2);
        int currentTurn = gameStateService.getCurrentTurn();
        messagingTemplate.convertAndSend("/topic/current-turn", currentTurn);
    }
}