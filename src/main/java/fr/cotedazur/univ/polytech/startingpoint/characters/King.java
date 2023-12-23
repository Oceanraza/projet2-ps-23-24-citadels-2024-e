package fr.cotedazur.univ.polytech.startingpoint.characters;

import fr.cotedazur.univ.polytech.startingpoint.Characters;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.players.Player;

public class King extends Characters {
    public King () {
        super("Roi", 4);
    }

    @Override
    public void specialEffect(Player player, Game game) {
        game.getCrown().setOwner(player);
    }
}