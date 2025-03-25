package com.example.kombat.backend.GameState;

import com.example.kombat.backend.AST.DoneNode;
import com.example.kombat.backend.AST.GameCommand;
import com.example.kombat.backend.AST.Node;

import java.util.ArrayList;
import java.util.List;

public class Minion implements MinionInterface {
    public int hp;
    protected int defenseFactor;
    protected int order; // Spawn order
    public Hex currentHex;
    protected Player owner;
    protected String typeName;
    // The strategy AST for this minion.
    private Node.StateNode strategy;
    private int typeNumber;
    private String src;

    public Minion(int hp, int defenseFactor, Hex spawnHex, Player owner, int order) {
        this.hp = hp;
        this.defenseFactor = defenseFactor;
        this.currentHex = spawnHex;
        this.owner = owner;
        this.order = order;
        spawnHex.setOccupant(this);
        // Default strategy is 'done' (i.e., do nothing).
        this.strategy = new DoneNode();
    }

    public Player getOwner() {return owner;}

    public void setStrategy(Node.StateNode strategy) {
        this.strategy = strategy;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeNumber(int typeNumber) {
        this.typeNumber = typeNumber;
        this.src  = getSrcForTypeNumber(typeNumber);
    }

    // Helper method to determine src based on typeNumber
    private String getSrcForTypeNumber(int typeNumber) {
        switch (typeNumber) {
            case 0:
                return "/image/Minion/minion1.png";
            case 1:
                return "/image/Minion/minion2.png";
            case 2:
                return "/image/Minion/minion3.png";
            case 3:
                return "/image/Minion/minion4.png";
            case 4:
                return "/image/Minion/minion5.png";
            default:
                return ""; // Default or fallback value
        }
    }

    public String getSrc() {
        return src;
    }

    // Execute the minion's fixed strategy (one action per turn).
    public void executeStrategy(GameCommand game) {
        if (strategy != null) {
            strategy.evaluate(game);
        }
    }

    @Override
    public void done() {
        // End of this minion's action sequence.
        System.out.println("Game.Minion " + order + " finished its actions.");
    }

    @Override
    public void move(Direction direction) {
        Hex target = getTargetHex(direction, 1);
        if (target != null && target.getOccupant() == null) {
            // Check if the owner has enough budget (move cost is 1 unit)
            if (owner.getCurrentBudget() >= 1) {
                owner.adjustBudget(-1);
                // Update the board: remove from current hex and occupy target hex.
                currentHex.setOccupant(null);
                currentHex = target;
                target.setOccupant(this);
                System.out.println("Minion " + order + " move " + direction +  " moved to (" +
                        currentHex.getRow() + ", " + currentHex.getCol() + ")");
            } else {
                System.out.println("Insufficient budget to move.");
            }
        } else {
            System.out.println("Cannot move " + direction + "; blocked or out of bounds.");
        }
    }

    @Override
    public void shoot(Direction direction, long expenditure) {
        if (owner.getCurrentBudget() >= expenditure + 1) {
            owner.adjustBudget(-(expenditure + 1));
            Hex targetHex = getTargetHex(direction,1);
            if (targetHex != null && targetHex.isOccupied()) {
                Minion targetMinion = targetHex.getOccupant();
                // Calculate damage: damage = max(1, expenditure - target's defenseFactor)
                int damage = (int) Math.max(1, expenditure - targetMinion.defenseFactor);
                targetMinion.takeDamage(damage);
                System.out.println("Minion " + order + " shot target at (" +
                        targetHex.getRow() + ", " + targetHex.getCol() + ") for " +
                        damage + " damage.");
                System.out.println("Owner budget :" + owner.budget());
            } else {
                System.out.println("Shot missed: no minion at target hex.");

            }
        } else {
            System.out.println("Insufficient budget to shoot.");
        }
    }

    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
        if (hp == 0) {
            System.out.println("Minion at (" + currentHex.getRow() + ", " + currentHex.getCol() + ") has died.");
            currentHex.setOccupant(null);
            // Remove this minion from its owner's list.
            owner.getMinionsOwned().remove(this);
            strategy = null;
        }
    }

    /**
     * Returns the immediate neighbor hex from a given hex in the specified direction.
     * This method applies the offset rules for a hex grid.
     */
    private Hex getImmediateTargetHex(Direction direction, Hex current) {
        int targetRow = current.getRow();
        int targetCol = current.getCol();
        switch (direction) {
            case up:
                targetRow--;
                break;
            case down:
                targetRow++;
                break;
            case upleft:
                targetCol--;
                if (current.getCol() % 2 == 0) {
                    targetRow--;
                }
                break;
            case upright:
                targetCol++;
                if (current.getCol() % 2 == 0) {
                    targetRow--;
                }
                break;
            case downleft:
                targetCol--;
                if (current.getCol() % 2 != 0) {
                    targetRow++;
                }
                break;
            case downright:
                targetCol++;
                if (current.getCol() % 2 != 0) {
                    targetRow++;
                }
                break;
        }
        Board board = Game.getInstance().getBoard();
        if (board.isWithinBounds(targetRow, targetCol)) {
            return board.getHex(targetRow, targetCol);
        }
        return null;
    }

    /**
     * Returns the hex that is 'distance' steps away in the specified direction,
     * starting from the currentHex.
     */
    public Hex getTargetHex(Direction direction, int distance) {
        Hex hex = currentHex;
        for (int i = 0; i < distance; i++) {
            hex = getImmediateTargetHex(direction, hex);
            if (hex == null) return null;
        }
        return hex;
    }

    @Override
    /**
     * Scans all six directions and returns an encoded value for the nearest enemy minion.
     * Encoding: (distance * 10) + (direction number), where direction number = (ordinal + 1).
     * Returns 0 if no enemy is found.
     */
    public int opponent() {
        int bestDistance = Integer.MAX_VALUE;
        int bestDirectionNum = Integer.MAX_VALUE; // for tie-breaking
        for (Direction d : Direction.values()) {
            int distance = 1;
            while (true) {
                Hex target = getTargetHex(d, distance);
                if (target == null) {
                    break; // went off board
                }
                if (target.getOccupant() != null) {
                    Minion found = target.getOccupant();
                    if (!found.owner.equals(this.owner)) { // enemy found
                        int currentDirNum = d.ordinal() + 1;
                        if (distance < bestDistance ||
                                (distance == bestDistance && currentDirNum < bestDirectionNum)) {
                            bestDistance = distance;
                            bestDirectionNum = currentDirNum;
                        }
                    }
                    break; // Stop scanning this direction once a minion is found.
                }
                distance++;
            }
        }
        if (bestDistance == Integer.MAX_VALUE)
            return 0;
        int opponent_info = bestDistance * 10 + bestDirectionNum;
        System.out.println("Opponent info = " + opponent_info);
        return opponent_info;
    }

    @Override
    /**
     * Scans all six directions and returns an encoded value for the nearest allied minion.
     * Uses the same encoding as opponent() but returns the negative value.
     * Returns 0 if no allied minion is found.
     */
    public int ally() {
        int bestDistance = Integer.MAX_VALUE;
        int bestDirectionNum = Integer.MAX_VALUE;
        for (Direction d : Direction.values()) {
            int distance = 1;
            while (true) {
                Hex target = getTargetHex(d, distance);
                if (target == null) break;
                if (target.getOccupant() != null) {
                    Minion found = target.getOccupant();
                    if (found.owner.equals(this.owner)) { // allied found
                        int currentDirNum = d.ordinal() + 1;
                        if (distance < bestDistance ||
                                (distance == bestDistance && currentDirNum < bestDirectionNum)) {
                            bestDistance = distance;
                            bestDirectionNum = currentDirNum;
                        }
                    }
                    break;
                }
                distance++;
            }
        }
        if (bestDistance == Integer.MAX_VALUE)
            return 0;
        int ally_info = bestDistance * 10 + bestDirectionNum;
        System.out.println("Ally info = " + ally_info);
        return ally_info;
    }

    @Override
    /**
     * Scans in the specified direction and returns encoded information about the first minion encountered.
     * Let:
     *   x = minion's HP,
     *   y = defense factor,
     *   z = distance (in hexes) to the target.
     * If the target is an opponent, return (100*x + 10*y + z); if an ally, return the negative.
     * Returns 0 if no minion is found in that direction.
     */
    public int nearby(Direction direction) {
        int distance = 1;
        while (true) {
            Hex target = getTargetHex(direction, distance);
            if (target == null) {
                System.out.println("Nearby " + direction + " not found");
                return 0;
            }
            if (target.getOccupant() != null) {
                Minion found = target.getOccupant();
                int x = found.hp;
                int y = found.defenseFactor;
                int encoded = 100 * x + 10 * y + distance;
                if (found.owner.equals(this.owner)) {
                    System.out.println("Found ally with Nearby "+ direction + " parameter :" + (-encoded));
                    return -encoded;
                } else {
                    System.out.println("Found opponent with Nearby "+ direction + " parameter :" + encoded);
                    return encoded;
                }
            }
            distance++;
        }
    }

    @Override
    public void checkCurrentHex() {
        System.out.println("Minion " + order + " is at (" +
                currentHex.getRow() + ", " + currentHex.getCol() + ")");
    }

    public int getDefenseFactor() {
        return defenseFactor;
    }

    public Hex getCurrentHex() {
        return currentHex;
    }

    public int getTypeNumberPlusOne() {
        return typeNumber+1;
    }

}
