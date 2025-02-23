package com.example.kombat.backend.AST;

public class AllyNode extends Node.Expr{
    @Override
    public long eval(GameCommand game){
        //function here
        return game.getAllyInfo();
    }
}