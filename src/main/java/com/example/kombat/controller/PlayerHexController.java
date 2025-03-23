package com.example.kombat.controller;

import com.example.kombat.backend.GameState.Game;
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

    @MessageMapping("/board/request-all-minion-name")
    public void handleAllMinionNameRequest() {
        List<String> MinionName = gameStateService.getAllMinionTypesName();
        messagingTemplate.convertAndSend("/topic/all-minion-name", MinionName);
    }

    @MessageMapping("/board/request-all-minion-defence")
    public void handleAllMinionDefenceRequest() {
        List<Integer> MinionDefence = gameStateService.getAllMinionTypesDefenseFactor();
        messagingTemplate.convertAndSend("/topic/minion-defence", MinionDefence);
    }

    @MessageMapping("/board/request-minion")
    public void handleMinionRequest() {
        List<List<String>> Minion = gameStateService.getAllMinionLocationAndType();
        System.out.println(Minion);
        messagingTemplate.convertAndSend("/topic/minion", Minion);
    }

    @MessageMapping("/board/request-GameMode")
    public void handleGameModeRequest() {
        int gameMode = gameStateService.getGameMode();
        messagingTemplate.convertAndSend("/topic/gameMode", gameMode);
    }
    @MessageMapping("/board/request-GameResult")
    public void handleGameResultRequest() {
        if(Game.getInstance().gameOver()){
            int result = Game.getInstance().GameResult();
            messagingTemplate.convertAndSend("/topic/gameResult", result);
        }
    }
    @MessageMapping("/board/perform-turn")
    public void performTurn(TurnData turnData) {
        int player = 0;
        if(gameStateService.getCurrentTurn() % 2 == 0) {
            player = 1;
        }
        if ( player == 0 ) {
            gameStateService.performturnPlayer1(turnData.getMinionRow(), turnData.getMinionCol(), turnData.getHexRow(), turnData.getHexCol(), turnData.getTypeIndex());
            handlePlayer1HexesRequest();
            handlePlayer2HexesRequest();
            handlePlayer1BudgetRequest();
            handlePlayer2BudgetRequest();
            handleCurrentTurnRequest();
            handleMinionRequest();
            handleGameResultRequest();
        } else if ( player == 1 ) {
            gameStateService.performturnPlayer2(turnData.getMinionRow(), turnData.getMinionCol(), turnData.getHexRow(), turnData.getHexCol(), turnData.getTypeIndex());
            handlePlayer1HexesRequest();
            handlePlayer2HexesRequest();
            handlePlayer1BudgetRequest();
            handlePlayer2BudgetRequest();
            handleCurrentTurnRequest();
            handleMinionRequest();
            handleGameResultRequest();
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

        List<String> MinionName = gameStateService.getAllMinionTypesName();
        messagingTemplate.convertAndSend("/topic/all-minion-name", MinionName);

        List<Integer> MinionDefence = gameStateService.getAllMinionTypesDefenseFactor();
        messagingTemplate.convertAndSend("/topic/minion-defence", MinionDefence);

        int gameMode = gameStateService.getGameMode();
        messagingTemplate.convertAndSend("/topic/gameMode", gameMode);
    }
}