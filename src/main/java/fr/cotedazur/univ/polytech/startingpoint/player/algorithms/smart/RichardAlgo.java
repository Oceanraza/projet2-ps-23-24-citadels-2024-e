package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.List;

public class RichardAlgo extends SmartAlgo {

    @Override
    public void chooseCharacterAlgorithm(Game game) {
        // TODO
    }

    @Override
    public void warlordAlgorithm(Game game) {
        // TODO
    }

    @Override
    public void kingAlgorithm(Game game) {
        // TODO

    }

    @Override
    public void magicianAlgorithm(Game game) {
        // TODO
    }

    @Override
    public void assassinAlgorithm(Game game) {
        List<Player> players = game.getPlayers();
        Player richestPlayer = players.stream()
                .max((p1, p2) -> Integer.compare(p1.getGold(), p2.getGold()))
                .orElse(null);

        List<Player> runningOrder = game.getRunningOrder();
        int previousPlayerIndexes = game.getCurrentPlayerIndexInRunningOrder(bot);

        boolean thiefIsTaken = !game.containsAvailableRole(GameCharacterRole.THIEF);

        if (pLayerWithTooMuchMoney(richestPlayer, thiefIsTaken) || winnerMaybeTookThief(game, richestPlayer, previousPlayerIndexes)) { // If the richest player has more than 6 gold, the assassin should target the richest player
            bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.THIEF);
            // If the richest player could be the thief, the assassin should target the thief
            bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.THIEF);
        } else if (imAhead(game) || winnerMaybeTookWarlord(game, richestPlayer, previousPlayerIndexes)) {// If the player with the assassin is ahead, target the condottiere
            // If the player with the assassin is ahead, target the condottiere
            bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.WARLORD);
        } else {
            selectRandomKillableCharacter(game);
            bot.getGameCharacter().specialEffect(bot, game, selectRandomKillableCharacter(game));

        }
    }

    private boolean imAhead(Game game) {
        return bot.getCity().size() > game.getPlayers().stream().mapToInt(player -> player.getCity().size()).average().getAsDouble();
    }

    private static boolean winnerMaybeTookThief(Game game, Player richestPlayer, int previousPlayerIndexes) {
        return richestPlayer != null && richestPlayer.getCity().size() == 7 && game.containsAvailableRole(GameCharacterRole.THIEF) && game.getCurrentPlayerIndexInRunningOrder(richestPlayer) < previousPlayerIndexes;
    }

    private static boolean winnerMaybeTookWarlord(Game game, Player richestPlayer, int previousPlayerIndexes) {
        return richestPlayer != null
                && richestPlayer.getCity().size() == 7
                && game.containsAvailableRole(GameCharacterRole.WARLORD)
                && game.getCurrentPlayerIndexInRunningOrder(richestPlayer) < previousPlayerIndexes;
    }

    private static boolean pLayerWithTooMuchMoney(Player richestPlayer, boolean thiefIsTaken) {
        return richestPlayer != null && richestPlayer.getGold() > 6 && thiefIsTaken;
    }

    @Override
    public void botChoosesCard(Game game, List<District> threeCards) {
        // TODO
    }

    @Override
    public boolean graveyardChoice() {
        return true;
    }
}
