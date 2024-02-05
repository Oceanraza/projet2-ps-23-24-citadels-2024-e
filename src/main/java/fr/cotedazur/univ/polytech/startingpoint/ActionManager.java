package fr.cotedazur.univ.polytech.startingpoint;


import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.*;

import java.util.Optional;

public class ActionManager {
    private ActionManager() {
        throw new IllegalStateException("Action Manager is a utility class");
    }

    public static int printGold(Player player, int addedGold){
        player.addGold(addedGold);
        if (addedGold != 0) {
            System.out.println((player.getGameCharacter().getRole().toStringLeOrL()) + " a donné " + addedGold + " or à " + player.getName());
        }
        return addedGold;
    }

    private static int collectGoldUtil(Player player,  DistrictColor districtColor) {
        int addedGold = player.getNumberOfDistrictsByColor().get(districtColor);
        return printGold(player, addedGold);
    }

    public static void startOfTurn(Game game, Player player) {
        Bot bot = (Bot) player;
        int choice = bot.botAlgo.startOfTurnChoice();
        if (choice == 1) { // Take 2 gold coins
            System.out.println(bot.getName() + " prend deux pièces d'or.");
            bot.addGold(2);
        } else { // Draw a card
            game.drawCard(bot);
        }
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
        GameCharacterRole role = player.getGameCharacter().getRole();
        if (role == WARLORD) return collectGoldUtil(player, DistrictColor.MILITARY);
        else if(role == KING) return collectGoldUtil(player, DistrictColor.NOBLE);
        else if (role == BISHOP) return collectGoldUtil(player, DistrictColor.RELIGIOUS);
        else if (role == MERCHANT) return collectGoldUtil(player, DistrictColor.TRADE);
        return 0;
    }

    public static void applySpecialEffect(Player player, Game game) {
        player.getGameCharacter().specialEffect(player, game);
    }

    public static void applySpecialCardsEffect(Game game, Player player) {
        boolean hasManufacture = player.getCity().containsDistrict("Manufacture");
        boolean hasLaboratory = player.getCity().containsDistrict("Laboratoire");
        Bot bot = (Bot) player;
        if(hasManufacture && bot.getGold() >= 3) {
            boolean useManufactureEffect = bot.botAlgo.manufactureChoice();
            if(useManufactureEffect) {
                System.out.println(player.getName() + " utilise la Manufacture pour piocher 3 cartes et paye 3 pièces.");
                bot.removeGold(3);
                for(int i=0; i<3; i++) {
                    game.drawCard(bot);
                }
            }
        }
        if(hasLaboratory) {
            Optional<District> districtToDiscard = bot.botAlgo.laboratoryChoice();
            if(districtToDiscard.isPresent()) {
                System.out.println(player.getName() + " utilise le Laboratoire pour défausser sa carte " + districtToDiscard.get().getName() + " contre 1 pièce d'or.");
                bot.getDistrictsInHand().remove(districtToDiscard.get());
                game.getDeck().addDistrict(districtToDiscard.get());

                bot.addGold(1);
            }
        }
    }
}
