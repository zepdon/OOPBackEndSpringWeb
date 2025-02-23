package com.example.kombat.controller;

import com.example.kombat.backend.GameState.ConfigLoader;
import com.example.kombat.backend.GameState.GameMode;
import com.example.kombat.backend.GameState.MinionType;
import com.example.kombat.backend.parser.GameParser;
import com.example.kombat.backend.parser.GameTokenizer;
import com.example.kombat.service.GameSettings;
import com.example.kombat.backend.AST.BlockStatementNode;
import com.example.kombat.backend.AST.Node;
import com.example.kombat.backend.Error.SyntaxError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MinionTypeController {

    private final GameSettings gameSettings;
    private final ConfigLoader configLoader;

    public MinionTypeController(GameSettings gameSettings, ConfigLoader configLoader) {
        this.gameSettings = gameSettings;
        this.configLoader = configLoader;
    }

    @PostMapping("/saveMinionType")
    public ResponseEntity<?> saveMinionType(@RequestBody MinionTypeRequest request) {
        try {
            // Attempt to parse the strategy script to verify its correctness.
            GameTokenizer tokenizer = new GameTokenizer(request.getStrategy());
            GameParser parser = new GameParser(tokenizer);
            // Parse the script into a list of StateNodes.
            // This may throw a SyntaxError if the script is invalid.
            java.util.List<Node.StateNode> statements = parser.parse();
            // Wrap the statements into a BlockStatementNode.
            Node.StateNode strategyAST = new BlockStatementNode(statements);

            // Optionally, create a new MinionType instance using the parsed strategy.
            MinionType newType = new MinionType(request.getName(),
                    Integer.parseInt(request.getDefenseFactor()),
                    strategyAST);
            // TODO: Save or process the new minion type as needed (e.g., add to a list or persist in a database).

            return ResponseEntity.ok("Minion type saved successfully");
        } catch (SyntaxError e) {
            return ResponseEntity.badRequest().body("Syntax error in strategy: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing minion type: " + e.getMessage());
        }
    }

    // DTO class for receiving the minion type data from the frontend.
    public static class MinionTypeRequest {
        private String name;
        private String defenseFactor;
        private String strategy;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getDefenseFactor() {
            return defenseFactor;
        }
        public void setDefenseFactor(String defenseFactor) {
            this.defenseFactor = defenseFactor;
        }
        public String getStrategy() {
            return strategy;
        }
        public void setStrategy(String strategy) {
            this.strategy = strategy;
        }
    }
}
