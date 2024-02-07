package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This class represents the random algorithm
 * It contains the random algorithm for the bot
 */

public class RandomAlgo extends BaseAlgo {
    public RandomAlgo() {
        super();
    }

    public int startOfTurnChoice() {
        if (oneChanceOutOfTwo) {
            return 1; // Take 2 gold coins
        }
        return 2; // Draw a card
    }

    public void chooseCharacterAlgorithm(Game game) {
        List<GameCharacter> availableChars = game.getAvailableChars();
        GameCharacter chosenChar = game.getAvailableChars().get(Utils.generateRandomNumber(availableChars.size()));
        bot.chooseChar(game, chosenChar.getRole());
    }

    public void warlordAlgorithm(Game game) {
        if (oneChanceOutOfTwo) { // Have 50% chance to decide to destroy a building of a random player or not
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
        }
    }

    public void assassinAlgorithm(Game game) {
        int numberOfTargets;
        int indexPlayerKilled;
        GameCharacterRole targetedCharacter;

        // Choose a random character and kill him
        numberOfTargets = game.getKillableCharacters().size();
        indexPlayerKilled = Utils.generateRandomNumber(numberOfTargets);
        targetedCharacter = game.getKillableCharacters().get(indexPlayerKilled).getRole();

        bot.getGameCharacter().specialEffect(bot, game, targetedCharacter);
    }

    @Override
    public void graveyardLogic(District district) {
        if (oneChanceOutOfTwo && bot.getGold() >= 1) {

        }
    }


    public void magicianAlgorithm(Game game) {
        if (oneChanceOutOfTwo) { // have 50% chance to decide to destroy a building of a random player or not
            List<Player> playerList = game.getSortedPlayersByScore();
            playerList.remove(bot);
            bot.getGameCharacter().specialEffect(bot, game, true, playerList.get(Utils.generateRandomNumber(playerList.size())));
        } else {
            bot.getGameCharacter().specialEffect(bot, game, false);
        }
    }

    public void huntedQuarterAlgorithm(District huntedQuarter) {
        huntedQuarter.setColor(DistrictColor.values()[Utils.generateRandomNumber(DistrictColor.values().length)]);
    }

    public boolean manufactureChoice() {
        return oneChanceOutOfTwo;
    }

    public Optional<District> laboratoryChoice() {
        if (oneChanceOutOfTwo) {
            List<District> districtsInHand = bot.getDistrictsInHand();
            return Optional.ofNullable(districtsInHand.get(Utils.generateRandomNumber(districtsInHand.size())));
        }
        return Optional.empty();
    }

    @Override
    public void botChoosesCard(Game game, List<District> threeCards) {
        District chosenCard = chooseCard(threeCards);
        bot.getDistrictsInHand().add(chosenCard);
    }

    public District chooseCard(List<District> cards){
        return cards.get(Utils.generateRandomNumber(cards.size()));
    }
}
