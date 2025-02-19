package com.example.kombat.model.AST;

public class OpponentNode extends Node.Expr{
    @Override
    public long eval(GameCommand game){
        //add function here
        return game.getOpponentInfo();

    }
}