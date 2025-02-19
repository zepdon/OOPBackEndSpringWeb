package com.example.kombat.model.AST;

import com.example.kombat.model.GameState.Direction;

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
}
