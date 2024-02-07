package fr.cotedazur.univ.polytech.startingpoint;


import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.*;
import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.LOGGER;

public class ActionManager {
    private ActionManager() {
        throw new IllegalStateException("Action Manager is a utility class");
    }

    public static int printGold(Player player, int addedGold) {
        player.addGold(addedGold);
        if (addedGold != 0) {
            String collectGoldMessage = (player.getGameCharacter().getRole().toStringLeOrLUpperCase()) + " a donne " + addedGold + " or a " + player.getName();
            LOGGER.info(collectGoldMessage);
        }
        return addedGold;
    }

    private static int collectGoldUtil(Player player, DistrictColor districtColor) {
        int addedGold = player.getNumberOfDistrictsByColor().get(districtColor);
        return printGold(player, addedGold);
    }

    public static void startOfTurn(Game game, Player player) {
        Bot bot = (Bot) player;
        int choice = bot.getBotAlgo().startOfTurnChoice();
        if (choice == 1) { // Take 2 gold coins
            String take2GoldMessage = bot.getName() + " prend deux pieces d'or.";
            LOGGER.info(take2GoldMessage);
            bot.addGold(2);
        } else { // Draw a card
            executeSpecificCardDraw(game, bot);
        }
    }

    public static void executeSpecificCardDraw(Game game, Player player) {
        Bot bot = (Bot) player;
        if (bot.getGameCharacter().getRole().equals(ARCHITECT)) {
            applyArchitectEffect(game, bot); //draws 2 cards
        }
        if (bot.getCity().containsDistrict("Bibliotheque")) {
            applyLibraryEffect(game, bot); //draws 2 cards
        } else if (bot.getCity().containsDistrict("Observatoire")) {
            applyObservatoryEffect(game, bot); //draws 3 cards and keeps one
        } else {
            game.drawCard(bot); //draws one card
        }
    }

    public static void applyArchitectEffect(Game game, Player player) {
        Bot bot = (Bot) player;
        for (int i = 0; i < 2; i++) {
            game.drawCard(bot);
        }
    }

    public static void applyLibraryEffect(Game game, Player player) { //draws 2 cards
        Bot bot = (Bot) player;
        for (int i = 0; i < 2; i++) {
            game.drawCard(bot);
        }
    }

    public static void applyObservatoryEffect(Game game, Player player) {
        Bot bot = (Bot) player;
        List<District> threeCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            threeCards.add(game.drawCard(bot));
        }
        bot.getBotAlgo().botChoosesCard(game, threeCards);
    }


    public static int collectGold(Player player) {
        GameCharacterRole role = player.getGameCharacter().getRole();
        if (role == WARLORD) return collectGoldUtil(player, DistrictColor.MILITARY);
        else if (role == KING) return collectGoldUtil(player, DistrictColor.NOBLE);
        else if (role == BISHOP) return collectGoldUtil(player, DistrictColor.RELIGIOUS);
        else if (role == MERCHANT) return collectGoldUtil(player, DistrictColor.TRADE);
        return 0;
    }

    public static void applyCharacterSpecialEffect(Player player, Game game) {
        player.getGameCharacter().specialEffect(player, game);
    }

    public static void applySpecialCardsEffect(Game game, Player player) {
        boolean hasManufacture = player.getCity().containsDistrict("Manufacture");
        boolean hasLaboratory = player.getCity().containsDistrict("Laboratoire");
        Bot bot = (Bot) player;
        if (hasManufacture && bot.getGold() >= 3) {
            boolean canUseManufacture = bot.getBotAlgo().manufactureChoice();
            if (canUseManufacture) {
                applyManufactureEffect(game, player, bot);
            }
        }
        if (hasLaboratory) {
            Optional<District> districtToDiscard = bot.getBotAlgo().laboratoryChoice(); // Optional.empty() if the bot doesn't want to use the laboratory
            if(districtToDiscard.isPresent()) {
                applyLaboratoryEffect(game, player, districtToDiscard, bot);
            }
        }
    }

    private static void applyLaboratoryEffect(Game game, Player player, Optional<District> districtToDiscard, Bot bot) {
        String laboratoryMessage = player.getName() + " utilise le Laboratoire pour defausser sa carte " + districtToDiscard.get().getName() + " contre 1 piece d'or.";
        LOGGER.info(laboratoryMessage);
        bot.moveCardInDeck(districtToDiscard.get(), game.getDeck());
        bot.addGold(1);
    }

    private static void applyManufactureEffect(Game game, Player player, Bot bot) {
        String manufactureMessage = player.getName() + " utilise la Manufacture pour piocher 3 cartes et paye 3 pieces.";
        LOGGER.info(manufactureMessage);
        bot.removeGold(3);
        for (int i = 0; i < 3; i++) {
            game.drawCard(bot);
        }
    }
}
