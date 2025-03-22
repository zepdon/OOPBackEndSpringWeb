package com.example.kombat.backend.GameState;

public class KOMBATMain {
    public static void main(String[] args) {
        GameLauncher.launch();
//        //what to do in one turn
//        // this is example for turn 1

        Game.getInstance().performTurn(Game.getInstance().getPlayers().get(0) , 1 ,1 ,0 , 0 , 1);
        Game.getInstance().endturn();
        Game.getInstance().applyBudgetAndInterest(Game.getInstance().getPlayers().get(0));
        System.out.println(Game.getInstance().getPlayerOwnBudget(1));
        System.out.println(Game.getInstance().getPlayers().get(0).getOwnedHexes());
//
//        //turn 2
        Game.getInstance().applyBudgetAndInterest(Game.getInstance().getPlayers().get(1));
        Game.getInstance().performTurn(Game.getInstance().getPlayers().get(1) , 8 ,8 ,0 , 0 , 2);
        Game.getInstance().endturn();
        System.out.println(Game.getInstance().getPlayerOwnBudget(2));
        System.out.println(Game.getInstance().getPlayers().get(1).getOwnedHexes());
//
//        //turn 3
        Game.getInstance().applyBudgetAndInterest(Game.getInstance().getPlayers().get(0));
        Game.getInstance().performTurn(Game.getInstance().getPlayers().get(0) , 2 ,2 ,2 , 4 , 1);
        Game.getInstance().endturn();
        System.out.println(Game.getInstance().getPlayerOwnBudget(1));
        System.out.println(Game.getInstance().getPlayers().get(0).getOwnedHexes());
//
//        //turn 4
        Game.getInstance().applyBudgetAndInterest(Game.getInstance().getPlayers().get(1));
        Game.getInstance().performTurn(Game.getInstance().getPlayers().get(1) , 8 ,6 ,6 , 7 , 2);
        Game.getInstance().endturn();
        System.out.println(Game.getInstance().getPlayerOwnBudget(2));
        System.out.println(Game.getInstance().getPlayers().get(1).getOwnedHexes());
//
//        //turn 5
        Game.getInstance().applyBudgetAndInterest(Game.getInstance().getPlayers().get(0));
        Game.getInstance().performTurn(Game.getInstance().getPlayers().get(0) , 2 ,2 ,3 , 1 , 1);
        Game.getInstance().endturn();
        System.out.println(Game.getInstance().getPlayerOwnBudget(1));
        System.out.println(Game.getInstance().getPlayers().get(0).getOwnedHexes());
//
//
        Game.getInstance().getBoard().printBoard();
    }
}


