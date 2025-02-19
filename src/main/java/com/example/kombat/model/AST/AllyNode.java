package com.example.kombat.model.AST;

public class AllyNode extends Node.Expr{
    @Override
    public long eval(GameCommand game){
        //function here
        return game.getAllyInfo();
    }
}