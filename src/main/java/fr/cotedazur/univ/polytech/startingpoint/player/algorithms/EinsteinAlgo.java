package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.Utils;

import java.util.List;

public class EinsteinAlgo extends BaseAlgo {
    boolean lowestDistrictFound = false;
    private Bot bot;
    public EinsteinAlgo(){
        super();
    }
    @Override
    public void setPlayer(Bot player){
        this.bot = player;
        System.out.println("Le joueur " + player.getName() + " est si intelligent qu'il est comparable à Einstein !");
    }

    public void startOfTurn(Game game) { //Always draws if needed
        if (bot.getDistrictsInHand().isEmpty() || bot.districtsInHandAreBuilt()) {
            District drawnDistrict = game.drawCard();
            System.out.println(bot.getName() + " pioche le " + drawnDistrict);
            bot.getDistrictsInHand().add(drawnDistrict);
        } else { // Otherwise it gets 2 gold coins
            System.out.println(bot.getName() + " prend deux pièces d'or.");
            bot.addGold(2);
        }
    }
    public void charAlgorithmsManager(Game game){
        switch (bot.getCharacterName()){
            case("Condottiere"):
                warlordAlgorithm(game);
        }
    }
    public void chooseCharacterAlgorithm(Game game) { //always chooses the char that gives him the most gold, or king if can build 8th quarter next turn
        List<GameCharacter> availableChars = game.getAvailableChars();
        // If the bot can build its 8th quarter next turn, it will choose the king (if possible)
        if (!(bot.getDistrictsInHand().isEmpty()) && (bot.getCity().getDistrictsBuilt().size() >= 7) && (bot.canBuildDistrictThisTurn())
                && (bot.isCharInList(availableChars, "Roi"))) {
            bot.chooseChar(game, "Roi");
        }
        // If the bot doesn't have an immediate way to win, it will just pick the character who gives out the most gold for him
        else {
            GameCharacter chosenChar = availableChars.get(0);
            for (GameCharacter cha : availableChars) {
                if (bot.getNumberOfDistrictsByColor().get(cha.getColor()) > bot.getNumberOfDistrictsByColor().get(chosenChar.getColor())) {
                    chosenChar = cha;
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

    public void lowestDistrictHasBeenFound() {
        lowestDistrictFound = true;
    }
    public void buildOrNot(Game game){ //builds if he can
        for (District district : bot.getDistrictsInHand()) {
            if (bot.buildDistrict(district)) {
                break;
            }
        }
    }
}
