package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.*;

public class Warlord extends GameCharacter {
    public Warlord() {
        super("Condottiere", 8, DistrictColor.MILITAIRE);
    }
    @Override
    public void specialEffect(Player player,Game game, Object... optionalArgs) {
        Player targetedPlayer = (Player) optionalArgs[0];
        District destroyedDistrict = (District) optionalArgs[1];
        targetedPlayer.getCity().destroyDistrict(destroyedDistrict);
        player.removeGold(destroyedDistrict.getPrice() - 1);
        System.out.println("Le Condottiere à détruit le quartier " + destroyedDistrict.getName() + " qui appartient au joueur " + targetedPlayer.getName() + " au prix de " + (destroyedDistrict.getPrice() - 1) + " or");
    }

}