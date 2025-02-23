// src/main/java/com/example/kombat/service/GameSettings.java
package com.example.kombat.service;

import com.example.kombat.backend.GameState.GameMode;
import org.springframework.stereotype.Service;

@Service
public class GameSettings {
    private GameMode gameMode;

    public synchronized void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public synchronized GameMode getGameMode() {
        return gameMode;
    }
}
