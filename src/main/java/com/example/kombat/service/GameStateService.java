package com.example.kombat.service;

import com.example.kombat.backend.GameState.*;
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
        System.out.println("Player 1 budget to show " + Game.getInstance().getPlayerOwnBudget(1));
        return Game.getInstance().getPlayerOwnBudget(1);
    }

    // Method to fetch player 2's budget
    public int getPlayer2Budget() {
        Game.getInstance().applyBudgetAndInterest(Game.getInstance().getPlayers().get(1));
        System.out.println("Player 2 budget to show " + Game.getInstance().getPlayerOwnBudget(2));
        return Game.getInstance().getPlayerOwnBudget(2);
    }

    // Method to fetch current turn
    public int getCurrentTurn() {
        System.out.println("current turn : "+Game.getInstance().getCurrentTurn());
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
    /*
     * [[1, 1, 100, 1, /image/Minion/minion1.png, blue], [1, 2, 100, 3, /image/Minion/minion3.png, blue], [2, 2, 100, 5, /image/Minion/minion5.png, blue], [7, 6, 100, 3, /image/Minion/minion3.png, red], [6, 8, 100, 5, /image/Minion/minion5.png, red]]
     */
    public List<List<String>> getAllMinionLocationAndType() {
        List<List<String>> result = new ArrayList<>();
        Game game = Game.getInstance();

        // Iterate over all players
        for (Player player : game.getPlayers()) {
            // Iterate over all minions owned by the current player
            for (Minion m : player.getMinionsOwned()) {
                Hex currentHex = m.getCurrentHex();

                // Extract individual attributes
                int row = currentHex.getRow();
                int col = currentHex.getCol();
                int typeNumber = m.getTypeNumberPlusOne();
                int hp = m.hp;
                int defenseFactor = m.getDefenseFactor();
                String src = m.getSrc(); // Get the src attribute

                // Determine the owner's color
                String owner;
                if (player.getId() == 1) {
                    owner = "blue";
                } else {
                    owner = "red";
                }

                // Convert all attributes to strings
                String rowStr = String.valueOf(row);
                String colStr = String.valueOf(col);
                String typeNumberStr = String.valueOf(typeNumber);
                String hpStr = String.valueOf(hp);
                String defenseFactorStr = String.valueOf(defenseFactor);

                // Create a list for the current minion's attributes
                List<String> minionInfo = new ArrayList<>();
                minionInfo.add(rowStr);          // Row as String
                minionInfo.add(colStr);          // Column as String
                minionInfo.add(typeNumberStr);   // Type Number as String
                minionInfo.add(hpStr);           // HP as String
                minionInfo.add(defenseFactorStr); // Defense Factor as String
                minionInfo.add(src);             // Src as String
                minionInfo.add(owner);           // Owner as String ("blue" or "red")

                // Add the minion's info to the result list
                result.add(minionInfo);
            }
        }
        return result;
    }
    public int getGameMode(){
        return Game.getInstance().getGameMode();
    }
    public int Player1spawnRemaining(){
        System.out.println("Player 1 spawn avalible :" + Game.getInstance().getPlayers().get(0).getSpawnAvalible());
        return Game.getInstance().getPlayers().get(0).getSpawnAvalible();
    }
    public int Player2spawnRemaining(){
        System.out.println("Player 2 spawn avalible : " + Game.getInstance().getPlayers().get(1).getSpawnAvalible());
        return Game.getInstance().getPlayers().get(1).getSpawnAvalible();
    }


}