package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.List;

public class KingAlgo {
    public void kingAlgorithm(Game game, Player bot) {
        List<Player> players = game.getPlayers();
        Player potentialWinner = players.stream()
                .max((p1, p2) -> Integer.compare(p1.getCity().size(), p2.getCity().size()))
                .orElse(null);

        if (potentialWinner != null && (potentialWinner.getCity().size() == 6)) {// Si le potentiel gagnant a 5 ou 6 districts
            // S'il manque le roi, l'assassin et condotière dans le choix, prend l'eveque
            if (game.containsAvailableRole(GameCharacterRole.KING) && game.containsAvailableRole(GameCharacterRole.ASSASSIN) && game.containsAvailableRole(GameCharacterRole.WARLORD)) {
                bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.BISHOP);

                // S'il manque le roi et l'assassin dans le choix, prend le condotière pour détruire le district du potentiel gagnant
            } else if (game.containsAvailableRole(GameCharacterRole.KING) && game.containsAvailableRole(GameCharacterRole.ASSASSIN)) {
                bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.WARLORD);
                //potentialWinner.getCity().remove(0);
                // S'il manque le roi dans le choix, prend l'assassin pour tuer le potentiel gagnant
            } else if (game.containsAvailableRole(GameCharacterRole.KING)) {
                //bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.ASSASSIN);
                // Sinon devient roi
            } else {
                bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.KING);
            }
        }
    }
}
