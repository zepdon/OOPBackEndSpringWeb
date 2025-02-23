package com.example.kombat.backend.AST;

public class IfStatementNode extends Node.StateNode {
    Expr condition;
    Node.StateNode thenStatement;
    Node.StateNode elseStatement;
    public IfStatementNode(Expr condition, Node.StateNode thenStatement, Node.StateNode elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }
    @Override
    public boolean evaluate(GameCommand game){
        if(condition.eval(game) > 0){
            return thenStatement.evaluate(game);
        }else {
            return elseStatement.evaluate(game);
        }
    }

}