package com.example.kombat.backend.AST;

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
        if ("row".equals(identifier)) {
            if (game.getCurrentMinion() != null && game.getCurrentMinion().currentHex != null) {
                return game.getCurrentMinion().getCurrentHex().getRow();
            }
        }
        if ("col".equals(identifier)) {
            if (game.getCurrentMinion() != null && game.getCurrentMinion().currentHex != null) {
                return game.getCurrentMinion().getCurrentHex().getCol();
            }
        }
        if ("int".equals(identifier)) {
            if (game.getCurrentMinion() != null) {
                double m = game.getCurrentMinion().getOwner().getCurrentBudget();
                if (m < 1) m =1;
                double turn = game.getCurrentTurn();
                if (turn <= 0) turn = 1;
                double r = game.getConfig().interestPct * Math.log10(m) * Math.log(turn);
                return (long) r;
            }
            return 0;
        }
        if ("maxbudget".equals(identifier)) {
            return game.getConfig().maxBudget;
        }
        if ("spawnleft".equals(identifier)) {
            if (game.getCurrentMinion() != null) {
                int used = game.getCurrentMinion().getOwner().getSpawnsUsed();
                return game.getConfig().maxSpawns - used;
            }
            return 0;
        }


        return game.identifierpack().getOrDefault(identifier, 0L);
    }

}