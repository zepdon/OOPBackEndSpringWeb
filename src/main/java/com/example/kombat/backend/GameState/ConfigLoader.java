package com.example.kombat.backend.GameState;

import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class ConfigLoader {
    private static ConfigLoader instance;
    private Properties properties;
    private Map<String, Long> configValues; // Stores all properties as long values

    // Predefined configuration fields:
    public final long spawnCost;
    public final long hexPurchaseCost;
    public final long initBudget;
    public final long initHP;
    public final long turnBudget;
    public final long maxBudget;
    public final long interestPct;
    public final long maxTurns;
    public final long maxSpawns;

    // Private constructor loads from file and parses all properties
    public ConfigLoader(String configFilePath) throws IOException {
        properties = new Properties();
        try (FileInputStream in = new FileInputStream(configFilePath)) {
            properties.load(in);
        }

        // Initialize the map to hold all configuration values as longs.
        configValues = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            try {
                long value = Long.parseLong(properties.getProperty(key).trim());
                configValues.put(key, value);
            } catch (NumberFormatException e) {
                System.err.println("Warning: Could not parse property '" + key + "' with value '"
                        + properties.getProperty(key) + "' as long.");
            }
        }

        // Initialize the predefined fields using helper getLong() method.
        spawnCost       = getLong("spawn_cost", 0);
        hexPurchaseCost = getLong("hex_purchase_cost", 0);
        initBudget      = getLong("init_budget", 0);
        initHP          = getLong("init_hp", 0);
        turnBudget      = getLong("turn_budget", 0);
        maxBudget       = getLong("max_budget", 0);
        interestPct     = getLong("interest_pct", 0);
        maxTurns        = getLong("max_turns", 0);
        maxSpawns       = getLong("max_spawns", 0);
    }

    // Singleton instance accessor
    public static ConfigLoader getInstance(String configFilePath) throws IOException {
        if (instance == null) {
            instance = new ConfigLoader(configFilePath);
        }
        return instance;
    }

    /**
     * Returns the long value for the specified key, or the defaultValue if not present.
     */
    public long getLong(String key, long defaultValue) {
        return configValues.getOrDefault(key, defaultValue);
    }

    /**
     * Returns the long value for the specified key.
     * Throws IllegalArgumentException if the key is not found.
     */
    // System.out.println(config.getInstance("config.txt").getLong("shark_cost"));
    public long getLong(String key) {
        if (!configValues.containsKey(key)) {
            throw new IllegalArgumentException("Key not found in configuration: " + key);
        }
        return configValues.get(key);
    }

    /**
     * Returns an unmodifiable view of all configuration properties.
     */
    public Map<String, Long> getAllProperties() {
        return Collections.unmodifiableMap(configValues);
    }
}
