package fr.cotedazur.univ.polytech.startingpoint.players.BotAlgorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.Condottiere;
import fr.cotedazur.univ.polytech.startingpoint.players.Bot;
import fr.cotedazur.univ.polytech.startingpoint.players.Player;
import fr.cotedazur.univ.polytech.startingpoint.Utils;
import java.util.ArrayList;

public class EinsteinAlgo extends baseAlgo {
    boolean lowestDistrictFound = false;
    public Bot player;
    public EinsteinAlgo(){
        super();
    }
    @Override
    public void setPlayer(Bot player){
        this.player = player;
        System.out.println("Le joueur " + player.getName() + " est si intelligent qu'il est comparable à Einstein !");
    }

    public void startOfTurn(Game game) { //Always draws if needed
        if (player.getDistrictsInHand().isEmpty() || player.districtsAlreadyBuilt()) {
            District drawnDistrict = game.drawCard();
            System.out.println(player.getName() + " pioche le " + drawnDistrict);
            player.getDistrictsInHand().add(drawnDistrict);
        } else { // Otherwise it gets 2 gold coins
            System.out.println(player.getName() + " prend deux pièces d'or.");
            player.addGold(2);
        }
    }
    public void charAlgorithmsManager(Game game){
        switch (player.getCharacterName()){
            case("Condottiere"):
                warlordAlgorithm(game);
        }
    }
    public void chooseCharacterAlgorithm(Game game) { //always chooses the char that gives him the most gold, or king if can build 8th quarter next turn
        ArrayList<GameCharacter> availableChars = game.getAvailableChars();
        // If the bot can build its 8th quarter next turn, it will choose the king (if possible)
        if (!(player.getDistrictsInHand().isEmpty()) && (player.getDistrictsBuilt().size() >= 7) && (player.canBuildDistrictThisTurn())
                && (player.isCharInList(availableChars, "Roi"))) {
            player.chooseChar(game, "Roi");
        }
        // If the bot doesn't have an immediate way to win, it will just pick the character who gives out the most gold for him
        else {
            GameCharacter chosenChar = availableChars.get(0);
            for (GameCharacter cha : availableChars) {
                if (player.getNumberOfDistrictsByColor().get(cha.getColor()) > player.getNumberOfDistrictsByColor().get(chosenChar.getColor())) {
                    chosenChar = cha;
                }
            }
            player.chooseChar(game, chosenChar.getName());
        }
    }

    public void warlordAlgorithm(Game game) {
        ArrayList<Player> playerList = game.getSortedPlayersByScore();
        playerList.remove(player);
        for (Player targetedPlayer : playerList) {
            targetedPlayer.getLowestDistrict().ifPresent(value -> {
                District dist = value;
                if (Utils.canDestroyDistrict(dist, player)) {
                    player.getGameCharacter().specialEffect(player,game,targetedPlayer,dist);
                    LowestDistrictHasBeenFound();
                }
            });
            if (lowestDistrictFound) {
                break;
            }
        }
    }

    public void LowestDistrictHasBeenFound() {
        lowestDistrictFound = true;
    }
    public void buildOrNot(Game game){ //builds if he can
        for (District district : player.getDistrictsInHand()) {
            if (player.build(district)) {
                break;
            }
        }
    }
}

