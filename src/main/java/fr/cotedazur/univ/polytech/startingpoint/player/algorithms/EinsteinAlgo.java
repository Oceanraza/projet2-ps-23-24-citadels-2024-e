package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.Utils;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.*;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.*;
/**
 * This class represents the algorithm of the bot Einstein
 * It contains the logic of the bot's actions
 */

public class EinsteinAlgo extends BaseAlgo {
    boolean lowestDistrictFound = false;
    public EinsteinAlgo(){
        super();
    }

    public void botChoosesCard(Game game, List<District> threeCards) {
        District chosenCard = chooseCard(threeCards);
        threeCards.remove(chosenCard);
        for (District card : threeCards) {
            game.getDeck().discard(card);
        }
        System.out.println(bot.getName() + " pioche le " + chosenCard);
        bot.getDistrictsInHand().add(chosenCard);
    }


    public int startOfTurnChoice() { // Always draws if needed
        if (bot.getDistrictsInHand().isEmpty() || bot.districtsInHandAreBuilt() || bot.getGameCharacter().getRole().equals(ARCHITECT)) {
            return 2; // Draw a card
        } else {
            return 1; // Take 2 gold coins
        }
    }


    public void graveyardLogic(Game game, Player targetedPlayer, District destroyedDistrict) {
        if (bot.getCity().containsDistrict("Cimetière") && bot.getGold() >= 1 && !bot.getCharacterName().equals("Condottiere")) {
            System.out.println(bot.getName() + " utilise le Cimetière pour reprendre le " + destroyedDistrict + " dans sa main.");
            bot.getDistrictsInHand().add(destroyedDistrict);
            bot.addGold(-1);
        }
    }


    private void addTwoGold() {
        System.out.println(bot.getName() + " prend deux pièces d'or.");
        bot.addGold(2);
    }


    public void charAlgorithmsManager(Game game){
        GameCharacterRole role = bot.getGameCharacter().getRole();
        if (role == WARLORD) warlordAlgorithm(game);
        else if (role == KING) kingAlgorithm(game);
        else if (role == ASSASSIN) assassinAlgorithm(game);
        else if (role == MAGICIAN) magicianAlgorithm(game);
    }

    public void chooseCharacterAlgorithm(Game game) {
        List<GameCharacter> availableChars = game.getAvailableChars();
        // If the bot can build its 8th quarter next turn, it will choose the assassin
        // So he won't be killed
        if ((bot.getCity().getDistrictsBuilt().size() >= 7) && (bot.canBuildDistrictThisTurn()) && (bot.isCharInList(availableChars, ASSASSIN))) {
                bot.chooseChar(game, ASSASSIN);
        }
        //If the bot's hand is empty, it chooses the magician if he gives him more cards than the architect would
        else if ((bot.getDistrictsInHand().isEmpty()) && (((bot.isCharInList(availableChars, GameCharacterRole.MAGICIAN)) || (bot.isCharInList(availableChars, ARCHITECT))))) {
            if ((bot.isCharInList(availableChars, GameCharacterRole.MAGICIAN)) && (Utils.getHighestNumberOfCardsInHand(game.getPlayers(), this.bot) > 2)) {
                bot.chooseChar(game, GameCharacterRole.MAGICIAN);
            } else if (bot.isCharInList(availableChars, GameCharacterRole.ARCHITECT)) {
                bot.chooseChar(game, GameCharacterRole.ARCHITECT);
            }
        } else {
            // If the bot doesn't have an immediate way to win, it will just pick the character who gives out the most gold for him
            GameCharacter chosenChar = availableChars.get(1);
            int numberOfDistrictByColor;
            int goldCollectedWithDistrictColor = 0;

            for (GameCharacter cha : availableChars) {
                // We only compare character that collects gold according to his districts
                if (cha.getColor() != null) {
                    numberOfDistrictByColor = bot.getNumberOfDistrictsByColor().get(cha.getColor());
                    if (numberOfDistrictByColor > goldCollectedWithDistrictColor) {
                        goldCollectedWithDistrictColor = numberOfDistrictByColor;
                        chosenChar = cha;
                    }
                }
            }
            bot.chooseChar(game, chosenChar.getRole());
        }
    }

    public void warlordAlgorithm(Game game) {
        List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
        playerList.remove(bot);
        for (Player targetedPlayer : playerList) {
            if (!targetedPlayer.getGameCharacter().getRole().equals(GameCharacterRole.BISHOP)) { //doesn't target the bishop because he's immune to the warlord
                targetedPlayer.getLowestDistrict().ifPresent(value -> {
                    if (Utils.canDestroyDistrict(value, bot)) {
                        bot.getGameCharacter().specialEffect(bot, game, targetedPlayer, value);
                        graveyardLogic(game, targetedPlayer, value); // Call the graveyard logic here
                        lowestDistrictHasBeenFound();
                    }
                });
                if (lowestDistrictFound) {
                    break;
                }
            }
        }
    }
    // Note that this algorithm doesn't use the second part of the magician, finding it useless compared to other cards
    public void magicianAlgorithm (Game game){
        List<Player> playerList = game.getSortedPlayersByScore();
        playerList.remove(bot);
        Player chosenPlayer = bot;
        for (Player p : playerList){
            if (p.getDistrictsInHand().size() > chosenPlayer.getDistrictsInHand().size()){
                chosenPlayer = p; // It takes the player's hand with the most cards
            }
        }
        boolean switching = true;
        bot.getGameCharacter().specialEffect(bot, game, switching, chosenPlayer);
    }

    public void kingAlgorithm(Game game) {
        bot.getGameCharacter().specialEffect(bot, game);
    }

    public void assassinAlgorithm(Game game) {
        List<GameCharacter> killableCharacters;
        int indexKilledCharacter;
        GameCharacterRole targetedCharacter;

        int indexWarlord;
        int indexKing;

        killableCharacters = game.getKillableCharacters();
        indexWarlord = isKillable(killableCharacters, WARLORD);
        indexKing = isKillable(killableCharacters, KING);

        // Kill the warlord if possible
        if (indexWarlord != -1) {
            indexKilledCharacter = indexWarlord;
        }
        // Kill the king if the warlord can't be killed
        else if (indexKing != -1) {
            indexKilledCharacter = indexKing;
        }
        // Kill a random character if neither the warlord nor the king can be killed
        else {
            int numberOfTargets = game.getKillableCharacters().size();
            indexKilledCharacter = Utils.generateRandomNumber(numberOfTargets);
        }

        targetedCharacter = game.getKillableCharacters().get(indexKilledCharacter).getRole();
        bot.getGameCharacter().specialEffect(bot, game, targetedCharacter);
    }

    // To know if the assassin can kill this character
    int isKillable(List<GameCharacter> killableCharacters, GameCharacterRole charEnum) {
        for (int i = 0; i < killableCharacters.size(); i++) {
            // If the character can be killed, this functions returns the index of this character
            if (killableCharacters.get(i).getRole() == charEnum) {
                return i;
            }
        }
        return -1;
    }

    public void lowestDistrictHasBeenFound() {
        lowestDistrictFound = true;
    }

    public void buildOrNot(GameState gameState) { //builds if he can
        int builtThisTurn = 0;
        ArrayList<District> tempHand = new ArrayList<>(); //Need to create a deep copy to avoid concurrent modification
        for (District district : bot.getDistrictsInHand()) {
            tempHand.add(district);
        }
        for (District district : tempHand) {

            if (bot.buildDistrict(district, gameState)) {
                builtThisTurn++;
              if ((!bot.getCharacterName().equals("Architecte")) || (builtThisTurn == 3)) {
                    break;
                }
            }
        }
    }

    public void huntedQuarterAlgorithm(District huntedQuarter) {
        Set<DistrictColor> colorList = new HashSet<>();
        List<District> districtsBuilt = bot.getCity().getDistrictsBuilt();
        for (District district : districtsBuilt) {
            colorList.add(district.getColor());
        }
        DistrictColor[] districtColors = DistrictColor.values();
        if (colorList.size() == districtColors.length - 1) {
            for (DistrictColor districtColor : districtColors) {
                if (!colorList.contains(districtColor)) {
                    huntedQuarter.setColor(districtColor);
                }
            }
        }
    }

    public boolean manufactureChoice() { // Use manufacture effect if the player has less than 7 built + buildable districts
        Set<District> builtAndBuildableDistricts = new HashSet<>(bot.getCity().getDistrictsBuilt());
        builtAndBuildableDistricts.addAll(bot.getDistrictsInHand());
        return builtAndBuildableDistricts.size() < 8 - 1; // If player has 7 built + buildable districts he can just draw without paying 3 gold coins
    }

    public Optional<District> laboratoryChoice() {
        List<District> districtsBuilt = bot.getCity().getDistrictsBuilt();
        List<District> districtsInHand = bot.getDistrictsInHand();
        for (District district : districtsInHand) {
            if (districtsBuilt.contains(district)) {
                return Optional.ofNullable(district); // Discard districts already built
            }
            int count = Collections.frequency(districtsInHand, district);
            if (count > 1) {
                return Optional.ofNullable(district); // Discard duplicates
            }
        }
        if (districtsBuilt.size() + districtsInHand.size() > 8) {
            return Optional.ofNullable(districtsInHand.remove(districtsInHand.size() - 1)); // Discard last district drawn
        }
        return Optional.empty();
    }
    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public District chooseCard(List<District> cards) {
        District chosenCard = null;
        int minCost = Integer.MAX_VALUE;
        for (District card : cards) {
            if (card.getPrice() <= bot.getGold() && card.getPrice() < minCost) {
                chosenCard = card;
                minCost = card.getPrice();
            }
        }
        return chosenCard;
    }

}

