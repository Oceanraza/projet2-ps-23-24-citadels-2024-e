package fr.cotedazur.univ.polytech.startingpoint.gameCharacter;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.players.Player;

public class King extends GameCharacter {
    public King () {
        super("Roi", 4, DistrictColor.noble);
    }

    @Override
    public void specialEffect(Player player, Game game,Object... OptionalArgs) {
        game.getCrown().setOwner(player);
    }
}