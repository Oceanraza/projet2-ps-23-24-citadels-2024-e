package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.List;

public class ArchitectAlgo {
    public boolean shouldPickArchitect(Game game) {
        List<Player> players = game.getPlayers();
        Player richestPlayer = players.stream()
                .max((p1, p2) -> Integer.compare(p1.getGold(), p2.getGold()))
                .orElse(null);

        if (richestPlayer != null && richestPlayer.getGold() >= 4 && !(richestPlayer.getDistrictsInHand().isEmpty()) && richestPlayer.getCity().size() >= 5) {
            return false;
        }

        Player firstPlayer = players.get(0);
        if (firstPlayer.getGameCharacter().getRole() == GameCharacterRole.ASSASSIN || firstPlayer.getGameCharacter().getRole() == GameCharacterRole.ARCHITECT) {
            return true;
        }

        return false;
    }
}
