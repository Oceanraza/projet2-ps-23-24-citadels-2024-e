package fr.cotedazur.univ.polytech.startingpoint;


import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public class ActionManager {
    public static int printGold(Player player, int addenGold){
        player.addGold(addenGold);
        if (addenGold !=0){System.out.println((player.getCharacterName().equals("Eveque")? "L'" :"Le ") + player.getCharacterName() + " a donné " + addenGold + " or a " + player.getName());}
        return addenGold;
    }
    public static int collectGold(Player player) {
        int addenGold = 0;
        switch (player.getCharacterName()) {
            case ("Roi"):
                addenGold = player.getNumberOfDistrictsByColor().get(DistrictColor.NOBLE);
                return printGold(player, addenGold);
            case ("Eveque"):
                addenGold = player.getNumberOfDistrictsByColor().get(DistrictColor.RELIGIEUX);
                return printGold(player, addenGold);
            case ("Condottiere"):
                addenGold = player.getNumberOfDistrictsByColor().get(DistrictColor.MILITAIRE);
                return printGold(player, addenGold);
            case ("Marchand"):
                addenGold = player.getNumberOfDistrictsByColor().get(DistrictColor.MARCHAND);
                return printGold(player, addenGold);
            default:
                return 0;
        }
    }

    public static void applySpecialEffect(Player player, Game game) {
        player.getGameCharacter().specialEffect(player, game);
    }
}
