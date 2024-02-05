package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActionManager {
    public ActionManager() {
        throw new IllegalStateException("Action Manager is a utility class");
    }

    public static void startOfTurn(Game game, Player player) {
        Bot bot = (Bot) player;
        int choice = bot.botAlgo.startOfTurnChoice();
        if (choice == 1) { // Take 2 gold coins
            System.out.println(bot.getName() + " prend deux pièces d'or.");
            bot.addGold(2);
        } else { // Draw a card
            drawChoice(game, bot);
        }
    }

    public static void drawChoice(Game game, Player player) {
        Bot bot = (Bot) player;
        if (bot.getCity().containsDistrict("Bibliothèque")) {
            lybraryLogic(game, bot); //draws 2 cards
        } else if (bot.getCity().containsDistrict("Observatoire")) {
            observatoryLogic(game, bot); //draws 3 cards and keeps one
        } else {
            game.drawCard(bot); //draws one card
        }
    }

    public static void lybraryLogic(Game game, Player player) { //draws 2 cards
        Bot bot = (Bot) player;
        for (int i = 0; i < 2; i++) {
            game.drawCard(bot);
        }
    }

    public static void observatoryLogic(Game game, Player player) {
        Bot bot = (Bot) player;
        List<District> threeCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            threeCards.add(game.drawCard(bot));
        }
        bot.botAlgo.botChoosesCard(game, threeCards);
    }

    public static int printGold(Player player, int addedGold){
        player.addGold(addedGold);
        if (addedGold != 0) {
            System.out.println((player.getCharacterName().equals("Eveque") ? "L'" : "Le ") + player.getCharacterName() + " a donné " + addedGold + " or a " + player.getName() + ".");
        }
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

    public static void applySpecialCardsEffect(Game game, Player player) {
        boolean hasManufacture = player.getCity().containsDistrict("Manufacture");
        boolean hasLaboratory = player.getCity().containsDistrict("Laboratoire");
        Bot bot = (Bot) player;
        if (hasManufacture && bot.getGold() >= 3) {
            boolean useManufactureEffect = bot.botAlgo.manufactureChoice();
            if (useManufactureEffect) {
                System.out.println(player.getName() + " utilise la Manufacture pour piocher 3 cartes et paye 3 pièces.");
                bot.removeGold(3);
                for (int i = 0; i < 3; i++) {
                    game.drawCard(bot);
                }
            }
        }
        if (hasLaboratory) {
            Optional<District> districtToDiscard = bot.botAlgo.laboratoryChoice();
            if (districtToDiscard.isPresent()) {
                System.out.println(player.getName() + " utilise le Laboratoire pour défausser sa carte " + districtToDiscard.get().getName() + " contre 1 pièce d'or.");
                bot.getDistrictsInHand().remove(districtToDiscard.get());
                game.getDeck().addDistrict(districtToDiscard.get());
                bot.addGold(1);
            }
        }
    }
}