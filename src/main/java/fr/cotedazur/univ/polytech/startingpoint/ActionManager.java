package fr.cotedazur.univ.polytech.startingpoint;


import fr.cotedazur.univ.polytech.startingpoint.board.Deck;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.*;
import static fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger.*;

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
            applyArchitectOrLibraryEffect(game, bot); //draws 2 cards
        }
        if (bot.getCity().containsDistrict("Bibliotheque")) {
            applyArchitectOrLibraryEffect(game, bot); //draws 2 cards
        } else if (bot.getCity().containsDistrict("Observatoire")) {
            applyObservatoryEffect(game, bot); //draws 3 cards and keeps one
        } else {
            game.drawCard(bot); //draws one card
        }
    }

    public static void applyArchitectOrLibraryEffect(Game game, Player player) {
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
        selectCardForBot(game, threeCards, bot);
    }

    public static void selectCardForBot(Game game, List<District> threeCards, Bot bot) {
        if (threeCards == null || threeCards.isEmpty()) {
            throw new IllegalArgumentException("List of cards cannot be null or empty");
        }
        District chosenCard = bot.getBotAlgo().chooseCard(threeCards);
        if (chosenCard == null) {
            throw new IllegalStateException("Chosen card cannot be null");
        }
        threeCards.remove(chosenCard); // Remove the chosen card from the list of three cards

        for (District card : threeCards) {
            bot.removeFromHandAndPutInDeck(game.getDeck(), card);
        }

        String drawMessage = bot.getName() + " pioche le " + chosenCard;
        LOGGER.info(drawMessage);
        bot.addDistrictInHand(chosenCard);
    }

    public static void applyGraveyardEffect(Deck deck, Player graveyardOwner, District destroyedDistrict) {
        if (graveyardOwner.getGold() >= 1 && !graveyardOwner.getCharacterName().equals("Condottiere") && ((Bot) graveyardOwner).getBotAlgo().graveyardChoice()) {
            String graveyardMessage = COLOR_PURPLE + graveyardOwner.getName() + " utilise le Cimetiere pour reprendre le " + destroyedDistrict + " dans sa main." + COLOR_RESET;
            LOGGER.info(graveyardMessage);
            graveyardOwner.addDistrictInHand(destroyedDistrict);
            graveyardOwner.removeGold(1);
        } else {
            deck.putCardAtBottom(destroyedDistrict);
        }
    }

    public static int collectGold(Player player) {
        GameCharacterRole role = player.getGameCharacter().getRole();
        if (role == WARLORD) return collectGoldUtil(player, DistrictColor.MILITARY);
        else if (role == KING) return collectGoldUtil(player, DistrictColor.NOBLE);
        else if (role == BISHOP) return collectGoldUtil(player, DistrictColor.RELIGIOUS);
        else if (role == MERCHANT) return collectGoldUtil(player, DistrictColor.TRADE);
        return 0;
    }

    public static Optional<Player> playerHasSpecialDistrict(List<Player> players, String districtName) {
        for (Player player : players) {
            if (player.getCity().containsDistrict(districtName)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
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
            districtToDiscard.ifPresent(district -> applyLaboratoryEffect(game, player, district, bot));
        }
    }

    private static void applyLaboratoryEffect(Game game, Player player, District districtToDiscard, Bot bot) {
        String laboratoryMessage = COLOR_PURPLE + player.getName() + " utilise le Laboratoire pour defausser sa carte " + districtToDiscard.getName() + " contre 1 piece d'or." + COLOR_RESET;
        LOGGER.info(laboratoryMessage);
        bot.removeFromHandAndPutInDeck(game.getDeck(), districtToDiscard);
        bot.addGold(1);
    }

    private static void applyManufactureEffect(Game game, Player player, Bot bot) {
        String manufactureMessage = COLOR_PURPLE + player.getName() + " utilise la Manufacture pour piocher 3 cartes et paye 3 pieces." + COLOR_RESET;
        LOGGER.info(manufactureMessage);
        bot.removeGold(3);
        for (int i = 0; i < 3; i++) {
            game.drawCard(bot);
        }
    }
}
