package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.Utils;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

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

    public void charAlgorithmsManager(Game game) {
        switch (bot.getCharacterName()) {
            case ("Condottiere"):
                warlordAlgorithm(game);
                break;
            case ("Roi"):
                kingAlgorithm(game);
                break;
            case ("Assassin"):
                assassinAlgorithm(game);
                break;
            case ("Magicien"):
                magicianAlgorithm(game);
                break;
            case ("Voleur"):
                thiefAlgorithm(game);
                break;
        }
    }

    public int startOfTurnChoice() {
        if (Utils.generateRandomNumber(2) == 0) {
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
        if (Utils.generateRandomNumber(2) == 0) { // Have 50% chance to decide to destroy a building of a random player or not
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

    public void thiefAlgorithm(Game game) {
        int numberOfTargets;
        int indexPlayerStolen;
        GameCharacterRole targetedCharacter;

        // Choose a random character and steal him
        numberOfTargets = game.getCharactersThatCanBeStolen().size();
        indexPlayerStolen = Utils.generateRandomNumber(numberOfTargets);
        targetedCharacter = game.getCharactersThatCanBeStolen().get(indexPlayerStolen).getRole();

        bot.getGameCharacter().specialEffect(bot, game, targetedCharacter);
    }

    public void magicianAlgorithm(Game game) {
        if (Utils.generateRandomNumber(2) == 0) { // have 50% chance to decide to destroy a building of a random player or not
            List<Player> playerList = game.getSortedPlayersByScore();
            playerList.remove(bot);
            bot.getGameCharacter().specialEffect(bot, game, true, playerList.get(Utils.generateRandomNumber(playerList.size())));
        } else {
            bot.getGameCharacter().specialEffect(bot, game, false);
        }
    }

    public void kingAlgorithm(Game game) {
        bot.getGameCharacter().specialEffect(bot, game);
    }

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

    public void huntedQuarterAlgorithm(District huntedQuarter) {
        huntedQuarter.setColor(DistrictColor.values()[Utils.generateRandomNumber(DistrictColor.values().length)]);
    }

    public boolean manufactureChoice() {
        return Utils.generateRandomNumber(2) == 0;
    }

    public Optional<District> laboratoryChoice() {
        if (Utils.generateRandomNumber(2) == 0) {
            List<District> districtsInHand = bot.getDistrictsInHand();
            return Optional.ofNullable(districtsInHand.get(Utils.generateRandomNumber(districtsInHand.size())));
        }
        return Optional.empty();
    }

    @Override
    public void botChoosesCard(Game game, List<District> threeCards) {
        District chosenCard = threeCards.get(Utils.generateRandomNumber(threeCards.size()));
        bot.getDistrictsInHand().add(chosenCard);
    }

    public District chooseCard(List<District> cards){
        return cards.get(Utils.generateRandomNumber(cards.size()));
    }
}
