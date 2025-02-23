package com.example.kombat.backend.AST;

//import Error.*;
//import GameState.Direction;
//import parser.*;
import com.example.kombat.backend.Error.*;
import com.example.kombat.backend.GameState.Direction;
import com.example.kombat.backend.parser.*;

public class NearbyNode extends Node.Expr{
    Direction direction;
    public NearbyNode(Direction direction){
        this.direction = direction;
    }
    @Override
    public long eval(GameCommand game){
        //call function with direction here
        return game.getNearbyInfo(direction);
    }
}