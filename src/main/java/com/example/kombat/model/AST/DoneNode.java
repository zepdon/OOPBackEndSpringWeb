package com.example.kombat.model.AST;

public class DoneNode extends Node.StateNode{
    @Override
    public boolean evaluate(GameCommand game){
        // The 'done' command ends the minion's action sequence for this turn
        // Typically you’d call game.done() and then return false to stop further commands
        return game.done();
    }
}