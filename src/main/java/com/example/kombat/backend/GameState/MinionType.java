package com.example.kombat.backend.GameState;

//import AST.Node;
import com.example.kombat.backend.AST.Node;

public class MinionType {
    private String name;
    private int defenseFactor;
    private Node.StateNode strategyAST;

    public MinionType(String name, int defenseFactor, Node.StateNode strategyAST) {
        this.name = name;
        this.defenseFactor = defenseFactor;
        this.strategyAST = strategyAST;
    }

    public String getName() {
        return name;
    }

    public int getDefenseFactor() {
        return defenseFactor;
    }

    public Node.StateNode getStrategyAST() {
        return strategyAST;
    }
}
