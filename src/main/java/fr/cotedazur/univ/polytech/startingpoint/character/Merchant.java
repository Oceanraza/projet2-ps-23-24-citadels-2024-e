package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public class Merchant extends GameCharacter {
    public Merchant() {
        super(GameCharacterRole.MERCHANT, 6, DistrictColor.TRADE);
    }

    @Override
    public void specialEffect(Player player,Game game,Object... optionalArgs) {
        // The merchant has no special effect
    }
}