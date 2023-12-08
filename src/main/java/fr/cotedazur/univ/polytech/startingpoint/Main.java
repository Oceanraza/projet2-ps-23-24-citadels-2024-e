package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static final Random random = new Random();

    // If a player has 8 districts built, he wins
    public static boolean isFinished(Player player) {
        return player.getDistrictsBuilt().size() == 8;
    }

    public static Player isWinner(List<Player> players, Player firstBuilder) {
        int maxScore = 0;
        Player winner = players.get(0);
        for(Player player : players) {
            int score = player.gold;
            ArrayList<DistrictColor> districtColors = new ArrayList<>();
            for (District district : player.getDistrictsBuilt()) {
                score += district.getPrice();
                districtColors.add(district.getColor());
            }
            if (districtColors.size() == DistrictColor.values().length) {
                score += 3;
            }
            if (player == firstBuilder) {
                score += 4;
            } else if (isFinished(player)) {
                score += 2;
            }
            if (score > maxScore) {
                maxScore = score;
                winner = player;
            }
        }
        return winner;
    }

    public static void main(String... args) {
        Game newGame = new Game();
        System.out.println(newGame);
        isFinished(new Player() {
        });
    }

}
