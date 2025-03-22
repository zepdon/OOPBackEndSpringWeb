package com.example.kombat.models;

public class TurnData {
    private int minionRow;
    private int minionCol;
    private int hexRow;
    private int hexCol;
    private int typeIndex;

    // Getters and setters
    public int getMinionRow() {
        return minionRow;
    }

    public void setMinionRow(int minionRow) {
        this.minionRow = minionRow;
    }

    public int getMinionCol() {
        return minionCol;
    }

    public void setMinionCol(int minionCol) {
        this.minionCol = minionCol;
    }

    public int getHexRow() {
        return hexRow;
    }

    public void setHexRow(int hexRow) {
        this.hexRow = hexRow;
    }

    public int getHexCol() {
        return hexCol;
    }

    public void setHexCol(int hexCol) {
        this.hexCol = hexCol;
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(int typeIndex) {
        this.typeIndex = typeIndex;
    }
}