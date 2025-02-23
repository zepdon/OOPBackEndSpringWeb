//package com.example.kombat.service;
//
//import com.example.kombat.model.Game;
//import com.example.kombat.model.GameMode;
//import com.example.kombat.model.GameStateEnum;
//import com.example.kombat.model.MinionType;
//import com.example.kombat.model.Player;
//import com.example.kombat.GameState.ConfigLoader;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class GameService {
//
//    public Game startGame(Map<String, Object> config) throws IOException {
//        ConfigLoader configLoader = ConfigLoader.getInstance("config.txt");
//        GameMode gameMode = GameMode.valueOf((String) config.get("gameMode"));
//        Game.initializeGame(configLoader, gameMode);
//        return Game.getInstance();
//    }
//
//    public boolean moveMinion(int playerId, int minionId, String direction) {
//        return Game.getInstance().move(playerId, minionId, direction);
//    }
//
//    public boolean shootMinion(int playerId, int minionId, String direction, long expenditure) {
//        return Game.getInstance().shoot(playerId, minionId, direction, expenditure);
//    }
//
//    public boolean doneMinion(int playerId, int minionId) {
//        return Game.getInstance().done(playerId, minionId);
//    }
//
//    // Other game-related methods...
//}