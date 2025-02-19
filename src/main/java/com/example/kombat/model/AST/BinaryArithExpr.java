package com.example.kombat.model.AST;

import com.example.kombat.model.Error.*;

public class BinaryArithExpr extends Node.Expr {
    private Expr left;
    private String op;
    private Expr right;

    public BinaryArithExpr( Expr left, String op, Expr right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
    @Override
    public long eval(GameCommand game) {
        long lv = left.eval(game);
        long rv = right.eval(game);
        if (op.equals("+")) return lv + rv;
        if (op.equals("-")) return lv - rv;
        if (op.equals("*")) return lv * rv;
        if (op.equals("/")){
            if (rv == 0) {
                throw new SyntaxError("Division by zero");
            }
            return lv / rv;
        }
        if (op.equals("%")){
            if (rv == 0) {
                throw new SyntaxError("Modulo by zero");
            }
            return lv % rv;
        }
        if (op.equals("^")) return lv ^ rv;
        throw new EvalError("unknown op: " + op);
    }
}