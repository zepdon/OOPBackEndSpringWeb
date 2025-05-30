package com.example.kombat.backend.AST;

//import Error.*;
//import GameState.Direction;
//import parser.*;
import com.example.kombat.backend.Error.*;
import com.example.kombat.backend.parser.*;
import com.example.kombat.backend.GameState.Direction;

public class AttackCommandNode extends Node.StateNode{
    Direction direction;
    Expr expr;
    public AttackCommandNode(Direction direction, Expr expr){
        this.direction = direction;
        this.expr = expr;
    }
    @Override
    public boolean evaluate(GameCommand game){
        // 1) Evaluate the expression to get expenditure
        long expenditure = expr.eval(game);
        // If shoot(...) returns false, we end the script
        return game.shoot(direction, expenditure);
    }

}