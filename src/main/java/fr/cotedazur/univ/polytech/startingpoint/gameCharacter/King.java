package fr.cotedazur.univ.polytech.startingpoint.gameCharacter;

import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.Player;

public class King extends GameCharacter {
    public King () {
        super("Roi", 4);
    }

    @Override
    public void specialEffect(Player player, Game game) {
        game.getCrown().setOwner(player);
    }
}