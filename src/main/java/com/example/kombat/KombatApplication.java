package com.example.kombat;

import com.example.kombat.backend.GameState.GameLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KombatApplication {

    public static void main(String[] args) {
        SpringApplication.run(KombatApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner runGameLauncher() {
//        return args -> {
//            // Optionally run it in a new thread if GameLauncher.launch() is blocking.
//            new Thread(() -> {
//                GameLauncher.launch();
//            }).start();
//        };
//    }

}
