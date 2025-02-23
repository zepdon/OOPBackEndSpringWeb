// src/main/java/com/example/kombat/controller/GameController.java
package com.example.kombat.controller;

import com.example.kombat.backend.GameState.Game;
import com.example.kombat.backend.GameState.GameMode;
import com.example.kombat.backend.GameState.MinionType;
import com.example.kombat.backend.GameState.ConfigLoader;
import com.example.kombat.backend.parser.GameParser;
import com.example.kombat.backend.parser.GameTokenizer;
import com.example.kombat.backend.AST.BlockStatementNode;
import com.example.kombat.backend.AST.Node;
import com.example.kombat.backend.Error.SyntaxError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private ConfigLoader configLoader; // Ensure ConfigLoader is a Spring bean

    @PostMapping("/launchGame")
    public ResponseEntity<String> launchGame(@RequestBody LaunchGameRequest request) {
        if (request.getMode() == null) {
            return ResponseEntity.badRequest().body("Mode is missing in the payload.");
        }
        try {
            GameMode mode = GameMode.valueOf(request.getMode().toUpperCase());

            // Convert incoming DTOs to MinionType objects by parsing the strategy.
            List<MinionType> minionTypes = request.getMinionTypes().stream()
                    .map(mt -> {
                        Node.StateNode strategyAST;
                        try {
                            GameTokenizer tokenizer = new GameTokenizer(mt.getStrategy());
                            GameParser parser = new GameParser(tokenizer);
                            List<Node.StateNode> statements = parser.parse();
                            strategyAST = new BlockStatementNode(statements);
                        } catch (SyntaxError e) {
                            throw new RuntimeException("Invalid strategy for minion " + mt.getName() + ": " + e.getMessage(), e);
                        }
                        return new MinionType(mt.getName(), mt.getDefenseFactor(), strategyAST);
                    })
                    .collect(Collectors.toList());

            // Launch the game in a new thread.
            new Thread(() -> {
                Game.initializeGame(configLoader, mode);
                Game.getInstance().setMinionTypes(minionTypes);
                Game.getInstance().start();
            }).start();

            return ResponseEntity.ok("Game launched with mode: " + mode);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid mode: " + request.getMode());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public static class LaunchGameRequest {
        private String mode;
        private List<MinionTypeDTO> minionTypes;

        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }
        public List<MinionTypeDTO> getMinionTypes() { return minionTypes; }
        public void setMinionTypes(List<MinionTypeDTO> minionTypes) { this.minionTypes = minionTypes; }
    }

    public static class MinionTypeDTO {
        private String name;
        private int defenseFactor;
        private String strategy; // The raw strategy text from the frontend.

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getDefenseFactor() { return defenseFactor; }
        public void setDefenseFactor(int defenseFactor) { this.defenseFactor = defenseFactor; }
        public String getStrategy() { return strategy; }
        public void setStrategy(String strategy) { this.strategy = strategy; }
    }
}
