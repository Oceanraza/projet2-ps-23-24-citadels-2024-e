package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public class Architect extends GameCharacter {
    public Architect() {
        super(GameCharacterRole.ARCHITECT, 7);
    }

    @Override
    public void specialEffect(Player player, Game game, Object... optionalArgs) {
        // The architect has no special effect
    }

}