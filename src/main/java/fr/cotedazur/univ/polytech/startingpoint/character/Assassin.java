package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public class Assassin extends GameCharacter {
    public Assassin () {
        super("Assassin", 1);
    }

    @Override
    public void specialEffect(Player player, Game game, Object... optionalArgs) {
        String targetedCharacter = (String) optionalArgs[0];
        game.killCharacter(player, targetedCharacter);
    }
}
