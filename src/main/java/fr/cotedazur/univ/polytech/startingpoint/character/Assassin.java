package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public class Assassin extends GameCharacter {
    public Assassin() {
        super(GameCharacterRole.ASSASSIN, 1);
    }

    @Override
    public void specialEffect(Player player, Game game, Object... optionalArgs) {
        GameCharacterRole targetedCharacter = (GameCharacterRole) optionalArgs[0];
        killCharacter(player, game, targetedCharacter);
    }

    protected void killCharacter(Player assassin, Game game, GameCharacterRole killedCharacter) {
        GameCharacter targetCharacter;
        if (killedCharacter.equals(GameCharacterRole.ASSASSIN)) {
            System.out.println("Vous ne pouvez pas vous assassinez vous-même !");
            return;
        }
        for (Player target : game.getPlayers()) {
            targetCharacter = target.getGameCharacter();
            if (targetCharacter.getRole().equals(killedCharacter)) {
                targetCharacter.setIsAlive(false);
                targetCharacter.setAttacker(assassin);
                System.out.println("L'assassin a tué le " + killedCharacter);
                return;
            }
        }
        System.out.println("Le personnage tué n'est pas en jeu !");
    }
}
