package fr.cotedazur.univ.polytech.startingpoint;


import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.players.Player;

public class ActionManager {

    public static int collectGold(Player player) {
        int addenGold = 0;
        switch (player.getCharacterName()){
            case("Roi"):
                addenGold = player.getNumberOfDistrictsByColor(DistrictColor.noble);
            case("Eveque"):
                addenGold = player.getNumberOfDistrictsByColor(DistrictColor.religieux);
            case("Condottiere"):
                addenGold = player.getNumberOfDistrictsByColor(DistrictColor.militaire);
            case("Marchand"):
                addenGold = player.getNumberOfDistrictsByColor(DistrictColor.marchand);
            default:
                player.addGold(addenGold);
                if (addenGold !=0){System.out.println((player.getCharacterName().equals("Eveque")? "L'" :"Le ") + player.getCharacterName() + " a donné " + addenGold + " or a " + player.getName());}
        }
        return addenGold;
    }

    public static void applySpecialEffect(Player player, Game game) {
        player.getGameCharacter().specialEffect(player, game);
    }
}
