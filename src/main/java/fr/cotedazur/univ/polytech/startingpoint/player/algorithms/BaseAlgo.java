package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger.*;

public abstract class BaseAlgo {

    protected Bot bot;
    protected String algoName;

    protected BaseAlgo() {
    }

    public void setBot(Bot player) {
        this.bot = player;
    }

    public String getAlgoName() {
        return algoName;
    }

    public abstract int startOfTurnChoice();

    public abstract void chooseCharacterAlgorithm(Game game);

    public abstract void warlordAlgorithm(Game game);

    public abstract void magicianAlgorithm(Game game);

    public void assassinAlgorithm(Game game) {
        if (flipCoin()) { // have 50% chance to decide to assassinate a character
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

    public abstract void huntedQuarterAlgorithm(District huntedQuarter);

    public abstract boolean manufactureChoice();

    public abstract boolean graveyardChoice();

    public abstract Optional<District> laboratoryChoice();

    public abstract void botChoosesCard(Game game, List<District> threeCards);

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
            default:
                break;
        }
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


    public void kingAlgorithm(Game game) {
        bot.getGameCharacter().specialEffect(bot, game);
    }

    public void buildOrNot(GameState gameState) { //Builds districts in the bot's hand
        int builtThisTurn = 0;
        ArrayList<District> tempHand = new ArrayList<>(bot.getDistrictsInHand()); //Need to create a deep copy to avoid concurrent modification
        for (District district : tempHand) {

            if (bot.buildDistrict(district, gameState)) {
                builtThisTurn++;
                if ((!bot.getCharacterName().equals("Architecte")) || (builtThisTurn == 3)) {
                    break;
                }
            }
        }
    }

    protected boolean flipCoin() {
        return Utils.generateRandomNumber(2) == 0;
    }

    public int selectRandomKillableCharacter(Game game) {
        int numberOfTargets = game.getKillableCharacters().size();
        return Utils.generateRandomNumber(numberOfTargets);
    }

    public GameCharacter selectRandomKillableCharacterExcept(GameCharacterRole gameCharacterRole, Game game) {
        List<GameCharacter> killableCharacters = new ArrayList<>(game.getKillableCharacters());
        killableCharacters.removeIf(character -> character.getRole().equals(gameCharacterRole));

        if (killableCharacters.isEmpty()) {
            return null;
        }

        int randomIndex = Utils.generateRandomNumber(killableCharacters.size());
        return killableCharacters.get(randomIndex);
    }

    public abstract District chooseCard(List<District> threeCards);
}
