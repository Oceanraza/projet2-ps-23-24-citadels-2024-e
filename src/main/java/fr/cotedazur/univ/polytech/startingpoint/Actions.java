package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.players.Player;

public class ActionManager {
    public static int updateGold(Player player) {
        // King
        if (player.getCharactersName().equals("Roi")) {
            int addenGold = 0;
            // You receive one gold for each noble (yellow) district in your city.
            for (District district : player.getDistrictsBuilt()) {
                if (district.getColor() == DistrictColor.noble) {
                    addenGold++;
                }
            }
            player.setGold(player.getGold() + addenGold);
            if (addenGold != 0) {
                System.out
                        .println("Le " + player.getCharactersName() + " a donné " + addenGold + " or a " + player.getName());
            }
            return addenGold;
        }
        return 0;
    }

    public static void applySpecialEffect(Player player, Game game) {
        player.getCharacter().specialEffect(player, game);
    }
}
