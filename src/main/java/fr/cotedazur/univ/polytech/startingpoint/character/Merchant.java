package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.*;

public class Merchant extends GameCharacter {
    public Merchant() {
        super(GameCharacterRole.MERCHANT, 6, DistrictColor.TRADE);
    }

    @Override
    public void specialEffect(Player player,Game game,Object... optionalArgs) {
        //System.out.println("Le Marchand n'a pas de compétences programmée");
    }
}