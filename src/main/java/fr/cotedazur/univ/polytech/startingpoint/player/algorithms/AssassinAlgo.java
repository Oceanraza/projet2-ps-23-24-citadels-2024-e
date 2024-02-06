package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.List;

public class AssassinAlgo extends EinsteinAlgo {
    public AssassinAlgo() {
        super();
    }

    public GameCharacterRole chooseVictim(Game game) {
        List<Player> players = game.getPlayers();
        Player richestPlayer = players.stream()
                .max((p1, p2) -> Integer.compare(p1.getGold(), p2.getGold()))
                .orElse(null);

        if (richestPlayer != null && richestPlayer.getGameCharacter().getRole() == GameCharacterRole.THIEF) {
            return GameCharacterRole.THIEF;
        }

        Player leadingPlayer = players.stream()
                .max((p1, p2) -> Integer.compare(p1.getScore(), p2.getScore()))
                .orElse(null);

        if (leadingPlayer != null && leadingPlayer.getGameCharacter().getRole() == GameCharacterRole.WARLORD) {
            return GameCharacterRole.WARLORD;
        }

        // Default victim
        return GameCharacterRole.THIEF;
    }
}