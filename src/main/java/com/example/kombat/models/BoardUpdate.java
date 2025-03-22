package com.example.kombat.models;

public class BoardUpdate {
    private int row;
    private int col;
    private String player;
    private String action;
    private Minion minion;

    // Getters and setters
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Minion getMinion() {
        return minion;
    }

    public void setMinion(Minion minion) {
        this.minion = minion;
    }

    @Override
    public String toString() {
        return "BoardUpdate{" +
                "row=" + row +
                ", col=" + col +
                ", player='" + player + '\'' +
                ", action='" + action + '\'' +
                ", minion=" + minion +
                '}';
    }
}