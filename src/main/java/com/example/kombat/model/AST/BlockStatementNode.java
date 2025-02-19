package com.example.kombat.model.AST;

import java.util.List;

public class BlockStatementNode extends Node.StateNode{
    List<StateNode> statements;
    public BlockStatementNode(List<StateNode> statements) {
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