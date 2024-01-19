package fr.cotedazur.univ.polytech.startingpoint;


import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public class ActionManager {
    public static int printGold(Player player, int addenGold){
        player.addGold(addenGold);
        if (addenGold !=0){System.out.println((player.getCharacterName().equals("Eveque")? "L'" :"Le ") + player.getCharacterName() + " a donné " + addenGold + " or a " + player.getName());}
        return addenGold;
    }
    public static int collectGold(Player player) {
        int addenGold = 0;
        boolean hasMagicSchool = player.getCity().containsDistrict("Ecole de magie");
        switch (player.getCharacterName()) {
            case ("Roi"):
                if (hasMagicSchool) {
                    addenGold += 1;
                }
                addenGold += player.getNumberOfDistrictsByColor().get(DistrictColor.noble);
                return printGold(player, addenGold);
            case ("Eveque"):
                if (hasMagicSchool) {
                    addenGold += 1;
                }
                addenGold += player.getNumberOfDistrictsByColor().get(DistrictColor.religieux);
                return printGold(player, addenGold);
            case ("Condottiere"):
                if (hasMagicSchool) {
                    addenGold += 1;
                }
                addenGold += player.getNumberOfDistrictsByColor().get(DistrictColor.militaire);
                return printGold(player, addenGold);
            case ("Marchand"):
                if (hasMagicSchool) {
                    addenGold += 1;
                }
                addenGold += player.getNumberOfDistrictsByColor().get(DistrictColor.marchand);
                return printGold(player, addenGold);
            default:
                return 0;
        }
    }

    public static void applySpecialEffect(Player player, Game game) {
        player.getGameCharacter().specialEffect(player, game);
    }
}
