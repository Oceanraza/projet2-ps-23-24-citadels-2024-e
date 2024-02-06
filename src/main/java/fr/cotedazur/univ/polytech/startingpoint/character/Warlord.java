package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import static fr.cotedazur.univ.polytech.startingpoint.CitadelsLogger.LOGGER;

public class Warlord extends GameCharacter {
    public Warlord() {
        super(GameCharacterRole.WARLORD, 8, DistrictColor.MILITARY);
    }
    @Override
    public void specialEffect(Player player,Game game, Object... optionalArgs) {
        Player targetedPlayer = (Player) optionalArgs[0];
        District destroyedDistrict = (District) optionalArgs[1];
        targetedPlayer.getCity().destroyDistrict(destroyedDistrict);
        player.removeGold(destroyedDistrict.getPrice() - 1);
        LOGGER.info("Le Condottiere a detruit le quartier " + destroyedDistrict.getName() + " qui appartient au joueur " + targetedPlayer.getName() + " au prix de " + (destroyedDistrict.getPrice() - 1) + " or");
    }
}
