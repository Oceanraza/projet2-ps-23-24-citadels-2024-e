package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.ArrayList;
import java.util.List;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

public class Magician extends GameCharacter {
    public Magician() {
        super(GameCharacterRole.MAGICIAN, 3);
    }

    @Override
    public void specialEffect(Player player, Game game, Object... optionalArgs) {
        if ((boolean) optionalArgs[0]) { // If you decide to switch with another player
            List<District> tempD = new ArrayList<>(player.getDistrictsInHand());
            Player victim = (Player) optionalArgs[1];
            int temp = player.getDistrictsInHand().size();
            // We remove the first player's hand
            if (temp > 0) {
                player.getDistrictsInHand().subList(0, temp).clear();
            }
            // We add the second player's hand to the first's
            for (District d : victim.getDistrictsInHand()){
                player.addDistrictInHand(d);
            }
            // We remove the second's player's hand
            temp = victim.getDistrictsInHand().size();
            // We remove the districts in a separate method to avoid ConcurrentModificationException
            if (temp > 0) {
                victim.getDistrictsInHand().subList(0, temp).clear();
            }
            // We add the old first's player's hand to the second
            for (District d : tempD){
                victim.addDistrictInHand(d);
            }
            LOGGER.info(COLOR_RED + player.getName() + " a echange sa main avec " + victim.getName() + " !" + COLOR_RESET);
        }
        else {
            int nb = player.getDistrictsInHand().size();
            int i = 0;
            while (i < nb && !player.getDistrictsInHand().isEmpty()) {
                player.getDistrictsInHand().remove(0);
                game.drawCard(player);
                i++;
            }
            String swapHandsMessage = COLOR_RED + player.getName() + " a echange sa main avec le deck" + COLOR_RESET;
            LOGGER.info(swapHandsMessage);
        }
        String newHandMessage = COLOR_RED + "Sa nouvelle main est : " + player.getDistrictsInHand() + COLOR_RESET;
        LOGGER.info(newHandMessage);
    }
}