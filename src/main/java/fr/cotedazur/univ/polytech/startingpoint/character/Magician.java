package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.*;

import java.util.ArrayList;
import java.util.List;

public class Magician extends GameCharacter {
    public Magician() {
        super("Magicien", 3);
    }

    @Override
    public void specialEffect(Player player,Game game,Object... optionalArgs) {
        if ((boolean) optionalArgs[0]){ //If you decide to switch with another player
            ArrayList<District> tempD = new ArrayList<>(player.getDistrictsInHand());
            Player victim = (Player) optionalArgs[1];
            int temp = player.getDistrictsInHand().size();
            for (int k = 0; k < temp ; k++){ //We remove the first player's hand
                player.getDistrictsInHand().remove(0);
            }
            //We add the second player's hand to the first's
            for (District d : victim.getDistrictsInHand()){
                player.addDistrictInHand(d);
            }
            //We remove the second's player's hand
            temp = victim.getDistrictsInHand().size();
            //We remove the districts in a separate method to avoid ConcurrentModificationException
            for (int k = 0; k < temp ; k++){
                victim.getDistrictsInHand().remove(0);
            }
            //We add the old first's player's hand to the second
            for (District d : tempD){
                victim.addDistrictInHand(d);
            }
            System.out.println(player.getName() + " a échangé sa main avec " + victim.getName() + " !");
        }
        else {
            int nb = player.getDistrictsInHand().size();
            for (int i  = 0 ; i < nb; i++){
                player.getDistrictsInHand().remove(0);
                game.drawCard(player);
            }
            System.out.println(player.getName() + " a échangé sa main avec le deck");
        }
        System.out.println("Sa nouvelle main est : " + player.getDistrictsInHand());
    }
}