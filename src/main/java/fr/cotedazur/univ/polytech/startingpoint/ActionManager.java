package fr.cotedazur.univ.polytech.startingpoint;

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
            player.gold += addenGold;
            if (addenGold != 0) {
                System.out
                        .println("Le " + player.getCharactersName() + " a donné " + addenGold + " or a " + player.name);
            }
            return addenGold;
        }
        return 0;
    }

    public static void applySpecialEffect(Player player, Game game) {
        player.getCharacter().specialEffect(player, game);
    }
}
