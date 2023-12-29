package fr.cotedazur.univ.polytech.startingpoint.gameCharacter;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.players.*;

public class Marchand extends GameCharacter {
    public Marchand() {
        super("Marchand", 6);
    }

    @Override
    public void specialEffect(Player player, Game game) {
        //System.out.println("Le Marchand n'a pas de compétences programmée");
    }
}