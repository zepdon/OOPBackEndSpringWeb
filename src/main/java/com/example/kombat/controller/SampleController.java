package com.example.kombat.controller;

import com.example.kombat.backend.GameState.ConfigLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    private final ConfigLoader configLoader;

    public SampleController(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @GetMapping("/config")
    public String getConfigInfo() {
        return "Spawn Cost is: " + configLoader.getLong("spawn_cost", 0);
    }
}
