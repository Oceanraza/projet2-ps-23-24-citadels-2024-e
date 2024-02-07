package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.List;
import java.util.Random;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.*;

public class RichardAlgo extends SmartAlgo {

    public enum BotStyle {
        WISE,
        SUSPICIOUS
    }

    private boolean shouldPickAssassinNextTurn = false;

    private BotStyle getRandomBotStyle() { // Randomly choose a bot style
        BotStyle[] styles = BotStyle.values();
        return styles[new Random().nextInt(styles.length)];
    }

    public void chooseCharacterAlgorithm(Game game) {
        List<GameCharacter> availableChars = game.getAvailableChars();
        // If the bot can build its 8th quarter next turn, it will choose the assassin
        // So he won't be killed
        if (shouldPickAssassinNextTurn && game.containsAvailableRole(ASSASSIN)) {
            bot.chooseChar(game, ASSASSIN);
        }
        //Déclenché en fin de partie
        if (game.getPlayerWith6Districts() != null) { // I
            finishOrCounter(game);
        }

        //Algo durant la partie
        if (shouldPickAssassin(game) > 0) {
            bot.chooseChar(game, ASSASSIN);
        } else if (shouldPickArchitect(game) > 0) {
            bot.chooseChar(game, ARCHITECT);
        } else if (shouldPickKing(game) > 0) {
            bot.chooseChar(game, KING);
        }
    }

    private void finishOrCounter(Game game) {
        if (game.containsAvailableRole(KING)) {
            bot.chooseChar(game, KING);
            if (game.getPlayerWith6Districts() == bot)
                shouldPickAssassinNextTurn = true;
        } else if (game.containsAvailableRole(ASSASSIN)) {
            bot.chooseChar(game, ASSASSIN);
        } else if (game.containsAvailableRole(WARLORD)) {
            bot.chooseChar(game, WARLORD);
        } else if (game.containsAvailableRole(BISHOP)) {
            bot.chooseChar(game, BISHOP);
        }
    }


    @Override
    public void warlordAlgorithm(Game game) {
        if (oneChanceOutOfTwo) { // Have 50% chance to decide to destroy a building of a random player or not
            if (game.getPlayerWith6Districts() == bot) { // If the bot has 6 districts, he will destroy a building of a random player
                List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
                playerList.remove(bot);
                for (Player targetedPlayer : playerList) {
                    List<District> allDistricts = targetedPlayer.getCity().getDistrictsBuilt();
                    for (District d : allDistricts) {
                        if (Utils.canDestroyDistrict(d, bot)) {
                            bot.getGameCharacter().specialEffect(bot, game, targetedPlayer, d);
                            return;
                        }
                    }
                }
            }
            if (game.getPlayerWith6Districts() != null) {
                bot.getGameCharacter().specialEffect(bot, game, game.getPlayerWith6Districts());
            }
        }
    }

    @Override
    public void magicianAlgorithm(Game game) {
        // TODO
    }

    @Override
    public void assassinAlgorithm(Game game) {
        switch (shouldPickAssassin(game)) {
            case 1:
                bot.getGameCharacter().specialEffect(bot, game, KING);
                break;
            case 2:
                bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.THIEF);
                break;
            case 3:
                bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.WARLORD);
                break;
            case 4:
                bot.getGameCharacter().specialEffect(bot, game, ARCHITECT);
                break;
            default:
            bot.getGameCharacter().specialEffect(bot, game, selectRandomKillableCharacter(game));
        }
    }


    public int shouldPickArchitect(Game game) {

        if (game.containsAvailableRole(ARCHITECT)) {
            return 0;
        }

        int districtCount = bot.getCity().size();
        int goldCount = bot.getGold();
        int cardCount = bot.getDistrictsInHand().size();

        if (architectIsOverpoweredFor(bot)) {
            return 1;
        }


        return 0;
    }

    private int shouldPickKing(Game game) {
        if (game.containsAvailableRole(KING) || game.getCrownOwner() == bot) {
            return 0;
        }
        return 1;
    }

    public int shouldPickAssassin(Game game) {
        if (game.containsAvailableRole(GameCharacterRole.ASSASSIN)) {
            return 0;
        }

        Player richestPlayer = game.getRichestPlayer();
        int playerWithMostDistrictsIndex = game.getCurrentPlayerIndexInRunningOrder(game.getPlayerWithMostDistricts());
        boolean thiefIsTaken = !game.containsAvailableRole(GameCharacterRole.THIEF);
        boolean warlordIsTaken = !game.containsAvailableRole(GameCharacterRole.WARLORD);
        boolean assassinIsTaken = !game.containsAvailableRole(GameCharacterRole.ASSASSIN);
        boolean kingIsTaken = !game.containsAvailableRole(GameCharacterRole.KING);
        boolean isCurrentPlayerAfterPlayerWithMostDistricts = (game.getCurrentPlayerIndexInRunningOrder(bot) > playerWithMostDistrictsIndex);

        Player firstPlayer = game.getPlayers().get(0);
        if (kingIsTaken && game.getPlayerWith6Districts() != null) {
            return 1;// Kill the king
        }

        if (IsRich(richestPlayer) && thiefIsTaken || couldWinThisTurn(game.getPlayerWithMostDistricts()) && assassinIsTaken && isCurrentPlayerAfterPlayerWithMostDistricts) { // If the player with the most districts has at least 6 districts, the assassin should be picked
            return 2;// Kill the thief
        }
        if (isAhead(game, bot) || (couldWinThisTurn(game.getPlayerWithMostDistricts()) && warlordIsTaken && isCurrentPlayerAfterPlayerWithMostDistricts)) { // If the player with the assassin is ahead, the assassin should be picked
            return 3; // Kill the warlord
        }
        if (architectIsOverpoweredIn(game.getPlayers()) && getRandomBotStyle() == BotStyle.SUSPICIOUS || firstPlayer == bot && getRandomBotStyle() == BotStyle.WISE) { // Aggressive play (bot soupçonneux)
            return 4; // Kill the architect
        }
        return 0;
    }

    private boolean isAhead(Game game, Player bot) {
        return bot.getCity().size() > game.averageCitySize();
    }

    private static boolean architectIsOverpoweredFor(Player player) { // If the richest player has at least 4 gold, at least one district in hand and at least 5 districts in his city, the architect should be picked
        return player.getGold() >= 4 && !player.getDistrictsInHand().isEmpty() && player.getCity().size() >= 5;
    }

    private boolean architectIsOverpoweredIn(List<Player> players) {
        for (Player player : players) {
            if (player.getGold() >= 4 && !player.getDistrictsInHand().isEmpty() && player.getCity().size() >= 5) {
                return true;
            }
        }
        return false;
    }

    private static boolean couldWinThisTurn(Player player) {
        return player != null
                && player.getCity().size() == 7;

    }

    private static boolean IsRich(Player player) {
        return player != null && player.getGold() > 6;
    }
}
