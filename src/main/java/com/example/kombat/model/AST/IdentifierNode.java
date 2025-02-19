package com.example.kombat.model.AST;

public class IdentifierNode extends Node.Expr{
    private String identifier;
    public IdentifierNode(String identifier) {
        this.identifier = identifier;
    }
    @Override
    public long eval(GameCommand game){
        // Handle special, read-only variables.
        if ("budget".equals(identifier)) {
            return game.takeBudget();
        }
        if ("random".equals(identifier)) {
            return game.getRandom();
        }


        return game.identifierpack().getOrDefault(identifier, 0L);
    }

}