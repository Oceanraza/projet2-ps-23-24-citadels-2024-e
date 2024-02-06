package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.List;
import java.util.Optional;

public abstract class BaseAlgo {
    protected Bot bot;
    protected BaseAlgo(){}
    public void setPlayer(Bot player){
        this.bot = player;
    }
    public abstract int startOfTurnChoice();
    public abstract void chooseCharacterAlgorithm(Game game);
    public abstract void warlordAlgorithm(Game game);
    public abstract void kingAlgorithm (Game game);
    public abstract void magicianAlgorithm(Game game);

    public abstract void assassinAlgorithm(Game game);

    public abstract void buildOrNot(GameState gameState);
    public abstract void huntedQuarterAlgorithm(District huntedQuarter);

    public abstract boolean manufactureChoice();

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
}