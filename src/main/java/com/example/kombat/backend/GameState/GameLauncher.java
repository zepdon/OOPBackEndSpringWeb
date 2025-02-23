package com.example.kombat.backend.GameState;

//import AST.BlockStatementNode;
//import AST.Node;
//import Error.SyntaxError;
//import parser.GameParser;
//import parser.GameTokenizer;

import com.example.kombat.backend.AST.BlockStatementNode;
import com.example.kombat.backend.AST.Node;
import com.example.kombat.backend.Error.SyntaxError;
import com.example.kombat.backend.parser.GameParser;
import com.example.kombat.backend.parser.GameTokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The main entry point for KOMBAT.
 * 1. How many minion types they wish to include.
 * 2. Define name defencefactor and strategy
 */
public class GameLauncher {

    public static void launch() {
        try {
            // 1) Load configuration
            String configFilePath = "config.txt";
            ConfigLoader config = ConfigLoader.getInstance(configFilePath);
//            System.out.println(config.getAllProperties());

            // 2) Choose game mode
            Scanner scanner = new Scanner(System.in);
            GameMode gameMode = chooseGameMode(scanner);

            // 4) Ask the player how many types they wish to include (from 1 to 5)
            int numChosen = 0;
            while (true) {
                System.out.print("How many minion types do you want to have in the game (1–5)?: ");
                String numInput = scanner.nextLine().trim();
                try {
                    numChosen = Integer.parseInt(numInput);
                    if (numChosen >= 1 && numChosen <= 5) {
                        break;
                    } else {
                        System.out.println("Invalid number. Please enter a number between 1 and 5.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter an integer between 1 and 5.");
                }
            }

            // 4) For each minion type, prompt for name, defense, and script.
            List<MinionType> minionTypes = new ArrayList<>();
            for (int i = 1; i <= numChosen; i++) {
                System.out.println("\n--- Defining Minion Type #" + i + " ---");

                // Custom name.
                System.out.print("Enter the name for minion type #" + i + ": ");
                String name = scanner.nextLine().trim();
                while(name.isEmpty()){
                    System.out.print("Name cannot be empty. Enter the name for minion type #" + i + ": ");
                    name = scanner.nextLine().trim();
                }

                // Defense factor.
                int defenseFactor = 0;
                while (true) {
                    System.out.print("Enter the defense factor (integer >= 0) for " + name + ": ");
                    String defInput = scanner.nextLine().trim();
                    try {
                        defenseFactor = Integer.parseInt(defInput);
                        if (defenseFactor < 0) {
                            System.out.println("Defense factor must be non-negative.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter an integer.");
                    }
                }

                // Strategy script input with error checking.
                Node.StateNode strategyAST = null;
                boolean scriptParsed = false;
                while (!scriptParsed) {
                    System.out.println("Enter the strategy script for " + name + ":");
                    System.out.println("Type your script, ending with a line that contains only 'END'.");
                    String script = readMultilineScript(scanner);
                    try {
                        strategyAST = parseScript(script);
                        scriptParsed = true;
                    } catch (SyntaxError e) {
                        System.out.println("Syntax error in your script: " + e.getMessage());
                        System.out.println("Please re-enter your strategy script.");
                    }
                }

                // Create the minion type.
                MinionType mtype = new MinionType(name, defenseFactor, strategyAST);
                minionTypes.add(mtype);
            }


            // 7) Initialize and configure the game.
            Game.initializeGame(config, gameMode);
            Game game = Game.getInstance();
            // Set the chosen minion types into the game.
            game.setMinionTypes(minionTypes);

            // 8) Start the game.
            game.start();

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a multi-line script from the scanner until a line containing only "END" is encountered.
     * Returns the concatenated script as a single string.
     */
    private static String readMultilineScript(Scanner scanner) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.trim().equalsIgnoreCase("END")) {
                break;
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    /**
     * Helper method to prompt user for DUEL, SOLITAIRE, or AUTO mode.
     */
    private static GameMode chooseGameMode(Scanner scanner) {
        while (true) {
            System.out.println("Choose game mode:");
            System.out.println("1. DUEL (Player vs Player)");
            System.out.println("2. SOLITAIRE (Player vs Bot)");
            System.out.println("3. AUTO (Bot vs Bot)");
            System.out.print("Enter your choice (1–3): ");
            String modeInput = scanner.nextLine().trim();
            try {
                int modeChoice = Integer.parseInt(modeInput);
                switch (modeChoice) {
                    case 1: return GameMode.DUEL;
                    case 2: return GameMode.SOLITAIRE;
                    case 3: return GameMode.AUTO;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
            }
        }
    }

    /**
     * Parses a strategy script string into a single StateNode (BlockStatementNode).
     */
    private static Node.StateNode parseScript(String script) {
        GameTokenizer tokenizer = new GameTokenizer(script);
        GameParser parser = new GameParser(tokenizer);
        List<Node.StateNode> statements = parser.parse();

        // Wrap them in a BlockStatementNode so the entire script is one node
        return new BlockStatementNode(statements);
    }
}
