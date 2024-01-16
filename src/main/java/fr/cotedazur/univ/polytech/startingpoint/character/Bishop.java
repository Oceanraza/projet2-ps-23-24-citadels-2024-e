package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.player.*;

public class Bishop extends GameCharacter {
    public Bishop() {
        super("Eveque", 5, DistrictColor.religieux);
    }

    @Override
    public void specialEffect(Player player, Game game,Object... optionalArgs) {
        //System.out.println("L'évèque n'a pas de compétences programmée");
    }
}