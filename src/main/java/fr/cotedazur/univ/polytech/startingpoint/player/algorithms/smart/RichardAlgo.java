package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.List;
import java.util.Random;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.ARCHITECT;

public class RichardAlgo extends SmartAlgo {

    public enum BotStyle {
        WISE,
        SUSPICIOUS
    }

    private BotStyle getRandomBotStyle() { // Randomly choose a bot style
        BotStyle[] styles = BotStyle.values();
        return styles[new Random().nextInt(styles.length)];
    }

    public void chooseCharacterAlgorithm(Game game) {
        List<GameCharacter> availableChars = game.getAvailableChars();
        // If the bot can build its 8th quarter next turn, it will choose the assassin
        // So he won't be killed
        if (shouldPickAssassin(game) == 0) {
            //If the bot's hand is empty, it chooses the magician if he gives him more cards than the architect would
            if ((bot.getDistrictsInHand().isEmpty()) && ((bot.isCharInList(availableChars, GameCharacterRole.MAGICIAN)) || (bot.isCharInList(availableChars, ARCHITECT)))) {
                if ((bot.isCharInList(availableChars, GameCharacterRole.MAGICIAN)) && (Utils.getHighestNumberOfCardsInHand(game.getPlayers(), this.bot) > 2)) {
                    bot.chooseChar(game, GameCharacterRole.MAGICIAN);
                } else if (bot.isCharInList(availableChars, GameCharacterRole.ARCHITECT)) {
                    bot.chooseChar(game, GameCharacterRole.ARCHITECT);
                }
            } else {
                // If the bot doesn't have an immediate way to win, it will just pick the character who gives out the most gold for him
                //chooseMoneyCharacterAlgorithm(game, availableChars);
            }
        }
        bot.chooseChar(game, GameCharacterRole.ASSASSIN);
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
        switch (shouldPickAssassin(game)) {
            case 1:
                bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.THIEF);
                break;
            case 2:
                bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.WARLORD);
                break;
            case 3:
                bot.getGameCharacter().specialEffect(bot, game, ARCHITECT);
                break;
            default:
            bot.getGameCharacter().specialEffect(bot, game, selectRandomKillableCharacter(game));
        }
    }


    private boolean isAhead(Game game, Player bot) {
        return bot.getCity().size() > game.averageCitySize();
    }

    private static boolean architectIsOverpoweredFor(Player player) { // If the richest player has at least 4 gold, at least one district in hand and at least 5 districts in his city, the architect should be picked
        return player.getGold() >= 4 && player.getDistrictsInHand().isEmpty() && player.getCity().size() >= 5;
    }

    private boolean architectIsOverpoweredIn(List<Player> players) {
        for (Player player : players) {
            if (player.getGold() >= 4 && player.getDistrictsInHand().isEmpty() && player.getCity().size() >= 5) {
                return true;
            }
        }
        return false;
    }


    private static boolean couldWinNextTurn(Player player) {
        return player != null
                && player.getCity().size() == 7;
                
    }

    private static boolean IsRich(Player richestPlayer) {
        return richestPlayer != null && richestPlayer.getGold() > 6;
    }

    public boolean shouldPickArchitect(Game game) {
        int districtCount = bot.getCity().size();
        int goldCount = bot.getGold();
        int cardCount = bot.getDistrictsInHand().size();

        if (architectIsOverpoweredFor(bot)) {
            return false;
        }

        Player firstPlayer = game.getPlayers().get(0);
        if (firstPlayer.getGameCharacter().getRole() == GameCharacterRole.ASSASSIN || firstPlayer.getGameCharacter().getRole() == GameCharacterRole.ARCHITECT) {
            return true;
        }

        return false;
    }

    public int shouldPickAssassin(Game game) {

        Player richestPlayer = game.getRichestPlayer();
        Player playerWithMostDistricts = game.getPlayerWithMostDistricts();
        int playerWithMostDistrictsIndex = game.getCurrentPlayerIndexInRunningOrder(playerWithMostDistricts);
        boolean thiefIsTaken = !game.containsAvailableRole(GameCharacterRole.THIEF);
        boolean warlordIsTaken = !game.containsAvailableRole(GameCharacterRole.WARLORD);
        boolean assassinIsTaken = !game.containsAvailableRole(GameCharacterRole.ASSASSIN);
        boolean isCurrentPlayerAfterPlayerWithMostDistricts = (game.getCurrentPlayerIndexInRunningOrder(bot) > playerWithMostDistrictsIndex);

        Player firstPlayer = game.getPlayers().get(0);

        if (IsRich(richestPlayer) && thiefIsTaken || couldWinNextTurn(playerWithMostDistricts) && assassinIsTaken && isCurrentPlayerAfterPlayerWithMostDistricts) { // If the player with the most districts has at least 6 districts, the assassin should be picked
            return 1;// Kill the thief
        }
        if (isAhead(game, bot) || (couldWinNextTurn(playerWithMostDistricts) && warlordIsTaken && isCurrentPlayerAfterPlayerWithMostDistricts)) { // If the player with the assassin is ahead, the assassin should be picked
            return 2; // Kill the warlord
        }
        if (architectIsOverpoweredIn(game.getPlayers()) && getRandomBotStyle() == BotStyle.SUSPICIOUS || firstPlayer == bot && getRandomBotStyle() == BotStyle.WISE) { // Aggressive play (bot soupçonneux)
            return 3; // Kill the architect
        }

        return 0;
    }

}
