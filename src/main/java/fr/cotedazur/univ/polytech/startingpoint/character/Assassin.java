package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import static fr.cotedazur.univ.polytech.startingpoint.CitadelsLogger.LOGGER;

public class Assassin extends GameCharacter {
    public Assassin () {
        super(GameCharacterRole.ASSASSIN, 1);
    }

    @Override
    public void specialEffect(Player player, Game game, Object... optionalArgs) {
        GameCharacterRole targetedCharacter = (GameCharacterRole) optionalArgs[0];
        killCharacter(player, game, targetedCharacter);
    }

    protected void killCharacter(Player assassin, Game game, GameCharacterRole killedCharacter) {
        GameCharacter targetCharacter;
        if (killedCharacter.equals(GameCharacterRole.ASSASSIN)) { // Ne dois jamais arriver : à supprimer
            LOGGER.info("Vous ne pouvez pas vous assassiner vous-meme !");
            return;
        }
        for (Player target: game.getPlayers()) {
            targetCharacter = target.getGameCharacter();
            if (targetCharacter.getRole().equals(killedCharacter)) {
                targetCharacter.setIsAlive(false);
                targetCharacter.setAttacker(assassin);
                LOGGER.info("L'assassin a tue le " + killedCharacter);
                return;
            }
        }
        LOGGER.info("Le personnage tue n'est pas en jeu !");
    }
}
