package com.example.kombat.config;

import com.example.kombat.backend.GameState.ConfigLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;

@Configuration
public class ConfigLoaderConfig {

    @Bean
    public ConfigLoader configLoader() throws IOException {
        // Use your existing constructor with the config file path.
        return new ConfigLoader("config.txt");
    }
}
