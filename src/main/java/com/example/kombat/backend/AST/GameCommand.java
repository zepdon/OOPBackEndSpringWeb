package com.example.kombat.backend.AST;

//import GameState.ConfigLoader;
//import GameState.Direction;
//import GameState.Minion;
import com.example.kombat.backend.GameState.ConfigLoader;
import com.example.kombat.backend.GameState.Direction;
import com.example.kombat.backend.GameState.Minion;

import java.util.Map;

public interface GameCommand {
    Map<String, Long> identifierpack();

    boolean move(Direction direction);

    boolean shoot(Direction direction, long expenditure);

    boolean done();

    // Ally / Opponent / Nearby info
    long getAllyInfo();
    long getOpponentInfo();
    long getNearbyInfo(Direction direction);

    // Special variables:
    // Returns the player's remaining budget.
    int takeBudget();

    // Returns a random value between 0 and 999 (inclusive).
    long getRandom();

    Minion getCurrentMinion();
    int getCurrentTurn();
    ConfigLoader getConfig();
}
