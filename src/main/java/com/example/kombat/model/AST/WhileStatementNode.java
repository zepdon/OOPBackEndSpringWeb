package com.example.kombat.model.AST;

public class WhileStatementNode extends Node.StateNode{
    Node.Expr expr;
    Node.StateNode body;
    public WhileStatementNode(Node.Expr expr, Node.StateNode body) {
        this.expr = expr;
        this.body = body;
    }
    @Override
    public boolean evaluate(GameCommand game){
        for (int counter = 0; counter < 10000 && expr.eval(game) > 0; counter++){
            if(!body.evaluate(game)){
                return false;
            };
        }
        return true;
    }
}