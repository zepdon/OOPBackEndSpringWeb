package com.example.kombat.backend.GameState;

import com.example.kombat.backend.AST.*;

import java.util.*;

/**
 * To Oat or whoever reading this
 * start() is a game loop for terminal play so we don't need to call that any more (it's literally do nothing rn)
 * in one turn we
 * 1st apply budget
 * 2nd Perform turn (Player that turn , minion row , minion col , hex row , hex col , minion type)
 * 3rd endturn (minion execute strategy here then currentTurn++ and check endgame conditions)
 */


/**
 * Main game engine, implemented as a singleton.
 * Responsible for turn logic, board, players, endgame checking, etc.
 */
public class Game implements GameCommand {
    private static Game instance;
    private Board board;
    private GameMode gameMode;
    private GameStateEnum gameState;
    private ConfigLoader config;
    private List<Player> players;
    private Player winner;

    private int currentTurn;     // which turn we are on

    private Minion currentMinion;
    private List<MinionType> minionTypes; // The available minion kinds

    private Map<String, Long> variableEnv; // For storing global or shared variables if needed
    private Scanner scanner = new Scanner(System.in);

    public int getminiontype(){
        return minionTypes.size();
    }
    private Game(ConfigLoader config, GameMode mode) {
        this.config = config;
        this.gameMode = mode;
        this.gameState = GameStateEnum.RUNNING;
        this.board = new Board(9, 9);  // 9x9 to allow indexing 1..8
        this.players = new ArrayList<>();
        this.currentTurn = 1;
        this.variableEnv = new HashMap<>();

        // Create players
        setupPlayer();
        setupSpawnZones();
    }

    /**
     * Prints the hexes owned by the specified player.
     * @param player The player whose hexes will be printed.
     */
    private void printPlayerOwnedHexes(Player player) {
        List<String> ownedHexes = player.getOwnedHexes();
        System.out.println("Player " + player.getId() + " owns the following hexes: " + ownedHexes);
    }
    /**
     * Returns a list of hexes owned by the specified player.
     * @param playerId The ID of the player (1 or 2).
     * @return A list of hexes in the format "(row,col)".
     */
    public List<String> getPlayerOwnedHexes(int playerId) {
        if (playerId < 1 || playerId > players.size()) {
            throw new IllegalArgumentException("Invalid player ID. Player IDs start from 1.");
        }
        Player player = players.get(playerId - 1); // Players are stored in a 0-based list
        return player.getOwnedHexes();
    }
    public int getPlayerOwnBudget(int playerId) {
        if (playerId < 1 || playerId > players.size()) {
            throw new IllegalArgumentException("Invalid player ID. Player IDs start from 1.");
        }
        Player player = players.get(playerId - 1);
        return player.budget();
    }

    /**
     * Public method to init the singleton.
     */
    public static void initializeGame(ConfigLoader config, GameMode mode) {
        if (instance == null) {
            instance = new Game(config, mode);
        }
    }

    public static Game getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Game not initialized!");
        }
        return instance;
    }

    // List of minion types. Must be set before the game starts.
    public void setMinionTypes(List<MinionType> types) {
        this.minionTypes = types;
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void setupPlayer() {
        if (gameMode == GameMode.DUEL) {
            players.add(new Player(config.initBudget, config.interestPct, false));
            players.add(new Player(config.initBudget, config.interestPct, false));
        } else if (gameMode == GameMode.SOLITAIRE) {
            players.add(new Player(config.initBudget, config.interestPct, false)); // human
            players.add(new Player(config.initBudget, config.interestPct, true));  // bot
        } else {
            players.add(new Player(config.initBudget, config.interestPct, true));  // bot 1
            players.add(new Player(config.initBudget, config.interestPct, true));  // bot 2
        }
    }

    /**
     * Example method: sets up initial spawn zones for player 1 and 2.
     * You can adjust these as needed.
     */
    private void setupSpawnZones() {
        Player p1 = players.get(0);
        board.getHex(1, 1).setOwner(p1);
        board.getHex(1, 2).setOwner(p1);
        board.getHex(1, 3).setOwner(p1);
        board.getHex(2, 1).setOwner(p1);
        board.getHex(2, 2).setOwner(p1);
        p1.getBoughtHexes().add(board.getHex(1, 1));
        p1.getBoughtHexes().add(board.getHex(1, 2));
        p1.getBoughtHexes().add(board.getHex(1, 3));
        p1.getBoughtHexes().add(board.getHex(2, 1));
        p1.getBoughtHexes().add(board.getHex(2, 2));

        Player p2 = players.get(1);
        board.getHex(7, 7).setOwner(p2);
        board.getHex(7, 8).setOwner(p2);
        board.getHex(8, 6).setOwner(p2);
        board.getHex(8, 7).setOwner(p2);
        board.getHex(8, 8).setOwner(p2);
        p2.getBoughtHexes().add(board.getHex(7, 7));
        p2.getBoughtHexes().add(board.getHex(7, 8));
        p2.getBoughtHexes().add(board.getHex(8, 6));
        p2.getBoughtHexes().add(board.getHex(8, 7));
        p2.getBoughtHexes().add(board.getHex(8, 8));
    }

    /**
     * Main game loop for Terminal play:
     * 1) Each player takes a turn.
     * 2) Increase budget, apply interest.
     * 3) Optionally buy/spawn.
     * 4) Minions execute strategies.
     * 5) Check end conditions.
     */
    public void start() {
//        while (gameState == GameStateEnum.RUNNING && currentTurn <= config.maxTurns) {
//            for (Player player : players) {
//                System.out.println("\n=== Turn " + currentTurn + " for Player " + player.getId() + " ===");
//
//                if (!player.isBot()) {
//                    Scanner scanner = new Scanner(System.in);
//                    System.out.print("Enter minion row (e.g., 0): ");
//                    int minionrow = scanner.nextInt();
//
//                    System.out.print("Enter minion column (e.g., 0): ");
//                    int minioncol = scanner.nextInt();
//
//                    System.out.print("Enter hex row (e.g., 0): ");
//                    int hexrow = scanner.nextInt();
//
//                    System.out.print("Enter hex column (e.g., 0): ");
//                    int hexcol = scanner.nextInt();
//
//                    System.out.print("Enter type index (e.g., 0): ");
//                    int typeindex = scanner.nextInt();
//
//                    applyBudgetAndInterest(player);
//                    performTurn(player, minionrow, minioncol, hexrow, hexcol, typeindex);
//                    endturn();
//                } else {
//                    int minionrow = 0;
//                    int minioncol = 0;
//                    int hexrow = 0;
//                    int hexcol = 0;
//                    int typeindex = 0;
//                    applyBudgetAndInterest(player);
//                    performTurn(player, minionrow, minioncol, hexrow, hexcol, typeindex);
//                    endturn();
//                }
//            }
//        }
    }

    /**
     * Execute minion strategy of a player
     *
     * @param player
     */
    private void executeMinionStrategy(Player player) {
        if (currentTurn > 2) {
            for (Minion m : new ArrayList<>(player.getMinionsOwned())) {
                setCurrentMinion(m);
                System.out.println("Minion " + m.order + " executing strategy...");
                String input = "";
                m.executeStrategy(this); // calls StateNode.evaluate(this)
                board.printBoard();
            }
        }
    }

    /**
     * end turn function
     * also checks endgame conditions
     * increases turn
     */
    public void endturn() {
        if (checkEndGame()) {
            gameState = GameStateEnum.ENDED;
            determineWinner();
            endGameSummary();
        }
        currentTurn++;
        System.out.println(currentTurn);
        System.out.println("turn end");
    }

    /**
     * Applies turnBudget plus interest to the player's budget.
     * interest = m * (r / 100), where r = interest_pct * log10(m) * ln(currentTurn)
     */
    public void applyBudgetAndInterest(Player player) {
        // Add turn budget
        if (Game.getInstance().currentTurn > 2) {
            player.adjustBudget(config.turnBudget);

            // interest = m * (r / 100), r = interestPct * log10(m) * ln(currentTurn)
            double m = player.getCurrentBudget();
            double turnCount = (currentTurn <= 0 ? 1 : currentTurn);
            if (m < 1) m = 1; // avoid log10(0)
            double r = config.interestPct * Math.log10(m) * Math.log(turnCount);
            double interest = m * (r / 100.0);
            player.adjustBudget(interest);

            // Clamp to maxBudget
            if (player.getCurrentBudget() > config.maxBudget) {
                player.adjustBudget(config.maxBudget - player.getCurrentBudget());
            }
        }
    }

    /**
     * Function to perform a turn
     * Buy Hex (if invalid location skip its gonna skip this part) example row 0 , col 0 result in skip buyhex because hex 0,0 doesn't exist
     * Spawn Minion (if invalid location skip its gonna skip this part) example row 0 , col 0 result in skip spawnminion because hex 0,0 doesn't exist
     * typeIndex is the index of minion type player gonna spawn (start from 1)
     *
     * @param player    Player performing the turn
     * @param minionRow row of the minion you gonna spawn
     * @param minionCol col of the minion you gonna spawn
     * @param hexRow    row of the hex you gonna buy
     * @param hexCol    row of the hex you gonna buy
     * @param typeIndex
     */
    public void performTurn(Player player, int minionRow, int minionCol, int hexRow, int hexCol, int typeIndex) {
        int spawnMinionType = typeIndex - 1;
        if (!player.isBot()) {
            performHumanTurn(player, minionRow, minionCol, hexRow, hexCol, spawnMinionType);
        } else {
            performBotTurn(player);
        }

        executeMinionStrategy(player);

    }

    /**
     * perform turn for Human player
     *
     * @param player
     * @param minionRow
     * @param minionCol
     * @param hexRow
     * @param hexCol
     * @param typeIndex
     */
    private void performHumanTurn(Player player, int minionRow, int minionCol, int hexRow, int hexCol, int typeIndex) {
        player.printStatus();
        board.printBoard();
        Hex buyhexHex = board.getHex(hexRow, hexCol);
        Hex spawnminionHex = board.getHex(minionRow, minionCol);

        if (buyhexHex != null && player.canBuyHex(buyhexHex)) {
            player.buyHex(buyhexHex, config.hexPurchaseCost);
            player.printStatus();
        } else {
            System.out.println("Invalid hex purchase. Skipping buy hex");
        }

        if (spawnminionHex != null && spawnminionHex.getOwner() == player && !spawnminionHex.isOccupied()) {
            // Check if enough budget to spawn
            if (player.getCurrentBudget() >= config.spawnCost) {
                // Convert to zero-based index and spawn
                if (typeIndex >= 0 && typeIndex < minionTypes.size()) {
                    spawnMinion(player, spawnminionHex, typeIndex);
                    player.printStatus();
                } else {
                    System.out.println("Invalid minion type choice. Skipping spawn.");
                }
            } else {
                System.out.println("Not enough budget to spawn a minion. Skipping spawn.");
            }
        } else {
            System.out.println("Invalid spawn location. Skipping spawn.");
        }
    }

    //OLD do not call this
//    private void performTurn(Player player) {
//        if (!player.isBot()) {
//            performHumanTurn(player);
//        } else {
//            performBotTurn(player);
//        }
//
//        executeMinionStrategy(player);
//    }

    //    Old do not call this
//    private void performHumanTurn(Player player) {
//        player.printStatus();
//        board.printBoard();
//
//        // --- Hex Purchase ---
//        boolean validBuyInput = false;
//        while (!validBuyInput && currentTurn > 2) {
//            System.out.print("Do you want to buy a hex? (y/n): ");
//            String ans = scanner.nextLine().trim().toLowerCase();
//            if (ans.equals("y") || ans.equals("n")) {
//                validBuyInput = true;
//                if (ans.equals("y")) {
//                    boolean validHex = false;
//                    while (!validHex) {
//                        System.out.print("Enter row and column to buy (e.g., '2 3'): ");
//                        String line = scanner.nextLine().trim();
//                        String[] parts = line.split("\\s+");
//                        if (parts.length != 2) {
//                            System.out.println("Please enter exactly two numbers separated by space.");
//                            continue;
//                        }
//                        try {
//                            int row = Integer.parseInt(parts[0]);
//                            int col = Integer.parseInt(parts[1]);
//                            Hex hex = board.getHex(row, col);
//                            if (hex == null) {
//                                System.out.println("Coordinates out of bounds. Please try again.");
//                            } else if (hex.getOwner() != null) {
//                                System.out.println("This hex is already owned. Please choose another.");
//                            } else if (!player.canBuyHex(hex)) {
//                                System.out.println("You can only buy a hex adjacent to your owned hexes. Please try again.");
//                            } else {
//                                player.buyHex(hex, config.hexPurchaseCost);
//                                validHex = true;
//                                player.printStatus();
//                            }
//                        } catch (NumberFormatException e) {
//                            System.out.println("Invalid input. Please enter two integers.");
//                        }
//                    }
//                }
//            } else {
//                System.out.println("Invalid input. Please enter 'y' or 'n'.");
//            }
//        }
//
//        // --- Minion Spawn ---
//        // If it's the first turn, the player must spawn at least one minion.
//        boolean mustSpawn = (currentTurn <= 2);
//        boolean spawned = false; // track whether a spawn was successful
//        while (!spawned && (player.getSpawnsUsed() < config.maxSpawns)) {
//            boolean validSpawnInput = false;
//            // On first turn, force the answer to be "y"
//            if (mustSpawn) {
//                System.out.println("You must spawn a minion on your first turn.");
//                validSpawnInput = true;
//            } else {
//                while (!validSpawnInput) {
//                    System.out.print("Spawn a new minion? (y/n): ");
//                    String ans = scanner.nextLine().trim().toLowerCase();
//                    if (ans.equals("y") || ans.equals("n")) {
//                        validSpawnInput = true;
//                        if (ans.equals("n")) {
//                            return; // if not first turn, allow opting out
//                        }
//                    } else {
//                        System.out.println("Invalid input. Please enter 'y' or 'n'.");
//                    }
//                }
//            }
//
//            // Proceed with spawn (forced or chosen)
//            boolean validSpawnLocation = false;
//            while (!validSpawnLocation) {
//                System.out.print("Enter row and column for spawn (e.g., '2 3'): ");
//                String line = scanner.nextLine().trim();
//                String[] parts = line.split("\\s+");
//                if (parts.length != 2) {
//                    System.out.println("Please enter exactly two numbers separated by space.");
//                    continue;
//                }
//                try {
//                    int row = Integer.parseInt(parts[0]);
//                    int col = Integer.parseInt(parts[1]);
//                    Hex spawnHex = board.getHex(row, col);
//                    if (spawnHex == null) {
//                        System.out.println("Coordinates out of bounds. Please try again.");
//                    } else if (spawnHex.getOwner() != player) {
//                        System.out.println("You can only spawn on a hex that belongs to you. Please try again.");
//                    } else if (spawnHex.isOccupied()) {
//                        System.out.println("This hex is already occupied. Please choose another.");
//                    } else {
//                        // Check if enough budget to spawn
//                        if (player.getCurrentBudget() < config.spawnCost) {
//                            System.out.println("Not enough budget to spawn a minion.");
//                            // If first turn and cannot spawn, force re-prompt (or handle as a critical error)
//                            return;
//                        }
//                        // Let the user choose a minion type
//                        boolean validTypeChoice = false;
//                        int typeChoice = -1;
//                        while (!validTypeChoice) {
//                            System.out.println("Choose a minion type index:");
//                            for (int i = 0; i < minionTypes.size(); i++) {
//                                MinionType t = minionTypes.get(i);
//                                System.out.println((i + 1) + ". " + t.getName() + " (def=" + t.getDefenseFactor() + ")");
//                            }
//                            System.out.print("Enter a number between 1 and " + minionTypes.size() + ": ");
//                            String typeInput = scanner.nextLine().trim();
//                            try {
//                                typeChoice = Integer.parseInt(typeInput);
//                                if (typeChoice >= 1 && typeChoice <= minionTypes.size()) {
//                                    validTypeChoice = true;
//                                } else {
//                                    System.out.println("Invalid choice. Please try again.");
//                                }
//                            } catch (NumberFormatException e) {
//                                System.out.println("Invalid input. Please enter a number.");
//                            }
//                        }
//                        // Convert to zero-based index and spawn
//                        typeChoice--;
//                        spawnMinion(player, spawnHex, typeChoice);
//                        validSpawnLocation = true;
//                        spawned = true;
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("Invalid input. Please enter two integers.");
//                }
//            }
//            // If it's the first turn and spawn hasn't been successful, repeat the loop until a spawn occurs.
//            if (mustSpawn && !spawned) {
//                System.out.println("You must spawn at least one minion on your first turn.");
//            } else if (!mustSpawn) {
//                // For subsequent turns, if spawn was not performed (user answered "n"), then exit.
//                break;
//            }
//        }
//    }

    /**
     * Perform the bot's turn
     * Still using
     */
    private void performBotTurn(Player player) {
        System.out.println("Bot turn for Player " + player.getId());
        player.printStatus();
        board.printBoard();

        // 1) Attempt to buy a random adjacent hex if budget allows
        if (player.getCurrentBudget() >= config.hexPurchaseCost && currentTurn > 2) {
            // Collect all hexes that the bot can buy (adjacent to bought hexes, unowned)
            List<Hex> buyableHexes = new ArrayList<>();
            for (Hex owned : player.getBoughtHexes()) {
                for (Hex adj : owned.getAdjacentHexes()) {
                    if (adj.getOwner() == null) {
                        buyableHexes.add(adj);
                    }
                }
            }
            // If we have any buyable hexes, pick one at random and buy it
            if (!buyableHexes.isEmpty()) {
                Hex toBuy = buyableHexes.get((int) (Math.random() * buyableHexes.size()));
                player.buyHex(toBuy, config.hexPurchaseCost);
                player.printStatus();
            }
        }

        // 2) Attempt to spawn a random minion if we haven't reached maxSpawns
        if (player.getSpawnsUsed() < config.maxSpawns) {
            // Check if we have enough budget to spawn
            if (player.getCurrentBudget() >= config.spawnCost) {
                // Collect all owned hexes that are not occupied => potential spawn spots
                List<Hex> spawnableHexes = new ArrayList<>();
                for (Hex owned : player.getBoughtHexes()) {
                    if (!owned.isOccupied()) {
                        spawnableHexes.add(owned);
                    }
                }
                // If we have any spawnable hexes, choose one at random
                if (!spawnableHexes.isEmpty()) {
                    Hex spawnHere = spawnableHexes.get((int) (Math.random() * spawnableHexes.size()));
                    // Then pick a random minion type
                    if (!minionTypes.isEmpty()) {
                        int typeIndex = (int) (Math.random() * minionTypes.size());
                        spawnMinion(player, spawnHere, typeIndex);
                        player.printStatus();
                    }
                }
            }
        }
    }

    /**
     * Spawns a minion of the chosen type at the given hex.
     */
    private void spawnMinion(Player player, Hex hex, int typeIndex) {
        if (typeIndex < 0 || typeIndex >= minionTypes.size()) {
            System.out.println("Invalid minion type choice!");
            return;
        }
        if (player.getCurrentBudget() < config.spawnCost) {
            System.out.println("Not enough budget to spawn!");
            return;
        }
        if (currentTurn > 2) player.adjustBudget(-config.spawnCost);

        MinionType chosenType = minionTypes.get(typeIndex);
        int spawnOrder = player.getSpawnsUsed() + 1;
        Minion newMinion = new Minion((int) config.initHP,
                chosenType.getDefenseFactor(),
                hex,
                player,
                spawnOrder);
        // Set the strategy from the chosen type
        newMinion.setStrategy(chosenType.getStrategyAST());
        // Set the minion's type name.
        newMinion.setTypeName(chosenType.getName());

        player.addMinion(newMinion);
        player.incrementSpawnsUsed();
        System.out.println("Spawned minion " + spawnOrder + " of type " + chosenType.getName() +
                " at (" + hex.getRow() + "," + hex.getCol() + ")");
    }

    /**
     * Check if any player has no minions or if we reached max turns, etc.
     * This is a simplified version—adjust as per your final logic.
     */
    private boolean checkEndGame() {
        // Example: end if a player has 0 minions
        for (Player p : players) {
            if (p.getMinionsOwned().isEmpty() && currentTurn > 2) {
                System.out.println("Player " + p.getId() + " has no minions left! Game ends.");
                return true;
            }
        }
        if (currentTurn >= config.maxTurns) return true;
        return false;
    }

    public void determineWinner() {
        Player player1 = players.get(0);
        Player player2 = players.get(1);

        // Calculate statistics for each player.
        int minionCount1 = player1.getMinionsOwned().size();
        int minionCount2 = player2.getMinionsOwned().size();

        int totalHP1 = player1.getMinionsOwned().stream().mapToInt(minion -> minion.hp).sum();
        int totalHP2 = player2.getMinionsOwned().stream().mapToInt(minion -> minion.hp).sum();

        double budget1 = player1.getCurrentBudget();
        double budget2 = player2.getCurrentBudget();

        // Determine the winner based on the criteria.
        if (minionCount1 > minionCount2) {
            System.out.println("Player " + player1.getId() + " wins (more minions)!");
            winner = player1;
        } else if (minionCount2 > minionCount1) {
            System.out.println("Player " + player2.getId() + " wins (more minions)!");
            winner = player2;
        } else {
            // Same number of minions, check total HP.
            if (totalHP1 > totalHP2) {
                System.out.println("Player " + player1.getId() + " wins (greater total HP)!");
                winner = player1;
            } else if (totalHP2 > totalHP1) {
                System.out.println("Player " + player2.getId() + " wins (greater total HP)!");
                winner = player2;
            } else {
                // Same total HP, check remaining budget.
                if (budget1 > budget2) {
                    System.out.println("Player " + player1.getId() + " wins (more remaining budget)!");
                    winner = player1;
                } else if (budget2 > budget1) {
                    System.out.println("Player " + player2.getId() + " wins (more remaining budget)!");
                    winner = player2;
                } else {
                    // All equal.
                    System.out.println("The game is a tie!");
                    winner = new Player(0, 0, false);
                }
            }
        }
    }

    // get winner for end screen
    public Player getWinner() {
        return winner;
    }

    private void endGameSummary() {
        System.out.println("\nGame Over! Final results:");
        System.out.println("Winner is Player " + getWinner().getId());

        for (Player p : players) {
            int totalHP = 0;
            for (Minion m : p.getMinionsOwned()) {
                totalHP += m.hp;
            }
            System.out.println("Player " + p.getId() +
                    " => minions: " + p.getMinionsOwned().size() +
                    ", total HP: " + totalHP +
                    ", budget: " + p.budget());
        }
    }

    // === Implementation of GameCommand interface ===
    @Override
    public Map<String, Long> identifierpack() {
        // Return the global variable environment (for AST variable references)
        return variableEnv;
    }

    @Override
    public boolean move(Direction direction) {
        if (currentMinion != null) {
            double budgetBefore = currentMinion.getOwner().getCurrentBudget();
            if (budgetBefore < 1) {
                // no budget => end
                System.out.println("Not enough budget to move => end strategy");
                return false;
            }
            currentMinion.move(direction);
            // According to spec, after move is attempted, we still continue unless budget < 1
            // or we specifically decide to end. We'll return true if we want to keep going.
            return true;
        }
        return false;
    }

    @Override
    public boolean shoot(Direction direction, long expenditure) {
        if (currentMinion != null) {
            double budgetBefore = currentMinion.getOwner().getCurrentBudget();
            if (budgetBefore < (expenditure + 1)) {
                System.out.println("Not enough budget to shoot => end strategy");
                return false;
            }
            currentMinion.shoot(direction, (int) expenditure);
            // We continue after shooting, unless you want to end
            return false;
        }
        return false;
    }

    @Override
    public boolean done() {
        System.out.println("Minion ended its turn with 'done'.");
        // Return false => stop evaluating further strategy statements this turn
        return false;
    }

    @Override
    public long getAllyInfo() {
        if (currentMinion == null) return 0;
        return currentMinion.ally();
    }

    @Override
    public long getOpponentInfo() {
        if (currentMinion == null) return 0;
        return currentMinion.opponent();
    }

    @Override
    public long getNearbyInfo(Direction direction) {
        if (currentMinion == null) return 0;
        return currentMinion.nearby(direction);
    }

    @Override
    public int takeBudget() {
        if (currentMinion != null) {
            return currentMinion.getOwner().budget();
        }
        return 0;
    }

    @Override
    public long getRandom() {
        Random rand = new Random();
        return rand.nextInt(1000);
    }

    public void setCurrentMinion(Minion m) {
        this.currentMinion = m;
    }

    @Override
    public Minion getCurrentMinion() {
        return currentMinion;
    }

    @Override
    public int getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public ConfigLoader getConfig() {
        return config;
    }
}
