package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.Utils;
import java.util.*;

public class RandomAlgo extends BaseAlgo {
    Utils utils = new Utils();
    public RandomAlgo(){
        super();
    }
    public void charAlgorithmsManager(Game game){
        switch (bot.getCharacterName()){
            case("Condottiere"):
                warlordAlgorithm(game);
                break;
            case("Roi"):
                kingAlgorithm(game);
                break;
            case("Magicien"):
                magicianAlgorithm(game);
                break;
        }
    }

    public int startOfTurnChoice() {
        if (utils.generateRandomNumber(2) == 0) {
            return 1; // Take 2 gold coins
        }
        return 2; // Draw a card
    }

    public void chooseCharacterAlgorithm(Game game) {
        List<GameCharacter> availableChars = game.getAvailableChars();
        GameCharacter chosenChar = game.getAvailableChars().get(utils.generateRandomNumber(availableChars.size()));
            bot.chooseChar(game, chosenChar.getName());
        }

    public void warlordAlgorithm(Game game) {
        if (utils.generateRandomNumber(2) == 0) { // have 50% chance to decide to destroy a building of a random player or not
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
    public void magicianAlgorithm (Game game){
        if (utils.generateRandomNumber(2) == 0) { // have 50% chance to decide to destroy a building of a random player or not
            List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
            playerList.remove(bot);
            bot.getGameCharacter().specialEffect(bot, game, true, playerList.get(utils.generateRandomNumber(playerList.size())));
        }
        else{bot.getGameCharacter().specialEffect(bot,game,false);}
    }
    public void kingAlgorithm(Game game){bot.getGameCharacter().specialEffect(bot,game);}
    public void buildOrNot(GameState gameState){ //builds if he can
        int builtThisTurn = 0;
        for (District district : bot.getDistrictsInHand()) {
            if (bot.buildDistrict(district, gameState)) {
                builtThisTurn++;
                if ((!bot.getCharacterName().equals("Architect"))||(builtThisTurn == 3)){
                    break;
                }
            }
        }
    }

    public void huntedQuarterAlgorithm(District huntedQuarter) {
        huntedQuarter.setColor(DistrictColor.values()[utils.generateRandomNumber(DistrictColor.values().length)]);
    }

    public boolean manufactureChoice() {
        return utils.generateRandomNumber(2) == 0;
    }

    public Optional<District> laboratoryChoice() {
        if(utils.generateRandomNumber(2) == 0) {
            List<District> districtsInHand = bot.getDistrictsInHand();
            return Optional.ofNullable(districtsInHand.get(utils.generateRandomNumber(districtsInHand.size())));
        }
        return Optional.empty();
    }
}

