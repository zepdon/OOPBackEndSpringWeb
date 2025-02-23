package com.example.kombat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ModeController {

    // Endpoint to set the game mode
    @PostMapping("/setMode")
    public ResponseEntity<String> setMode(@RequestBody Map<String, String> payload) {
        String mode = payload.get("mode");
        // Here you would typically validate the mode and update your game configuration.
        // For example, you might convert the string into your GameMode enum.
        System.out.println("Received mode: " + mode);

        // In a real application, store the mode (e.g., in a service or session) for later use.
        // For now, simply return a confirmation.
        return ResponseEntity.ok("Mode set to: " + mode);
    }
}
