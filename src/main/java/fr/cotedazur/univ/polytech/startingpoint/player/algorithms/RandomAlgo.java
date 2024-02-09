package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

/**
 * This class represents the random algorithm
 * It contains the random algorithm for the bot
 */

public class RandomAlgo extends BaseAlgo {
    public RandomAlgo() {
        super();
        algoName = "Random";
    }

    @Override
    public int startOfTurnChoice() {
        if (getRandomBoolean()) {
            return 1; // Take 2 gold coins
        }
        return 2; // Draw a card
    }

    @Override
    public boolean collectGoldBeforeBuildChoice() {
        return getRandomBoolean();
    }

    @Override
    public void chooseCharacterAlgorithm(Game game) {
        List<GameCharacter> availableChars = game.getAvailableChars();
        GameCharacter chosenChar = game.getAvailableChars().get(Utils.generateRandomNumber(availableChars.size()));
        bot.chooseChar(game, chosenChar.getRole());
    }

    @Override
    public void warlordAlgorithm(Game game) {
        if (getRandomBoolean()) { // Have 50% chance to decide to destroy a building of a random player or not
            List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
            playerList.remove(bot);
            Collections.shuffle(playerList);
            for (Player targetedPlayer : playerList) {
                List<District> allDistricts = targetedPlayer.getCity().getDistrictsBuilt();
                Collections.shuffle(allDistricts);
                for (District d : allDistricts) {
                    if (Utils.canDestroyDistrict(d, bot)) {
                        bot.getGameCharacter().specialEffect(bot, game, targetedPlayer, d);
                        return;
                    }
                }
            }
        } else {
            LOGGER.info(COLOR_RED + "Il ne détruit aucun quartier" + COLOR_RESET);
        }
    }

    @Override
    public void assassinAlgorithm(Game game) {
        if (getRandomBoolean()) { // have 50% chance to decide to assassinate a character
            int numberOfTargets;
            int indexPlayerKilled;
            GameCharacterRole targetedCharacter;

            // Choose a random character and kill him
            numberOfTargets = game.getKillableCharacters().size();
            indexPlayerKilled = Utils.generateRandomNumber(numberOfTargets);
            targetedCharacter = game.getKillableCharacters().get(indexPlayerKilled).getRole();

            bot.getGameCharacter().specialEffect(bot, game, targetedCharacter);
        } else {
            LOGGER.info(COLOR_RED + "Il n'assassine personne" + COLOR_RESET);
        }
    }

    @Override
    public void magicianAlgorithm(Game game) {
        if (oneChanceOutOfTwo) { // have 50% chance to decide to destroy a building of a random player or not
            List<Player> playerList = game.getSortedPlayersByScore();
            playerList.remove(bot);
            bot.getGameCharacter().specialEffect(bot, game, true, playerList.get(Utils.generateRandomNumber(playerList.size())));
        } else {
            bot.getGameCharacter().specialEffect(bot, game, false);
        }
    }

    @Override
    public void buildOrNot(GameState gameState) { //builds if he can
        int builtThisTurn = 0;

        for (District district : bot.getDistrictsInHand()) {
            if (bot.buildDistrict(district, gameState)) {
                builtThisTurn++;
                if ((!bot.getCharacterName().equals("Architect")) || (builtThisTurn == 3)) {
                    break;
                }
            }
        }
    }

    @Override
    public void huntedQuarterAlgorithm(District huntedQuarter) {
        huntedQuarter.setColor(DistrictColor.values()[Utils.generateRandomNumber(DistrictColor.values().length)]);
    }

    @Override
    public boolean manufactureChoice() {
        return oneChanceOutOfTwo;
    }

    @Override
    public Optional<District> laboratoryChoice() {
        if (oneChanceOutOfTwo) {
            List<District> districtsInHand = bot.getDistrictsInHand();
            return !districtsInHand.isEmpty() ? Optional.ofNullable(districtsInHand.get(Utils.generateRandomNumber(districtsInHand.size()))) : Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public boolean graveyardChoice() {
        return oneChanceOutOfTwo;
    }

    @Override
    public void botChoosesCard(Game game, List<District> threeCards) {
        District chosenCard = chooseCard(threeCards);
        bot.addDistrictInHand(chosenCard);
    }

    public District chooseCard(List<District> cards){
        return cards.get(Utils.generateRandomNumber(cards.size()));
    }
}
