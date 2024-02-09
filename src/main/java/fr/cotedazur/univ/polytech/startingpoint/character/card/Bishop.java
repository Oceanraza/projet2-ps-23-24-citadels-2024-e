package fr.cotedazur.univ.polytech.startingpoint.character.card;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public class Bishop extends GameCharacter {
    public Bishop() {
        super(GameCharacterRole.BISHOP, 5, DistrictColor.RELIGIOUS);
    }

    @Override
    public void specialEffect(Player player, Game game,Object... optionalArgs) {
        // The bishop has no special effect
    }
}