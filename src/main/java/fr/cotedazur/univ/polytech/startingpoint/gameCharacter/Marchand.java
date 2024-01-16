package fr.cotedazur.univ.polytech.startingpoint.gameCharacter;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.players.*;

public class Marchand extends GameCharacter {
    public Marchand() {
        super("Marchand", 6, DistrictColor.marchand);
    }

    @Override
    public void specialEffect(Player player,Game game,Object... OptionalArgs) {
        //System.out.println("Le Marchand n'a pas de compétences programmée");
    }
}