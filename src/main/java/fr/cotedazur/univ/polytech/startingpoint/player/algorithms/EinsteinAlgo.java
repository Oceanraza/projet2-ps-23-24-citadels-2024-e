package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.KING;
import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.WARLORD;

public class EinsteinAlgo extends BaseAlgo {
    boolean lowestDistrictFound = false;
    public EinsteinAlgo(){
        super();
    }

    public int startOfTurnChoice() { // Always draws if needed
        if (bot.getDistrictsInHand().isEmpty() || bot.districtsInHandAreBuilt()) {
            return 2; // Draw a card
        } else {
            return 1; // Take 2 gold coins
        }
    }
    public void charAlgorithmsManager(Game game){
        switch (bot.getCharacterName()){
            case("Condottiere"):
                warlordAlgorithm(game);
                break;
            case("Roi"):
                kingAlgorithm(game);
                break;
            case("Assassin"):
                assassinAlgorithm(game);
                break;
            case("Magicien"):
                magicianAlgorithm(game);
                break;
        }
    }

    public void chooseCharacterAlgorithm(Game game) {
        List<GameCharacter> availableChars = game.getAvailableChars();

        // If the bot can build its 8th quarter next turn, it will choose the assassin
        // So he won't be killed
        if ((bot.getCity().getDistrictsBuilt().size() >= 7) && (bot.canBuildDistrictThisTurn())) {
            if (bot.isCharInList(availableChars, GameCharacterRole.ASSASSIN)) {
                bot.chooseChar(game, GameCharacterRole.ASSASSIN);
                return;
            }
        }
        // If the bot's hand is empty, it chooses the magician to get someone's else's hand
        else if (bot.getDistrictsInHand().isEmpty() && bot.isCharInList(availableChars, GameCharacterRole.MAGICIAN)){
            bot.chooseChar(game, GameCharacterRole.MAGICIAN);
            return;
        }
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

    public void warlordAlgorithm(Game game) {
        List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
        playerList.remove(bot);
        for (Player targetedPlayer : playerList) {
            if (!targetedPlayer.getGameCharacter().getRole().equals(GameCharacterRole.BISHOP)) { //doesn't target the bishop because he's immune to the warlord
                targetedPlayer.getLowestDistrict().ifPresent(value -> {
                    if (Utils.canDestroyDistrict(value, bot)) {
                        bot.getGameCharacter().specialEffect(bot, game, targetedPlayer, value);
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
    public void kingAlgorithm(Game game){bot.getGameCharacter().specialEffect(bot,game);}

    public void assassinAlgorithm(Game game) {
        ArrayList<GameCharacter> killableCharacters;
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
    int isKillable(ArrayList<GameCharacter> killableCharacters, GameCharacterRole charEnum) {
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
    public void buildOrNot(GameState gameState) { // builds if he can
        for (District district : bot.getDistrictsInHand()) {
            if (bot.buildDistrict(district, gameState)) {
                break;
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
            for(DistrictColor districtColor: districtColors) {
                if(!colorList.contains(districtColor)) {
                    huntedQuarter.setColor(districtColor);
                }
            }
        }
    }
}
