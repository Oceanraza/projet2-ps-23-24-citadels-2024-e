package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import static fr.cotedazur.univ.polytech.startingpoint.CitadelsLogger.LOGGER;

public class Thief extends GameCharacter {
    public Thief() {
        super(GameCharacterRole.THIEF, 2);
    }

    @Override
    public void specialEffect(Player player, Game game, Object... optionalArgs) {
        GameCharacterRole targetedCharacter = (GameCharacterRole) optionalArgs[0];
        GameCharacter stolenCharacter;
        int stolenGold;

        if (targetedCharacter.equals(GameCharacterRole.THIEF)) {
            throw new CannotAttackException("Le voleur ne peut pas se voler lui-meme");
        } else if (targetedCharacter.equals(GameCharacterRole.ASSASSIN)) {
            throw new CannotAttackException("Le voleur ne peut pas voler l'assassin");
        }

        for (Player target : game.getPlayers()) {
            stolenCharacter = target.getGameCharacter();

            if (stolenCharacter.getRole().equals(targetedCharacter)) {
                // If the character has been killed by the assassin
                if (!stolenCharacter.getIsAlive()) {
                    throw new CannotAttackException("Le voleur ne peut pas voler le personnage assassine");
                }
                stolenGold = target.getGold();
                player.addGold(stolenGold);
                target.setGold(0);
                stolenCharacter.setAttacker(player);

                String stolenMessage = targetedCharacter.toStringLeOrLUpperCase() + " s'est fait vole " + stolenGold + " or(s)";
                LOGGER.info(stolenMessage);
                return;
            }
        }
    }
}
