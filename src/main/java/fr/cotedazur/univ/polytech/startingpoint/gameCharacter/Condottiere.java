package fr.cotedazur.univ.polytech.startingpoint.gameCharacter;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Condottiere extends GameCharacter {
    public Condottiere() {
        super("Condottiere", 8, DistrictColor.militaire);
    }
    @Override
    public void specialEffect(Player player,Game game, Object... OptionalArgs) {
        Player targetedPlayer = (Player) OptionalArgs[0];
        if (targetedPlayer.getCharacterName().equals("Eveque")){
            System.out.println("L'Eveque est protégé par la puissance de son dieu !");
            return;
        }
        District destroyedDistrict = (District) OptionalArgs[1];

        targetedPlayer.getCity().destroyDistrict(destroyedDistrict);
        player.removeGold(destroyedDistrict.getPrice() - 1);
        System.out.println("Le Condottiere à détruit le quartier " + destroyedDistrict.getName() + " qui appartient au joueur " + targetedPlayer.getName() + " au prix de " + (destroyedDistrict.getPrice() - 1) + " or");
    }

}