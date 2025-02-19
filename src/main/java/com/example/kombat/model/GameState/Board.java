package com.example.kombat.model.GameState;

public class Board {
    private int rows;
    private int cols;
    private Hex[][] grid;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new Hex[rows][cols];
        initializeGrid();
        setupAdjacentHexes();
    }

    private void initializeGrid() {
        for (int r = 1; r < rows; r++) {
            for (int c = 1; c < cols; c++) {
                grid[r][c] = new Hex(r, c);
            }
        }
    }

    private void setupAdjacentHexes() {
        for (int r = 1; r < rows; r++) {
            for (int c = 1; c < cols; c++) {
                Hex hex = grid[r][c];
                // Determine offsets based on the column parity.
                int[][] offsets;
                if (c % 2 == 0) { // even column
                    offsets = new int[][]{
                            {-1, 0},  // up
                            {1, 0},  // down
                            {-1, -1},  // upleft
                            {-1, 1},  // upright
                            {0, -1},  // downleft
                            {0, 1}   // downright
                    };
                } else { // odd column
                    offsets = new int[][]{
                            {-1, 0},  // up
                            {1, 0},  // down
                            {0, -1},  // upleft
                            {0, 1},  // upright
                            {1, -1},  // downleft
                            {1, 1}   // downright
                    };
                }
                // For each offset, compute the neighbor position.
                for (int i = 0; i < offsets.length; i++) {
                    int nRow = r + offsets[i][0];
                    int nCol = c + offsets[i][1];
                    if (isWithinBounds(nRow, nCol)) {
                        hex.addAdjacentHex(grid[nRow][nCol]);
                    }
                }
            }
        }
    }

    public boolean isWithinBounds(int row, int col) {
        return (row > 0 && row < rows && col > 0 && col < cols);
    }

    public Hex getHex(int row, int col) {
        if (isWithinBounds(row, col)) {
            return grid[row][col];
        }
        return null;
    }

    public void printBoard() {
        // Print column headers
        System.out.print("     ");
        for (int c = 1; c < cols; c++) {
            System.out.printf("%-8d", c);
        }
        System.out.println();

        // Print a separator line
        System.out.print("     ");
        for (int c = 1; c < cols; c++) {
            System.out.print("--------");
        }
        System.out.println();

        // Print each row with row number on the left
        for (int r = 1; r < rows; r++) {
            // Print row header
            System.out.printf("%-4d|", r);
            for (int c = 1; c < cols; c++) {
                Hex hex = grid[r][c];
                String cellStr;
                if (hex.isOccupied()) {
                    // If the hex is occupied, print PlayerId + "M" + minion's order.
                    if (hex.getOccupant() != null && hex.getOccupant().getOwner() != null) {
                        int playerId = hex.getOccupant().getOwner().getId();
                        int minionOrder = hex.getOccupant().order; // assuming order is public; otherwise add a getter
                        cellStr = playerId + "M" + minionOrder;
                    } else {
                        cellStr = "??";
                    }
                } else if (hex.getOwner() != null) {
                    // Owned but unoccupied: mark as spawnable with "S" and player id.
                    cellStr = hex.getOwner().getId() + "S";
                } else {
                    // Empty hex.
                    cellStr = ".";
                }
                System.out.printf("%-8s", cellStr);
            }
            System.out.println();
        }
    }

}
