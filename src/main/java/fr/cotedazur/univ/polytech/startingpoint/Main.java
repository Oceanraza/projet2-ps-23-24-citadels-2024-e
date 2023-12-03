package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;

public class Main {
    //if a player has 8 districts built, he wins
    public static boolean isFinished(Player player) {
        return player.getDistrictsBuilt().size() == 8;
    }

    // Manque si le jouer est le premier à avoir fini +4 et bonus des cartes merveilles
    public static Player isWinner(List<Player> players) {
        int maxScore = 0;
        Player winner = players.get(0);
        for(Player player : players) {
            int score = player.gold;
            ArrayList<DistrictColor> districtColors = new ArrayList<>();
            for (District district : player.districtsBuilt) {
                score += district.getPrice();
                districtColors.add(district.getColor());
            }
            if (districtColors.size() == DistrictColor.values().length) {
                score += 3;
            }
            if (isFinished(player)) {
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
        isFinished(new Player() {
        });

    }

}
