package com.example.kombat.backend.AST;

//import Error.*;
//import GameState.Direction;
//import parser.*;
import com.example.kombat.backend.Error.*;
import com.example.kombat.backend.GameState.Direction;
import com.example.kombat.backend.parser.*;

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