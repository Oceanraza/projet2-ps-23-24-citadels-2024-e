package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public class King extends GameCharacter {
    public King () {
        super("Roi", 4, DistrictColor.noble);
    }

    @Override
    public void specialEffect(Player player, Game game,Object... optionalArgs) {
        game.getCrown().setOwner(player);
    }
}