package fr.cotedazur.univ.polytech.startingpoint.characters;

import fr.cotedazur.univ.polytech.startingpoint.Characters;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.Player;

public class Character1 extends Characters {
    public Character1 () {
        super("Personnage 1", 1);
    }

    @Override
    public void specialEffect(Player player, Game game) {
        // Add 2 gold to the player
        player.setGold(player.getGold() + 2);
    }
}