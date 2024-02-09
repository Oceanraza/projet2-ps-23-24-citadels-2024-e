package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.*;
import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.LOGGER;
/**
 * This class represents the algorithm of the bot Einstein
 * It contains the logic of the bot's actions
 */

public class EinsteinAlgo extends SmartAlgo {
    public EinsteinAlgo(){
        super();
        algoName = "Einstein";
    }

    public boolean collectGoldBeforeBuildChoice() {
        // The bot will collect gold before building if it doesn't have enough gold to build its lowest district
        Optional<District> lowestDistrict = bot.getLowestDistrictInHand();
        return lowestDistrict.isPresent() && (bot.getGold() < lowestDistrict.get().getPrice());
    }


    public void botChoosesCard(Game game, List<District> threeCards) {
        District chosenCard = chooseCard(threeCards);
        threeCards.remove(chosenCard); // Remove the chosen card from the list of three cards
        for (District card : threeCards) {
            this.bot.removeFromHandAndPutInDeck(game.getDeck(), card);
        }
        String drawMessage = bot.getName() + " pioche le " + chosenCard;
        LOGGER.info(drawMessage);
        bot.addDistrictInHand(chosenCard);
    }

    public boolean chooseAssassinAlgorithm(Game game, List<GameCharacter> availableChars) {
        if ((bot.getCity().getDistrictsBuilt().size() >= 7) && (bot.canBuildDistrictThisTurn()) && (bot.isCharInList(availableChars, ASSASSIN))) {
            bot.chooseChar(game, ASSASSIN);
            return true;
        }
        return false;
    }

    public void chooseMoneyCharacterAlgorithm(Game game, List<GameCharacter> availableChars) {
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

    @Override
    public void chooseCharacterAlgorithm(Game game) {
        List<GameCharacter> availableChars = game.getAvailableChars();
        // If the bot can build its 8th quarter next turn, it will choose the assassin
        // So he won't be killed
        if (!chooseAssassinAlgorithm(game, availableChars)) {
            //If the bot's hand is empty, it chooses the magician if he gives him more cards than the architect would
            if ((bot.getDistrictsInHand().isEmpty()) && ((bot.isCharInList(availableChars, GameCharacterRole.MAGICIAN)) || (bot.isCharInList(availableChars, ARCHITECT)))) {
                if ((bot.isCharInList(availableChars, GameCharacterRole.MAGICIAN)) && (Utils.getHighestNumberOfCardsInHand(game.getPlayers(), this.bot) > 2)) {
                    bot.chooseChar(game, GameCharacterRole.MAGICIAN);
                } else if (bot.isCharInList(availableChars, GameCharacterRole.ARCHITECT)) {
                    bot.chooseChar(game, GameCharacterRole.ARCHITECT);
                }
            } else {
                // If the bot doesn't have an immediate way to win, it will just pick the character who gives out the most gold for him
                chooseMoneyCharacterAlgorithm(game, availableChars);
            }
        }
        if (bot.getGameCharacter() == null){ //FailProof method
            bot.chooseChar(game,availableChars.get(Utils.generateRandomNumber(availableChars.size())).getRole());
        }
    }

    /**
     * This algorithm is used to destroy the lowest district of the player with the most points
     *
     * @param game The current game
     */
    @Override
    public void warlordAlgorithm(Game game) {
        List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
        playerList.remove(bot);
        for (Player targetedPlayer : playerList) {
            if (!targetedPlayer.getGameCharacter().getRole().equals(GameCharacterRole.BISHOP)) { // doesn't target the bishop because he's immune to the warlord
                targetedPlayer.getLowestDistrictBuilt().ifPresent(district -> {
                    if (Utils.canDestroyDistrict(district, bot)) {
                        bot.getGameCharacter().specialEffect(bot, game, targetedPlayer, district);
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
    @Override
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

    @Override
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
            indexKilledCharacter = selectRandomKillableCharacter(game);
        }

        targetedCharacter = game.getKillableCharacters().get(indexKilledCharacter).getRole();
        bot.getGameCharacter().specialEffect(bot, game, targetedCharacter);
    }
}

