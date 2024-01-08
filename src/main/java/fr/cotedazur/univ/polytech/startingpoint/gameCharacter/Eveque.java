package fr.cotedazur.univ.polytech.startingpoint.gameCharacter;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.players.*;

public class Eveque extends GameCharacter {
    public Eveque() {
        super("Eveque", 5, DistrictColor.religieux);
    }

    @Override
    public void specialEffect(Player player, Game game) {
        //System.out.println("L'Eveque n'a pas de compétences programmée");
    }
}