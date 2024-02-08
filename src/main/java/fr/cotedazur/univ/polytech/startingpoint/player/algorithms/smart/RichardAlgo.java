package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.List;
import java.util.Random;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.*;

public class RichardAlgo extends SmartAlgo {
    public RichardAlgo(){
        super();
        algoName = "Richard";
    }

    public enum BotStyle {
        WISE,
        SUSPICIOUS
    }

    private boolean shouldPickAssassinNextTurn = false;
    private boolean shouldKillBishop = false;
    private boolean shouldKillExceptWarlord = false;
    private boolean shouldKillMagician = false;

    BotStyle getRandomBotStyle() { // Randomly choose a bot style
        BotStyle[] styles = BotStyle.values();
        return styles[new Random().nextInt(styles.length)];
    }

    @Override
    public boolean collectGoldBeforeBuildChoice() {
        // Since Richard didn't specify any logic for this, he will always choose to collect gold after
        // building to get the money from the districts he built (if he has a money character)
        return false;
    }

    @Override
    public void chooseCharacterAlgorithm(Game game) {
        if (couldWinThisTurn(game.getPlayerWithMostDistricts()) && game.getPlayers().indexOf(game.getPlayerWithMostDistricts()) > 2 && game.getPlayers().indexOf(bot) < 2) {
            finalTurn(game);
            return;
        }

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

    private void finalTurn(Game game) {
        if (game.containsAvailableRoles(BISHOP, ASSASSIN, WARLORD) && bot == game.getPlayers().get(0)) {
            bot.chooseChar(game, WARLORD);
        } else if (game.containsAvailableRoles(BISHOP, ASSASSIN) && !game.containsAvailableRole(WARLORD) && bot == game.getPlayers().get(1)) {
            bot.chooseChar(game, ASSASSIN);
            shouldKillBishop = true;
        }
        if (game.containsAvailableRoles(ASSASSIN, WARLORD) && !game.containsAvailableRole(BISHOP) && bot == game.getPlayers().get(0)) {
            bot.chooseChar(game, ASSASSIN);
            shouldKillExceptWarlord = true;
        }
        if (game.containsAvailableRoles(ASSASSIN, BISHOP) && !game.containsAvailableRole(WARLORD) && bot == game.getPlayers().get(0)) {
            bot.chooseChar(game, ASSASSIN);
            if (game.getPlayers().get(1).getDistrictsInHand().size() > 4 && game.getPlayers().get(2).getDistrictsInHand().isEmpty()) {
                shouldKillMagician = true; //kill magician
            }
            //kill random
        } else if (bot == game.getPlayers().get(1) && game.getPlayers().get(1).getDistrictsInHand().size() < 4) {
            bot.chooseChar(game, MAGICIAN);

        }
        if (game.containsAvailableRoles(BISHOP, WARLORD) && !game.containsAvailableRole(ASSASSIN) && bot == game.getPlayers().get(0)) {
            bot.chooseChar(game, WARLORD);
        } else if (game.containsAvailableRole(BISHOP) && !game.containsAvailableRole(ASSASSIN) && !game.containsAvailableRole(WARLORD) && bot == game.getPlayers().get(1)) {
            bot.chooseChar(game, BISHOP);
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
        Player playerWith6Districts = game.getPlayerWith6Districts();
        Player playerWithLowestDistrictPrice = game.getPlayerWithLowestDistrictPrice();
        if (oneChanceOutOfTwo) { // Have 50% chance to decide to destroy a building of a random player or not
            if (playerWith6Districts == bot) { // If the bot has 6 districts, he will destroy a building of a random player
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
            if (playerWith6Districts != null) {
                bot.getGameCharacter().specialEffect(bot, game, playerWith6Districts, playerWith6Districts.getLowestDistrict().get());
                return;
            }
            if (game.getPlayerWithMostDistricts().getCity().getDistrictsBuilt().size() < 7) {
                bot.getGameCharacter().specialEffect(bot, game, game.getPlayerWithMostDistricts(), game.getPlayerWithMostDistricts().getLowestDistrict().get());
            }
            bot.getGameCharacter().specialEffect(bot, game, playerWithLowestDistrictPrice, playerWithLowestDistrictPrice.getLowestDistrict().get());
        }
    }

    @Override
    public void magicianAlgorithm(Game game) {
        if (bot.getDistrictsInHand().size() < 4) {
            if (game.getPlayerWithMostDistricts() != bot && couldWinThisTurn(game.getPlayerWithMostDistricts())) {
                bot.getGameCharacter().specialEffect(bot, game, game.getPlayerWithMostDistricts());
            }
            bot.getGameCharacter().specialEffect(game.getPlayerWithMostCardInHand(), game);
        }
    }

    @Override
    public void assassinAlgorithm(Game game) {
        if (shouldKillBishop) {
            bot.getGameCharacter().specialEffect(bot, game, BISHOP);
            shouldKillBishop = false;
        } else if (shouldKillExceptWarlord) {
            bot.getGameCharacter().specialEffect(bot, game, selectRandomKillableCharacterExcept(GameCharacterRole.WARLORD, game));
            shouldKillExceptWarlord = false;
        } else if (shouldKillMagician) {
            bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.MAGICIAN);
            shouldKillMagician = false;
        } else {
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
    }


    public int shouldPickArchitect(Game game) {

        if (!game.containsAvailableRole(ARCHITECT)) {
            return 0;
        }
        if (architectIsOverpoweredFor(bot)) {
            return 1;
        }

        return 0;
    }

    private int shouldPickKing(Game game) {
        if (!game.containsAvailableRole(KING) || game.getCrownOwner() == bot) {
            return 0;
        }
        return 1;
    }

    public int shouldPickAssassin(Game game) {
        if (!game.containsAvailableRole(GameCharacterRole.ASSASSIN)) {
            return 0;
        }

        Player richestPlayer = game.getRichestPlayer();
        int playerWithMostDistrictsIndex = game.getPlayers().indexOf(game.getPlayerWithMostDistricts());
        boolean thiefIsTaken = !game.containsAvailableRole(GameCharacterRole.THIEF);
        boolean warlordIsTaken = !game.containsAvailableRole(GameCharacterRole.WARLORD);
        boolean assassinIsTaken = !game.containsAvailableRole(GameCharacterRole.ASSASSIN);
        boolean kingIsTaken = !game.containsAvailableRole(GameCharacterRole.KING);
        boolean isCurrentPlayerAfterPlayerWithMostDistricts = (game.getPlayers().indexOf(bot) > playerWithMostDistrictsIndex);

        Player firstPlayer = game.getPlayers().get(0);
        if (kingIsTaken && game.getPlayers().get(0) != bot && game.getPlayerWith6Districts() != null) {
            return 1;// Kill the king
        }

        if (isRich(richestPlayer) && thiefIsTaken || couldWinThisTurn(game.getPlayerWithMostDistricts()) && assassinIsTaken && isCurrentPlayerAfterPlayerWithMostDistricts) { // If the player with the most districts has at least 6 districts, the assassin should be picked
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

    private static boolean isRich(Player player) {
        return player != null && player.getGold() > 6;
    }
}
