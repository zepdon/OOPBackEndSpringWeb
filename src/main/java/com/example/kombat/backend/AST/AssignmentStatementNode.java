package com.example.kombat.backend.AST;

public class AssignmentStatementNode extends Node.StateNode {
    private String identifier;
    private Expr expression;

    public AssignmentStatementNode(String identifier, Expr expression) {
        this.identifier = identifier;
        this.expression = expression;
    }
    @Override
    public boolean evaluate(GameCommand game) {
        long val = expression.eval(game);
        // Store in the environment
        game.identifierpack().put(identifier, val);
        return true; // continue
    }
}