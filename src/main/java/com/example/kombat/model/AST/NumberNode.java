package com.example.kombat.model.AST;

public class NumberNode extends Node.Expr{
    private long value;
    public NumberNode(long value) {
        this.value = value;
    }
    @Override
    public long eval(GameCommand game){
        return value;
    }
}