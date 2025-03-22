package com.example.kombat.controller;

import com.example.kombat.backend.GameState.Minion;
import com.example.kombat.models.TurnData;
import com.example.kombat.service.GameStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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

    // Handle requests for minion type
    @MessageMapping("/board/request-minion-type")
    public void handleMinionTypeRequest() {
        int minionType = gameStateService.getMinionType();
        messagingTemplate.convertAndSend("/topic/minion-type", minionType);
    }
//    @MessageMapping("/board/request-player1-minions")
//    public void handlePlayer1MinionsRequest() {
//        List<Minion> player1Minions = gameStateService.getMinionPlayer1();
//        messagingTemplate.convertAndSend("/topic/player1-minions", player1Minions);
//    }
//
//    @MessageMapping("/board/request-player2-minions")
//    public void handlePlayer2MinionsRequest() {
//        List<Minion> player2Minions = gameStateService.getMinionPlayer2();
//        messagingTemplate.convertAndSend("/topic/player2-minions", player2Minions);
//    }
    @MessageMapping("/board/perform-turn")
    public void performTurn(TurnData turnData) {
        int player = 0;
        if(gameStateService.getCurrentTurn() % 2 == 0) {
            player = 1;
        }
        if ( player == 0 ) {
            System.out.println("0000"+turnData.getMinionRow()+turnData.getMinionCol()+turnData.getHexRow()+turnData.getHexCol());
            gameStateService.performturnPlayer1(turnData.getMinionRow(), turnData.getMinionCol(), turnData.getHexRow(), turnData.getHexCol(), turnData.getTypeIndex());
            handlePlayer1HexesRequest();
            handlePlayer2HexesRequest();
            handlePlayer1BudgetRequest();
            handlePlayer2BudgetRequest();
            handleCurrentTurnRequest();
        } else if ( player == 1 ) {
            gameStateService.performturnPlayer2(turnData.getMinionRow(), turnData.getMinionCol(), turnData.getHexRow(), turnData.getHexCol(), turnData.getTypeIndex());
            handlePlayer1HexesRequest();
            handlePlayer2HexesRequest();
            handlePlayer1BudgetRequest();
            handlePlayer2BudgetRequest();
            handleCurrentTurnRequest();
        }
    }

    // Send initial player-owned hexes data when a client connects
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        List<String> player1Hexes = gameStateService.getPlayer1OwnedHexes();
        List<String> player2Hexes = gameStateService.getPlayer2OwnedHexes();
        messagingTemplate.convertAndSend("/topic/player1-hexes", player1Hexes);
        messagingTemplate.convertAndSend("/topic/player2-hexes", player2Hexes);

        int budget1 = gameStateService.getPlayer1Budget();
        messagingTemplate.convertAndSend("/topic/player1-budget", budget1);

        int budget2 = gameStateService.getPlayer2Budget();
        messagingTemplate.convertAndSend("/topic/player2-budget", budget2);

        int currentTurn = gameStateService.getCurrentTurn();
        messagingTemplate.convertAndSend("/topic/current-turn", currentTurn);

        int minionType = gameStateService.getMinionType();
        messagingTemplate.convertAndSend("/topic/minion-type", minionType);

    }
}