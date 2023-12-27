package fr.cotedazur.univ.polytech.startingpoint.gameCharacter;

import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.players.Player;

public class Character1 extends GameCharacter {
    public Character1() {
        super("Personnage 1", 1);
    }

    @Override
    public void specialEffect(Player player, Game game) {
        // Add 2 gold to the player
        player.addGold(2);
        System.out.println("Le Character1 a donné 2 or à " + player.getName());
    }
}