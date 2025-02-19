package com.example.kombat.model.AST;

import com.example.kombat.model.GameState.Direction;

public class MoveCommandNode extends Node.StateNode{
    Direction direction;
    public MoveCommandNode(Direction direction) {
        this.direction = direction;
    }
    @Override
    public boolean evaluate(GameCommand game){
        // If move(...) returns false, we stop the script
        return game.move(direction);
    }
}