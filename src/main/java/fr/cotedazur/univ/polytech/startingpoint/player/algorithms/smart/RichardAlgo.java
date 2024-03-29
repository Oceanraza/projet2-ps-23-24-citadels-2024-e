package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.*;

/**
 * Cette classe représente l'algorithme du bot Richard.
 * Elle contient la logique des actions du bot.
 */
public class RichardAlgo extends SmartAlgo {
    /**
     * Constructeur de la classe RichardAlgo.
     */
    public RichardAlgo() {
        super();
        algoName = "Richard";
    }

    /**
     * Enumération représentant le style du bot.
     */
    public enum BotStyle {
        AGGRESSIVE,
        BUILDER,
        OPPORTUNIST
    }

    private boolean shouldPickAssassinNextTurn = false;
    private boolean shouldKillBishop = false;
    private boolean shouldKillExceptWarlord = false;
    private boolean shouldKillMagician = false;

    private static final BotStyle BOT_STYLE = getRandomBotStyle();

    /**
     * Méthode pour obtenir un style de bot aléatoire.
     *
     * @return Un style de bot aléatoire.
     */
    public static BotStyle getRandomBotStyle() {
        BotStyle[] styles = BotStyle.values();
        return styles[Utils.generateRandomNumber(styles.length)];
    }

    /**
     * Méthode pour obtenir le style du bot.
     * @return Le style du bot.
     */
    public BotStyle getBotStyle() {
        return BOT_STYLE;
    }

    /**
     * Méthode pour choisir un personnage en fonction de l'algorithme.
     * @param game L'état actuel du jeu.
     */
    @Override
    public void chooseCharacterAlgorithm(Game game) {
        if (couldWinThisTurn(game.getPlayerWithMostDistricts()) && game.getPlayers().indexOf(game.getPlayerWithMostDistricts()) > 2 && game.getPlayers().indexOf(bot) < 2) {
            finalTurn(game);
        } else if (shouldPickAssassinNextTurn && game.containsAvailableRole(ASSASSIN)) {
            bot.chooseChar(game, ASSASSIN);
        }

        //Déclenché en fin de partie
        else if (game.getPlayerWith6Districts() != null) { // I
            finishOrCounter(game);
        } else {
            //Algo durant la partie
            if (shouldPickArchitect(game) > 0) {
                bot.chooseChar(game, ARCHITECT);
            } else if (shouldPickKing(game) > 0) {
                bot.chooseChar(game, KING);
            } else {
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
        }
        // If no character has been chosen, the bot will choose a random character in the available characters
        if (bot.getGameCharacter() == null) {
            bot.chooseChar(game, game.getAvailableChars().get(Utils.generateRandomNumber(game.getAvailableChars().size())).getRole());
        }
    }

    private void finalTurn(Game game) {
        if (isFirstBotAndRolesAvailable(game, BISHOP, ASSASSIN, WARLORD)) {
            bot.chooseChar(game, WARLORD);
            return;
        } else if (game.containsAvailableRoles(BISHOP, ASSASSIN) && !game.containsAvailableRole(WARLORD) && bot == game.getPlayers().get(1)) {
            bot.chooseChar(game, ASSASSIN);
            shouldKillBishop = true;
            return;
        }
        if (isSecondBotAndRolesAvailable(game, ASSASSIN, WARLORD) && !game.containsAvailableRole(BISHOP)) {
            bot.chooseChar(game, ASSASSIN);
            shouldKillExceptWarlord = true;
            return;
        }
        if (isFirstBotAndRolesAvailable(game, ASSASSIN, BISHOP) && !game.containsAvailableRole(WARLORD)) {
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

    private boolean isFirstBotAndRolesAvailable(Game game, GameCharacterRole... roles) {
        return game.containsAvailableRoles(roles) && bot == game.getPlayers().get(0);
    }

    private boolean isSecondBotAndRolesAvailable(Game game, GameCharacterRole... roles) {
        return game.containsAvailableRoles(roles) && bot == game.getPlayers().get(1);
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

    /**
     * Méthode pour choisir un personnage en fonction de l'algorithme du condottière.
     * @param game L'état actuel du jeu.
     */
    @Override
    public void warlordAlgorithm(Game game) {
        Player playerWith6Districts = game.getPlayerWith6Districts();
        Optional<Player> playerWithLowestDistrictPrice = game.getPlayerWithLowestDistrictPrice();
        if (playerWithLowestDistrictPrice.isEmpty()) {
            return;
        }
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
                playerWith6Districts.getLowestDistrictBuilt().ifPresent(district -> bot.getGameCharacter().specialEffect(bot, game, playerWith6Districts, district));
                return;
            }
            if (game.getPlayerWithMostDistricts().getCity().getDistrictsBuilt().size() < 7) {
                Player playerWithMostDistricts = game.getPlayerWithMostDistricts();
                playerWithMostDistricts.getLowestDistrictBuilt().ifPresent(district -> bot.getGameCharacter().specialEffect(bot, game, playerWithMostDistricts, district));
            }
            playerWithLowestDistrictPrice.get()
                    .getLowestDistrictBuilt()
                    .ifPresent(district -> bot.getGameCharacter().specialEffect(bot, game, playerWithLowestDistrictPrice.get(), district));
        }
    }

    /**
     * Méthode pour choisir un personnage en fonction de l'algorithme du magicien.
     * @param game L'état actuel du jeu.
     */
    @Override
    public void magicianAlgorithm(Game game) {
        if (bot.getDistrictsInHand().size() < 4) {
            if (game.getPlayerWithMostDistricts() != bot && couldWinThisTurn(game.getPlayerWithMostDistricts())) {
                bot.getGameCharacter().specialEffect(bot, game, true, game.getPlayerWithMostDistricts()); // If the bot has less than 4 cards in hand, he will swap his hand with the player with the most districts
                return;
            }
            bot.getGameCharacter().specialEffect(bot, game, true, game.getPlayerWithMostCardInHand());
            return;
        }
        bot.getGameCharacter().specialEffect(bot, game, false);// If the bot has less than 4 cards in hand, he will swap his hand with the deck
    }

    /**
     * Méthode pour choisir un personnage en fonction de l'algorithme de l'assassin.
     * @param game L'état actuel du jeu.
     */
    @Override
    public void assassinAlgorithm(Game game) {
        if (shouldKillBishop) {
            bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.WARLORD);
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
                    bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.KING);
                    break;
                case 2:
                    bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.THIEF);
                    break;
                case 3:
                    bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.WARLORD);
                    break;
                case 4:
                    bot.getGameCharacter().specialEffect(bot, game, GameCharacterRole.ARCHITECT);
                    break;
                case 5:
                    bot.getGameCharacter().specialEffect(bot, game, MAGICIAN);
                    break;
                default:
                    super.assassinAlgorithm(game);
            }
        }
    }

    /**
     * Méthode pour déterminer si le bot doit choisir le roi.
     * @param game L'état actuel du jeu.
     * @return 1 si le bot doit choisir le roi, 0 sinon.
     */
    private int shouldPickKing(Game game) {
        if (!game.containsAvailableRole(KING) || game.getCrownOwner() == bot) {
            return 0;
        }
        return 1;
    }

    /**
     * Méthode pour déterminer si le bot doit choisir l'architecte.
     * @param game L'état actuel du jeu.
     * @return 1 si le bot doit choisir l'architecte, 0 sinon.
     */
    public int shouldPickArchitect(Game game) {
        int goldCount = bot.getGold();

        if (!game.containsAvailableRole(ARCHITECT)) {
            return 0;
        }
        if (architectIsOverpoweredFor(bot) || goldCount > 8) {
            return 1;
        }
        return 0;
    }

    /**
     * Méthode pour déterminer si le bot doit choisir l'assassin.
     * @param game L'état actuel du jeu.
     * @return 1, 2, 3, 4 ou 5 si le bot doit choisir l'assassin, 0 sinon.
     */
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
        Optional<Player> playerWithNoDistrictInHand = onePlayerHasNoDistrictInHand(game.getPlayers());

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
        if (architectIsOverpoweredIn(game.getPlayers()) && BOT_STYLE == BotStyle.BUILDER || firstPlayer == bot && BOT_STYLE == BotStyle.OPPORTUNIST) { // Aggressive play (bot soupçonneux)
            return 4; // Kill the architect
        }
        if (playerWithNoDistrictInHand.isPresent() && bot.getDistrictsInHand().size() > 4) {
            return 5; // Kill the magician
        }
        return 0;
    }

    /**
     * Méthode pour déterminer si le bot doit choisir le cimetière.
     * @return Toujours vrai dans cette implémentation.
     */
    @Override
    public boolean graveyardChoice() {
        return true;
    }

    /**
     * Méthode pour choisir un personnage pour le bot de type constructeur.
     * @param game L'état actuel du jeu.
     */
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

    /**
     * Méthode pour choisir un personnage en fonction de l'ordre donné.
     * @param game L'état actuel du jeu.
     * @param gameCharacterRoles L'ordre des personnages à choisir.
     */
    private void chooseCharacterIfExistsInOrder(Game game, List<GameCharacterRole> gameCharacterRoles) {
        for(GameCharacterRole gameCharacterRole : gameCharacterRoles) {
            if (game.containsAvailableRole(gameCharacterRole)) {
                bot.chooseChar(game, gameCharacterRole);
                return;
            }
        }
        bot.chooseChar(game, game.getAvailableChars().get(0).getRole()); // If none of the characters are available, the bot will choose the first character
    }

    /**
     * Méthode pour choisir un personnage pour le bot de type agressif.
     * @param game L'état actuel du jeu.
     */
    private void chooseAggressiveChar(Game game) {
        List<GameCharacterRole> choosingOrder = new ArrayList<>(List.of(ASSASSIN, WARLORD, MAGICIAN, THIEF));
        chooseCharacterIfExistsInOrder(game, choosingOrder);
    }

    /**
     * Méthode pour choisir un personnage pour le bot de type opportuniste.
     * @param game L'état actuel du jeu.
     */
    private void chooseOpportunistChar(Game game) {
        List<GameCharacterRole> choosingOrder = new ArrayList<>(List.of(BISHOP, WARLORD, THIEF));
        chooseCharacterIfExistsInOrder(game, choosingOrder);
    }

    /**
     * Méthode pour déterminer si le bot est en avance dans le jeu.
     * @param game L'état actuel du jeu.
     * @param bot Le bot à vérifier.
     * @return true si le bot est en avance, false sinon.
     */
    private boolean isAhead(Game game, Player bot) {
        return bot.getCity().size() > game.averageCitySize();
    }

    /**
     * Méthode pour déterminer si l'architecte est surpuissant pour un joueur donné.
     * @param player Le joueur à vérifier.
     * @return true si l'architecte est surpuissant pour le joueur, false sinon.
     */
    private static boolean architectIsOverpoweredFor(Player player) { // If the richest player has at least 4 gold, at least one district in hand and at least 5 districts in his city, the architect should be picked
        return player.getGold() >= 4 && !player.getDistrictsInHand().isEmpty() && player.getCity().size() >= 5;
    }

    /**
     * Méthode pour déterminer si l'architecte est surpuissant pour une liste de joueurs.
     * @param players La liste de joueurs à vérifier.
     * @return true si l'architecte est surpuissant pour au moins un joueur, false sinon.
     */
    private boolean architectIsOverpoweredIn(List<Player> players) {
        for (Player player : players) {
            if (player.getGold() >= 4 && !player.getDistrictsInHand().isEmpty() && player.getCity().size() >= 5) {
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode pour déterminer si un joueur pourrait gagner ce tour.
     * @param player Le joueur à vérifier.
     * @return true si le joueur pourrait gagner ce tour, false sinon.
     */
    private static boolean couldWinThisTurn(Player player) {
        return player != null
                && player.getCity().size() == 7;

    }

    /**
     * Méthode pour déterminer si un joueur est riche.
     * @param player Le joueur à vérifier.
     * @return true si le joueur est riche, false sinon.
     */
    private static boolean isRich(Player player) {
        return player != null && player.getGold() > 6;
    }

    /**
     * Méthode pour trouver un joueur qui n'a pas de quartier en main.
     * @param players La liste de joueurs à vérifier.
     * @return Un Optional contenant le joueur trouvé, ou un Optional vide si aucun joueur n'a été trouvé.
     */
    private static Optional<Player> onePlayerHasNoDistrictInHand(List<Player> players) {
        for(Player player: players) {
            if (player.getDistrictsInHand().isEmpty()) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    /**
     * Méthode pour sélectionner un personnage tuable aléatoire, sauf un personnage spécifique.
     * @param gameCharacterRole Le rôle du personnage à exclure.
     * @param game L'état actuel du jeu.
     * @return Le rôle du personnage tuable sélectionné, ou null si aucun personnage tuable n'a été trouvé.
     */
    public GameCharacterRole selectRandomKillableCharacterExcept(GameCharacterRole gameCharacterRole, Game game) {
        List<GameCharacter> killableCharacters = new ArrayList<>(game.getKillableCharacters());
        killableCharacters.removeIf(character -> character.getRole().equals(gameCharacterRole));

        if (killableCharacters.isEmpty()) {
            return null;
        }

        int randomIndex = Utils.generateRandomNumber(killableCharacters.size());
        return killableCharacters.get(randomIndex).getRole();
    }
}