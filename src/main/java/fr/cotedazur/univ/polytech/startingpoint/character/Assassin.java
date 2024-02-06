package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

public class Assassin extends GameCharacter {
    public Assassin() {
        super(GameCharacterRole.ASSASSIN, 1);
    }

    @Override
    public void specialEffect(Player player, Game game, Object... optionalArgs) {
        GameCharacterRole targetedCharacter = (GameCharacterRole) optionalArgs[0];

        if (targetedCharacter.equals(GameCharacterRole.ASSASSIN)) {
            throw new CannotAttackException("L'assassin ne peut pas se tuer lui-meme");
        }

        String killMessage = COLOR_RED + "L'assassin: a tue " + targetedCharacter.toStringLeOrLLowerCase() + " !" + COLOR_RESET;
        LOGGER.info(killMessage);

        GameCharacter targetCharacter;
        for (Player target : game.getPlayers()) {
            targetCharacter = target.getGameCharacter();
            if (targetCharacter.getRole().equals(targetedCharacter)) {
                targetCharacter.setIsAlive(false);
                targetCharacter.setAttacker(player);
                return;
            }
        }
        LOGGER.info(COLOR_RED + "Le personnage tue n'est pas en jeu !" + COLOR_RESET);
    }
}
