package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;

public class Main {
    //if a player has 8 districts built, he wins

    public static boolean isFinished(Player player) {
        return player.getDistrictsBuilt().length == 8;
    }

    public static void main(String... args) {
        Game newGame = new Game();
        System.out.println(newGame);
        isFinished(new Player() {
        });

    }

}
