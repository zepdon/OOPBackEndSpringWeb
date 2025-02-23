package com.example.kombat.backend.GameState;

import java.util.ArrayList;
import java.util.List;

public class Hex {
    private int row;
    private int col;
    private Minion occupant;  // Reference to the minion on this hex (null if none)
    private Player owner;      // Who purchased this hex (if applicable)
    private List<Hex> adjacentHexes;

    public Hex(int row, int col) {
        this.row = row;
        this.col = col;
        this.occupant = null; // Initially, no minion is present
        this.owner = null; // Initially, the hex is unowned
        this.adjacentHexes = new ArrayList<>();
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public Minion getOccupant() { return occupant; }
    public void setOccupant(Minion m) { occupant = m; }

    public Player getOwner() { return owner; }
    public void setOwner(Player p) { owner = p; }

    public List<Hex> getAdjacentHexes() { return adjacentHexes; }
    public void addAdjacentHex(Hex hex) {
        if (!adjacentHexes.contains(hex)) {
            adjacentHexes.add(hex);
        }
    }

    public boolean isOccupied() {
        return occupant != null;
    }

    @Override
    public String toString() {
        return "Hex(" + row + ", " + col + ")";
    }
}
