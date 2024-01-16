package fr.cotedazur.univ.polytech.startingpoint.player.BotAlgorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.Utils;
import java.util.*;

public class randomAlgo extends baseAlgo {
    public Bot player;

    Random random = new Random();
    public randomAlgo(){
        super();
    }
    public void charAlgorithmsManager(Game game){
        switch (player.getCharacterName()){
            case("Condottiere"):
                warlordAlgorithm(game);
        }
    }
    @Override
    public void setPlayer(Bot player){
        this.player = player;
        System.out.println("Le joueur " + player.getName() + " ne sait pas vraiment ce qu'il fait, on dirait qu'il joue aléatoirement. ");
    }
    public void startOfTurn(Game game) { //Always draws if needed
        if (random.nextInt(10) > 5) {
            District drawnDistrict = game.drawCard();
            System.out.println(player.getName() + " pioche le " + drawnDistrict);
            player.getDistrictsInHand().add(drawnDistrict);
        } else { // Otherwise it gets 2 gold coins
            System.out.println(player.getName() + " prend deux pièces d'or.");
            player.addGold(2);
        }
    }

    public void chooseCharacterAlgorithm(Game game) {
        ArrayList<GameCharacter> availableChars = game.getAvailableChars();
        GameCharacter chosenChar = game.getAvailableChars().get(random.nextInt(availableChars.size()));
            player.chooseChar(game, chosenChar.getName());
        }

    public void warlordAlgorithm(Game game) {
        if ((random.nextInt(10) > 5)) { // have 50% chance to decide to destroy a building of a random player or not
            ArrayList<Player> playerList = game.getSortedPlayersByScoreForWarlord();
            playerList.remove(player);
            Collections.shuffle(playerList);
            for (Player targetedPlayer : playerList) {
                if (targetedPlayer.getLowestDistrict().get().getPrice() - 1 < player.getGold()) { // if the randomPlayer doesn't have a district the bot can destroy, there's no need to loop through it
                    List<District> allDistricts = targetedPlayer.getCity().getDistrictsBuilt();
                    Collections.shuffle(allDistricts);
                    for (District d : allDistricts) {
                        if (Utils.canDestroyDistrict(d, player)) {
                            player.getGameCharacter().specialEffect(player, game, targetedPlayer, d);
                            return;
                        }
                    }

                }
            }
        }
    }
    public void buildOrNot(Game game){ //builds if he can
        for (District district : player.getDistrictsInHand()) {
            if (player.buildDistrict(district)) {
                break;
            }
        }
    }
}

