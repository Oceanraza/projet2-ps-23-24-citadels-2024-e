package fr.cotedazur.univ.polytech.startingpoint.character.card;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public class King extends GameCharacter {
    public King() {
        super(GameCharacterRole.KING, 4, DistrictColor.NOBLE);
    }

    @Override
    public void specialEffect(Player player, Game game, Object... optionalArgs) {
        game.getCrown().setOwner(player);
    }
}
