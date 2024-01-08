package fr.cotedazur.univ.polytech.startingpoint;

public class ActionManager {
<<<<<<< HEAD
    public static int printGold(Player player, int addenGold){
        player.addGold(addenGold);
        if (addenGold !=0){System.out.println((player.getCharacterName().equals("Eveque")? "L'" :"Le ") + player.getCharacterName() + " a donné " + addenGold + " or a " + player.getName());}
        return addenGold;
    }
    public static int collectGold(Player player) {
        int addenGold = 0;
        switch (player.getCharacterName()){
            case("Roi"):
                addenGold = player.getNumberOfDistrictsByColor().get(DistrictColor.noble);
                return printGold(player,addenGold);
            case("Eveque"):
                addenGold = player.getNumberOfDistrictsByColor().get(DistrictColor.religieux);
                return printGold(player,addenGold);
            case("Condottiere"):
                addenGold = player.getNumberOfDistrictsByColor().get(DistrictColor.militaire);
                return printGold(player,addenGold);
            case("Marchand"):
                addenGold = player.getNumberOfDistrictsByColor().get(DistrictColor.marchand);
                return printGold(player,addenGold);
            default:
                return 0;
=======
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
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c
        }
        return 0;
    }

    public static void applySpecialEffect(Player player, Game game) {
        player.getCharacter().specialEffect(player, game);
    }
}
