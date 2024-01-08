package fr.cotedazur.univ.polytech.startingpoint.gameCharacter;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.players.*;

public class Condottiere extends GameCharacter {
    public Condottiere() {
        super("Condottiere", 8, DistrictColor.militaire);
    }

    @Override
    public void specialEffect(Player player, Game game) {
        //System.out.println("Le Condottiere n'a pas de compétences programmée");
    }
}