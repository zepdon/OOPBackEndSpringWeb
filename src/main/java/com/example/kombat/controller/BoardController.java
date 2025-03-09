// src/main/java/com/example/kombat/controller/BoardController.java
package com.example.kombat.controller;

import com.example.kombat.websocket.BoardWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardWebSocketHandler boardWebSocketHandler;

    @Autowired
    public BoardController(BoardWebSocketHandler boardWebSocketHandler) {
        this.boardWebSocketHandler = boardWebSocketHandler;
    }

    @PostMapping("/buyHex")
    public ResponseEntity<String> buyHex(@RequestBody BuyHexRequest request) {
        // Log the incoming request for debugging.
        System.out.println("Received BuyHex request: Row=" + request.getRow() +
                ", Col=" + request.getCol() +
                ", Player=" + request.getPlayer());

        // TODO: Process the purchase, update your game state, deduct funds, etc.

        // Prepare a JSON message with the update details.
        String updateMessage = String.format("{\"row\": %d, \"col\": %d, \"player\": \"%s\"}",
                request.getRow(), request.getCol(), request.getPlayer());

        // Broadcast the update over WebSocket.
        boardWebSocketHandler.broadcast(updateMessage);

        return ResponseEntity.ok("Hex purchased successfully at (" + request.getRow() + ", " + request.getCol() + ")");
    }

    // DTO to capture incoming request data.
    public static class BuyHexRequest {
        private int row;
        private int col;
        private String player;

        public int getRow() { return row; }
        public void setRow(int row) { this.row = row; }
        public int getCol() { return col; }
        public void setCol(int col) { this.col = col; }
        public String getPlayer() { return player; }
        public void setPlayer(String player) { this.player = player; }
    }
}
