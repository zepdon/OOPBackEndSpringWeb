package com.example.kombat.service;

import com.example.kombat.backend.GameState.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameStateService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameStateService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Method to fetch player 1's owned hexes
    public List<String> getPlayer1OwnedHexes() {
        return Game.getInstance().getPlayerOwnedHexes(1);
    }

    // Method to fetch player 2's owned hexes
    public List<String> getPlayer2OwnedHexes() {
        return Game.getInstance().getPlayerOwnedHexes(2);
    }

    // Method to fetch player 1's budget
    public int getPlayer1Budget() {
        return Game.getInstance().getPlayerOwnBudget(1);
    }

    // Method to fetch player 2's budget
    public int getPlayer2Budget() {
        return Game.getInstance().getPlayerOwnBudget(2);
    }

    // Method to fetch current turn
    public int getCurrentTurn() {
        return Game.getInstance().getCurrentTurn();
    }
}