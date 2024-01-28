package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public class Assassin extends GameCharacter {
    public Assassin () {
        super(GameCharacterRole.ASSASSIN, 1);
    }

    @Override
    public void specialEffect(Player player, Game game, Object... optionalArgs) {
        GameCharacterRole targetedCharacter = (GameCharacterRole) optionalArgs[0];
        game.killCharacter(player, targetedCharacter);
        System.out.println("L'assassin a tué le " + targetedCharacter);
    }
}
