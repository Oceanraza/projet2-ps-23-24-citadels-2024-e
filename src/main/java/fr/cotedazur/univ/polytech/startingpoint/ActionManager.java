package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public class ActionManager {
    private ActionManager() {
        throw new IllegalStateException("Action Manager is a utility class");
    }

    public static int printGold(Player player, int addedGold){
        player.addGold(addedGold);
        if (addedGold !=0){System.out.println((player.getCharacterName().equals("Eveque")? "L'" :"Le ") + player.getCharacterName() + " a donné " + addedGold + " or a " + player.getName() + ".");}
        return addedGold;
    }

    public static int calculateGold(Player player) {
        int addedGold = 0;
        boolean hasMagicSchool = player.getCity().containsDistrict("Ecole de magie");
        if (hasMagicSchool) {
            addedGold += 1;
        }
        addedGold += player.getNumberOfDistrictsByColor().get(player.getGameCharacter().getColor());
        return addedGold;
    }

    public static int collectGold(Player player) {
        String characterName = player.getCharacterName();
        if (characterName.equals("Roi") || characterName.equals("Eveque") ||
            characterName.equals("Condottiere") || characterName.equals("Marchand")) {
            return printGold(player, calculateGold(player));
        }
        return 0;
    }

    public static void applySpecialEffect(Player player, Game game) {
        player.getGameCharacter().specialEffect(player, game);
    }
}
