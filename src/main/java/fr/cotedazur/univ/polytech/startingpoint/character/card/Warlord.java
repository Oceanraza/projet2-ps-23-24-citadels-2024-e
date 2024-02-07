package fr.cotedazur.univ.polytech.startingpoint.character.card;

import fr.cotedazur.univ.polytech.startingpoint.ActionManager;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

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
        String destroyDistrictMessage = COLOR_RED + "Le Condottiere a detruit le quartier " + destroyedDistrict.getName() + " qui appartient au joueur " + targetedPlayer.getName() + " au prix de " + (destroyedDistrict.getPrice() - 1) + " or." + COLOR_RESET;
        LOGGER.info(destroyDistrictMessage);

        Optional<Player> playerWithGraveyard = ActionManager.playerHasSpecialDistrict(game.getPlayers(), "Cimetiere");
        if (playerWithGraveyard.isPresent()) {
            ActionManager.applyGraveyardEffect(game.getDeck(), playerWithGraveyard.get(), destroyedDistrict);
        } else {
            game.getDeck().putCardAtBottom(destroyedDistrict);
        }
    }
}
