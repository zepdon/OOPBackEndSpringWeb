package com.example.kombat.backend.AST;

import java.util.List;

public class BlockStatementNode extends Node.StateNode{
    List<Node.StateNode> statements;
    public BlockStatementNode(List<Node.StateNode> statements) {
        this.statements = statements;
    }
    @Override
    public boolean evaluate(GameCommand game){
        for(Node.StateNode state: statements){
            if(!state.evaluate(game)){
                return false;
            }
        }
        return true;
    }

}