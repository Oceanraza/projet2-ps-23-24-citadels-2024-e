package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import static fr.cotedazur.univ.polytech.startingpoint.CitadelsLogger.LOGGER;

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

        LOGGER.info("L'assassin a tue le " + targetedCharacter.toStringLeOrL());

        GameCharacter targetCharacter;
        for (Player target : game.getPlayers()) {
            targetCharacter = target.getGameCharacter();
            if (targetCharacter.getRole().equals(targetedCharacter)) {
                targetCharacter.setIsAlive(false);
                targetCharacter.setAttacker(player);
                return;
            }
        }
    }
}
