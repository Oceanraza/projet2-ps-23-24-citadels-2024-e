package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.*;

public class RichardAlgo extends SmartAlgo {

    public enum BotStyle {
        AGGRESSIVE,
        BUILDER,
        OPPORTUNIST
    }

    private boolean shouldPickAssassinNextTurn = false;

    private static final BotStyle BOT_STYLE = getRandomBotStyle();

    public BotStyle getBotStyle() {
        return BOT_STYLE;
    }

    private static BotStyle getRandomBotStyle() { // Randomly choose a bot style
        BotStyle[] styles = BotStyle.values();
        return styles[Utils.generateRandomNumber(styles.length)];
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
        } else if (shouldPickMagician(game) > 0) {
            bot.chooseChar(game, MAGICIAN);
        }

        // If none of the best strategy is chosen, use the strategy related to the bot style
        switch (getBotStyle()) {
            case BUILDER:
                chooseBuilderChar(game);
                break;
            case AGGRESSIVE:
                chooseAggressiveChar(game);
                break;
            case OPPORTUNIST:
                chooseOpportunistChar(game);
                break;
            default:
                break;
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
                bot.getGameCharacter().specialEffect(bot, game, THIEF);
                break;
            case 3:
                bot.getGameCharacter().specialEffect(bot, game, WARLORD);
                break;
            case 4:
                bot.getGameCharacter().specialEffect(bot, game, ARCHITECT);
                break;
            case 5:
                bot.getGameCharacter().specialEffect(bot, game, MAGICIAN);
                break;
            default:
            bot.getGameCharacter().specialEffect(bot, game, selectRandomKillableCharacter(game));
        }
    }

    public int shouldPickArchitect(Game game) {
        int goldCount = bot.getGold();

        if (!game.containsAvailableRole(ARCHITECT)) {
            return 0;
        }
        if (architectIsOverpoweredFor(bot) || goldCount > 6) {
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
        Optional<Player> playerWithNoDistrictInHand = onePlayerHasNoDistrictInHand(game.getPlayers());

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
        if (architectIsOverpoweredIn(game.getPlayers()) && BOT_STYLE == BotStyle.BUILDER || firstPlayer == bot && BOT_STYLE == BotStyle.OPPORTUNIST) { // Aggressive play (bot soupçonneux)
            return 4; // Kill the architect
        }
        if (playerWithNoDistrictInHand.isPresent() && bot.getDistrictsInHand().size() > 4) {
            return 5; // Kill the magician
        }
        return 0;
    }

    private int shouldPickMagician(Game game) {
        if (game.containsAvailableRole(MAGICIAN) && bot.getDistrictsInHand().isEmpty()) {
            return 1;
        }
        return 0;
    }

    private void chooseBuilderChar(Game game) {
        int nobleDistrictsBuilt = bot.getNumberOfDistrictsByColor().get(DistrictColor.NOBLE);
        int tradeDistrictsBuilt = bot.getNumberOfDistrictsByColor().get(DistrictColor.TRADE);

        if(game.containsAvailableRole(KING) && game.getCrownOwner() != bot && nobleDistrictsBuilt >= tradeDistrictsBuilt) {
            bot.chooseChar(game, KING);
        } else if(game.containsAvailableRole(ARCHITECT) && bot.getGold() > 5) {
            bot.chooseChar(game, ARCHITECT);
        } else if(game.containsAvailableRole(MERCHANT)) {
            bot.chooseChar(game, MERCHANT);
        } else {
            bot.chooseChar(game, game.getAvailableChars().get(0).getRole()); // If none of the characters are available, the bot will choose the first character
        }
    }

    private void chooseAggressiveChar(Game game) {
        // Strat :assassin, condottiere, magicien, voleur.
        if (game.containsAvailableRole(ASSASSIN)) {
            bot.chooseChar(game, ASSASSIN);
        } else if (game.containsAvailableRole(WARLORD)) {
            bot.chooseChar(game, WARLORD);
        } else if (game.containsAvailableRole(MAGICIAN)) {
            bot.chooseChar(game, MAGICIAN);
        } else if (game.containsAvailableRole(THIEF)) {
            bot.chooseChar(game, THIEF);
        } else {
            bot.chooseChar(game, game.getAvailableChars().get(0).getRole()); // If none of the characters are available, the bot will choose the first character
        }
    }

    private void chooseOpportunistChar(Game game) {
        // Srat : évêque, condottiere, voleur
        if (game.containsAvailableRole(BISHOP)) {
            bot.chooseChar(game, BISHOP);
        } else if (game.containsAvailableRole(WARLORD)) {
            bot.chooseChar(game, WARLORD);
        } else if (game.containsAvailableRole(THIEF)) {
            bot.chooseChar(game, THIEF);
        } else {
            bot.chooseChar(game, game.getAvailableChars().get(0).getRole()); // If none of the characters are available, the bot will choose the first character
        }
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

    private static Optional<Player> onePlayerHasNoDistrictInHand(List<Player> players) {
        for(Player player: players) {
            if (player.getDistrictsInHand().isEmpty()) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
}
