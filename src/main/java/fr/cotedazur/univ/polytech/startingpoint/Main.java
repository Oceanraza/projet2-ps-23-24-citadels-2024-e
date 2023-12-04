package fr.cotedazur.univ.polytech.startingpoint;

public class Main {
    //if a player has 8 districts built, he wins
    public static boolean isFinished(Player player) {
        return player.getDistrictsBuilt().length == 8;
    }

    public static void main(String... args) {
        isFinished(new Player() {
        });

    }

}
