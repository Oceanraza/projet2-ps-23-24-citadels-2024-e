package fr.cotedazur.univ.polytech.startingpoint.gameCharacter;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.Player;

public class Character3 extends GameCharacter {
    public Character3() {
        super("Personnage 3", 3);
    }

    @Override
    public void specialEffect(Player player, Game game) {
        // Add 2 gold to the player
        player.addGold(2);
        System.out.println("Le Character3 a donné 2 or à " + player.getName());
    }
}