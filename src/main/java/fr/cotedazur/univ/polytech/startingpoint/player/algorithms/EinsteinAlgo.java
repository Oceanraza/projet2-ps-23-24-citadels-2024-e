package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class EinsteinAlgo extends BaseAlgo {
    boolean lowestDistrictFound = false;
    public EinsteinAlgo(){
        super();
    }

    public int startOfTurnChoice() { //Always draws if needed
        if ((bot.getDistrictsInHand().isEmpty() || bot.districtsInHandAreBuilt()) || (bot.getCharacterName().equals("Architecte"))){
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
            case("Magicien"):
                magicianAlgorithm(game);
                break;
        }
    }
    public void chooseCharacterAlgorithm(Game game) { //always chooses the char that gives him the most gold, or king if can build 8th quarter next turn
        List<GameCharacter> availableChars = game.getAvailableChars();
        // If the bot can build its 8th quarter next turn, it will choose the king (if possible)
        if ((bot.getCity().getDistrictsBuilt().size() >= 7) && (bot.canBuildDistrictThisTurn())
                && (bot.isCharInList(availableChars, "Roi"))) {
            bot.chooseChar(game, "Roi");
        }
        //If the bot's hand is empty, it chooses the magician if he gives him more cards than the architect would
        else if (bot.getDistrictsInHand().isEmpty()){
            if ((bot.isCharInList(availableChars,"Magicien")) && (Utils.getHighestNumberOfCardsInHand(game.getPlayers(),this.bot) > 2)){
                bot.chooseChar(game,"Magicien");
            }
            else if (bot.isCharInList(availableChars,"Architecte")){
                bot.chooseChar(game,"Architecte");
            }
        }
        // If the bot doesn't have an immediate way to win, it will just pick the character who gives out the most gold for him
        else {
            GameCharacter chosenChar = availableChars.get(0);
            for (GameCharacter cha : availableChars) {
                    if (cha.getColor() != null){
                        if (bot.getNumberOfDistrictsByColor().get(cha.getColor()) > bot.getNumberOfDistrictsByColor().get(chosenChar.getColor())) {
                            chosenChar = cha;
                    }

                }
            }
            bot.chooseChar(game, chosenChar.getName());
        }
    }

    public void warlordAlgorithm(Game game) {
        List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
        playerList.remove(bot);
        for (Player targetedPlayer : playerList) {
            if (!targetedPlayer.getGameCharacter().getName().equals("Eveque")) { //doesn't target the bishop because he's immune to the warlord
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
    //note that this algorithm doesn't use the second part of the magician, finding it useless compared to other cards
    public void magicianAlgorithm (Game game){
        List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
        playerList.remove(bot);
        Player chosenPlayer = bot;
        for (Player p : playerList){
            if (p.getDistrictsInHand().size() > chosenPlayer.getDistrictsInHand().size()){
                chosenPlayer = p; //it takes the player's hand with the most cards
            }
        }
        boolean switching = true;
        bot.getGameCharacter().specialEffect(bot,game,switching,chosenPlayer);
    }
    public void kingAlgorithm(Game game){bot.getGameCharacter().specialEffect(bot,game);}

    public void lowestDistrictHasBeenFound() {
        lowestDistrictFound = true;
    }
    public void buildOrNot(GameState gameState){ //builds if he can
        int builtThisTurn = 0;
        ArrayList<District> tempHand = new ArrayList<>(); //Need to create a deep copy to avoid concurrent modification
        for (District district : bot.getDistrictsInHand()){
            tempHand.add(district);
        }
        for (District district : tempHand) {
            if (bot.buildDistrict(district, gameState)) {
                builtThisTurn++;
                System.out.println(bot.getCharacterName());
                if ((!bot.getCharacterName().equals("Architecte"))||(builtThisTurn == 3)){
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
            for(DistrictColor districtColor: districtColors) {
                if(!colorList.contains(districtColor)) {
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
        for(District district: districtsInHand) {
            if(districtsBuilt.contains(district)) {
                return Optional.ofNullable(district); // Discard districts already built
            }
            int count = Collections.frequency(districtsInHand, district);
            if(count > 1) {
                return Optional.ofNullable(district); // Discard duplicates
            }
        }
        if(districtsBuilt.size() + districtsInHand.size() > 8) {
            return Optional.ofNullable(districtsInHand.remove(districtsInHand.size()-1)); // Discard last district drawn
        }
        return Optional.empty();
    }
}

