package com.example.kombat.service;

import com.example.kombat.backend.GameState.Game;
import com.example.kombat.backend.GameState.Minion;
import com.example.kombat.backend.GameState.MinionType;
import com.example.kombat.backend.GameState.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameStateService {

    private final SimpMessagingTemplate messagingTemplate;


    @Autowired
    public GameStateService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Method to fetch player 1's owned hexes
    public List<String> getPlayer1OwnedHexes() {
        return Game.getInstance().getPlayerOwnedHexes(1);
    }

    // Method to fetch player 2's owned hexes
    public List<String> getPlayer2OwnedHexes() {
        return Game.getInstance().getPlayerOwnedHexes(2);
    }

    // Method to fetch player 1's budget
    public int getPlayer1Budget() {
        Game.getInstance().applyBudgetAndInterest(Game.getInstance().getPlayers().get(0));
        System.out.println("butget to show " + Game.getInstance().getPlayerOwnBudget(1));
        return Game.getInstance().getPlayerOwnBudget(1);
    }

    // Method to fetch player 2's budget
    public int getPlayer2Budget() {
        Game.getInstance().applyBudgetAndInterest(Game.getInstance().getPlayers().get(1));
        System.out.println("buget to show " + Game.getInstance().getPlayerOwnBudget(2));
        return Game.getInstance().getPlayerOwnBudget(2);
    }

    // Method to fetch current turn
    public int getCurrentTurn() {
        System.out.println("cur"+Game.getInstance().getCurrentTurn());
        return Game.getInstance().getCurrentTurn();
    }
    // Method to show minion in game
    public int getMinionType(){
        return Game.getInstance().getminiontype();
    }
    // Method to upqdate
    public void performturnPlayer1(int minionrow, int minioncol, int hexrow, int hexcol, int typeindex) {
        Game.getInstance().performTurn(Game.getInstance().getPlayers().get(0), minionrow, minioncol, hexrow, hexcol, typeindex);
        Game.getInstance().endturn();
    }

    public void performturnPlayer2(int minionrow, int minioncol, int hexrow, int hexcol, int typeindex) {
        Game.getInstance().performTurn(Game.getInstance().getPlayers().get(1), minionrow, minioncol, hexrow, hexcol, typeindex);
        Game.getInstance().endturn();
    }
    /* example

    [MyNameisOne, MyNameisTwo, MynameisPoon, 4, Why does that guy name just "4" ???]*/
    public List<String> getAllMinionTypesName() {
        List<String> l = new ArrayList<>();
        for (MinionType mt : Game.getInstance().minionTypes) {
            l.add(mt.getName());}
        return l;
    }

    public List<Integer> getAllMinionTypesDefenseFactor() {
        List<Integer> l = new ArrayList<>();
        for (MinionType mt : Game.getInstance().minionTypes) {
            l.add(mt.getDefenseFactor());
        }
        return l;
    }
//    public List<Minion> getMinionPlayer1() {
//        System.out.println("getMinionPlayer1"+Game.getInstance().getMinions(1));
//        return Game.getInstance().getMinions(1);
//    }
//    public List<Minion> getMinionPlayer2() {
//        return Game.getInstance().getMinions(2);
//    }


}