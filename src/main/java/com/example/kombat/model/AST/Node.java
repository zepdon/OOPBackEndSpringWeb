package com.example.kombat.model.AST;

public class Node {
    public static class StateNode extends Node{
        public StateNode nextState;
        public boolean evaluate(GameCommand game){
            return false;
        };
    }

    public static class Expr extends Node {
        public long eval(GameCommand game){
            return 0;
        }
    }
}