package fr.cotedazur.univ.polytech.startingpoint;

public class ActionManager {
    public static int collectGold(Player player) {
        // King
        if (player.getGameCharacter() != null && player.getCharacterName().equals("Roi")) {
            int addenGold = 0;
            // You receive one gold for each noble (yellow) district in your city.
            for (District district : player.getDistrictsBuilt()) {
                if (district.color() == DistrictColor.noble) {
                    addenGold++;
                }
            }
            player.addGold(addenGold);
            if (addenGold != 0) {
                System.out
                        .println("Le " + player.getCharacterName() + " a donné " + addenGold + " or a " + player.getName());
            }
            return addenGold;
        }
        return 0;
    }

    public static void applySpecialEffect(Player player, Game game) {
        player.getGameCharacter().specialEffect(player, game);
    }
}
