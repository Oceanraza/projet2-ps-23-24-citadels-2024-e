package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.List;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.ARCHITECT;
import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.ASSASSIN;

/**
 * Cette classe représente l'algorithme du bot Einstein
 * Elle contient la logique des actions du bot
 */
public class EinsteinAlgo extends SmartAlgo {
    /**
     * Constructeur de la classe EinsteinAlgo
     */
    public EinsteinAlgo() {
        super();
        algoName = "Einstein";
    }

    /**
     * Choix de l'assassin en fonction de l'algorithme
     *
     * @param game           Le jeu en cours
     * @param availableChars Les personnages disponibles
     * @return true si l'assassin est choisi, false sinon
     */
    public boolean chooseAssassinAlgorithm(Game game, List<GameCharacter> availableChars) {
        if ((bot.getCity().getDistrictsBuilt().size() >= 7) && (bot.canBuildDistrictThisTurn()) && (bot.isCharInList(availableChars, ASSASSIN))) {
            bot.chooseChar(game, ASSASSIN);
            return true;
        }
        return false;
    }

    /**
     * Choix du personnage qui rapporte le plus d'argent en fonction de l'algorithme
     * @param game Le jeu en cours
     * @param availableChars Les personnages disponibles
     */
    public void chooseMoneyCharacterAlgorithm(Game game, List<GameCharacter> availableChars) {
        GameCharacterRole chosenChar = availableChars.get(0).getRole();
        int numberOfDistrictByColor;
        int goldCollectedWithDistrictColor = 0;

        for (GameCharacter cha : availableChars) {
            // On compare uniquement les personnages qui collectent de l'or en fonction de leurs quartiers
            if (cha.getColor() != null) {
                numberOfDistrictByColor = bot.getNumberOfDistrictsByColor().get(cha.getColor());
                if (numberOfDistrictByColor > goldCollectedWithDistrictColor) {
                    goldCollectedWithDistrictColor = numberOfDistrictByColor;
                    chosenChar = cha.getRole();
                }
            }
        }
        bot.chooseChar(game, chosenChar);
    }

    /**
     * Choix du personnage en fonction de l'algorithme
     * @param game Le jeu en cours
     */
    @Override
    public void chooseCharacterAlgorithm(Game game) {
        List<GameCharacter> availableChars = game.getAvailableChars();
        // Si le bot peut construire son 8ème quartier au prochain tour, il choisira l'assassin
        // Ainsi, il ne sera pas tué
        if (!chooseAssassinAlgorithm(game, availableChars)) {
            // Si la main du bot est vide, il choisit le magicien s'il lui donne plus de cartes que l'architecte
            if ((bot.getDistrictsInHand().isEmpty()) && ((bot.isCharInList(availableChars, GameCharacterRole.MAGICIAN)) || (bot.isCharInList(availableChars, ARCHITECT)))) {
                if ((bot.isCharInList(availableChars, GameCharacterRole.MAGICIAN)) && (Utils.getHighestNumberOfCardsInHand(game.getPlayers(), this.bot) > 2)) {
                    bot.chooseChar(game, GameCharacterRole.MAGICIAN);
                } else if (bot.isCharInList(availableChars, GameCharacterRole.ARCHITECT)) {
                    bot.chooseChar(game, GameCharacterRole.ARCHITECT);
                }
            } else {
                // Si le bot n'a pas de moyen immédiat de gagner, il choisira simplement le personnage qui lui donne le plus d'or
                chooseMoneyCharacterAlgorithm(game, availableChars);
            }
        }
        if (bot.getGameCharacter() == null) { // Méthode à l'épreuve des échecs
            bot.chooseChar(game, availableChars.get(Utils.generateRandomNumber(availableChars.size())).getRole());
        }
    }

    /**
     * Cet algorithme est utilisé pour détruire le quartier le moins cher du joueur ayant le plus de points
     * @param game Le jeu en cours
     */
    @Override
    public void warlordAlgorithm(Game game) {
        List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
        playerList.remove(bot);
        for (Player targetedPlayer : playerList) {
            if (!targetedPlayer.getGameCharacter().getRole().equals(GameCharacterRole.BISHOP)) { // ne cible pas l'évêque car il est immunisé contre le seigneur de guerre
                targetedPlayer.getLowestDistrictBuilt().ifPresent(district -> {
                    if (Utils.canDestroyDistrict(district, bot)) {
                        bot.getGameCharacter().specialEffect(bot, game, targetedPlayer, district);
                        lowestDistrictHasBeenFound();
                    }
                });
                if (lowestDistrictFound) {
                    break;
                }
            }
        }
    }

    // Notez que cet algorithme n'utilise pas la deuxième partie du magicien, la trouvant inutile par rapport aux autres cartes
    @Override
    public void magicianAlgorithm(Game game) {
        List<Player> playerList = game.getSortedPlayersByScore();
        playerList.remove(bot);
        Player chosenPlayer = bot;
        for (Player p : playerList) {
            if (p.getDistrictsInHand().size() > chosenPlayer.getDistrictsInHand().size()) {
                chosenPlayer = p; // Il prend la main du joueur avec le plus de cartes
            }
        }
        boolean switching = true;
        bot.getGameCharacter().specialEffect(bot, game, switching, chosenPlayer);
    }

    /**
     * Choix du cimetière
     * @return true
     */
    @Override
    public boolean graveyardChoice() {
        return true;
    }
}