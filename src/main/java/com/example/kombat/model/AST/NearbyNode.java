package com.example.kombat.model.AST;

import com.example.kombat.model.GameState.Direction;

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