package com.example.kombat.backend.GameState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
    private static int nextId = 1;
    private int id;
    private double currentBudget;
    private double interestRatePercentage;

    private List<Hex> boughtHexes;
    private List<Minion> minionsOwned;
    private int spawnsUsed;
    private boolean isBot;
    private Set<Hex> buyableHexes;

    private int spawnAvalible;

    public Player(double initBudget, double interestRatePercentage, boolean isBot) {
        this.id = nextId++;
        this.currentBudget = initBudget;
        this.interestRatePercentage = interestRatePercentage;
        this.boughtHexes = new ArrayList<>();
        this.minionsOwned = new ArrayList<>();
        this.spawnsUsed = 0;
        this.isBot = isBot;
        this.buyableHexes = new HashSet<>();
        this.spawnAvalible = 15; //from config
    }

    public Set<Hex> getBuyableHexes() {
        for (Hex owned : boughtHexes) {
            for (Hex adj : owned.getAdjacentHexes()) {
                if (adj.getOwner() == null) {
                    buyableHexes.add(adj);
                }
            }
        }
        return buyableHexes;
    };
    /**
     * Returns a list of hexes owned by this player.
     * Each hex is represented as a string in the format "(row,col)".
     */
    public List<String> getOwnedHexes() {
        List<String> ownedHexes = new ArrayList<>();
        for (Hex hex : boughtHexes) {
            ownedHexes.add("(" + hex.getRow() + "," + hex.getCol() + ")");
        }
        return ownedHexes;
    }

    public List<Minion> getMinions() {
        return minionsOwned;
    }
    public int getId() { return id; }

    public double getCurrentBudget() { return currentBudget; }

    //budget but return as int
    public int budget() {
        return (int) currentBudget;
    }

    public void adjustBudget(double amount) {
        currentBudget += amount;
    }
    public double getInterestRatePercentage() {
        return interestRatePercentage;
    }

    public List<Hex> getBoughtHexes() {
        return boughtHexes;
    }
    public List<Minion> getMinionsOwned() {
        return minionsOwned;
    }
    public int getSpawnsUsed() {
        return spawnsUsed;
    }
    public void incrementSpawnsUsed() {
        spawnsUsed++;
    }

    public void setSpawnAvalible() {
        spawnAvalible = (int) Game.getInstance().getConfig().getMaxSpawns() - spawnsUsed;
    }

    public int getSpawnAvalible() {
        return spawnAvalible;
    }

    public boolean isBot() { return isBot; }

    //Hex cost in Config file
    public void buyHex(Hex hex, long cost) {
        if (!canBuyHex(hex)) {
            System.out.println("You can only buy a hex that is adjacent to one of your owned hexes.");
            return;
        }
        if (currentBudget >= cost) {
            adjustBudget(-cost);
            boughtHexes.add(hex);
            hex.setOwner(this);
            System.out.println("Bought hex at (" + hex.getRow() + ", " + hex.getCol() + ")");
        } else {
            System.out.println("Insufficient funds to buy hex at (" + hex.getRow() + ", " + hex.getCol() + ").");
        }
    }

    public void addMinion(Minion minion) {
        minionsOwned.add(minion);
    }

    public boolean canBuyHex(Hex hex) {
        // If hex is already owned, no
        if (hex.getOwner() != null) {
            return false;
        }
        // Must be adjacent to a hex we own
        for (Hex owned : boughtHexes) {
            if (owned.getAdjacentHexes().contains(hex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prints detailed status information about the player.
     */
    public void printStatus() {
        System.out.println("==================================");
        System.out.println("Player " + id + " Status:");
        System.out.println("Current turn " + Game.getInstance().getCurrentTurn());
        System.out.println("Budget: " + budget());
        System.out.println("Minions Count: " + minionsOwned.size());

        // Print owned hexes.
        System.out.print("Owned Hexes: ");
        for (Hex hex : boughtHexes) {
            System.out.print("(" + hex.getRow() + "," + hex.getCol() + ") ");
        }
        System.out.println();

        getBuyableHexes();

        System.out.print("Buyable Hexes: ");
        for (Hex hex : buyableHexes) {
            System.out.print("(" + hex.getRow() + "," + hex.getCol() + ") ");
        }
        System.out.println();

        // Print minions details.
        System.out.println("Minions:");
        for (Minion m : minionsOwned) {
            // Print in the format: <PlayerId>M<order> - <MinionTypeName> at (row,col) - HP: <hp>
            System.out.println("   " + m.getOwner().getId() + "M" + m.order + " - " + m.getTypeName() +
                    " at (" + m.currentHex.getRow() + "," + m.currentHex.getCol() + ") - HP: " + m.hp);
        }
        System.out.println("==================================");
    }
}
