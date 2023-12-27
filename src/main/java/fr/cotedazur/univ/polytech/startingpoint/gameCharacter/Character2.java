package fr.cotedazur.univ.polytech.startingpoint.gameCharacter;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.Player;

public class Character2 extends GameCharacter {
    public Character2() {
        super("Personnage 2", 2);
    }

    @Override
    public void specialEffect(Player player, Game game) {
        // Add 2 gold to the player
        player.addGold(2);
        System.out.println("Le Character2 a donné 2 or à " + player.getName());
    }
}