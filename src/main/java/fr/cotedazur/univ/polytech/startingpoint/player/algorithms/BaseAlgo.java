package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.LOGGER;

/**
 * Classe de base pour les algorithmes des bots.
 * Cette classe est abstraite et doit être étendue par les classes d'algorithmes spécifiques.
 */
public abstract class BaseAlgo {
    protected boolean oneChanceOutOfTwo = Utils.generateRandomNumber(2) == 0;

    protected Bot bot;
    protected String algoName;

    /**
     * Constructeur de la classe BaseAlgo.
     */
    protected BaseAlgo() {
    }

    /**
     * Définit le bot pour cet algorithme.
     *
     * @param player Le bot à définir.
     */
    public void setBot(Bot player) {
        this.bot = player;
    }

    /**
     * Obtient le nom de l'algorithme.
     * @return Le nom de l'algorithme.
     */
    public String getAlgoName() {
        return algoName;
    }

    /**
     * Gère les algorithmes de caractères pour le bot.
     * @param game L'état actuel du jeu.
     */
    public void charAlgorithmsManager(Game game) {
        switch (bot.getCharacterName()) {
            case ("Condottiere"):
                warlordAlgorithm(game);
                break;
            case ("Roi"):
                kingAlgorithm(game);
                break;
            case ("Assassin"):
                assassinAlgorithm(game);
                break;
            case ("Magicien"):
                magicianAlgorithm(game);
                break;
            case ("Voleur"):
                thiefAlgorithm(game);
                break;
            default:
                break;
        }
    }

    /**
     * Le bot choisit une carte parmi trois.
     * @param game L'état actuel du jeu.
     * @param threeCards Les trois cartes parmi lesquelles choisir.
     */
    public void botChoosesCard(Game game, List<District> threeCards) {
        District chosenCard = chooseCard(threeCards);
        threeCards.remove(chosenCard); // Remove the chosen card from the list of three cards
        for (District card : threeCards) {
            this.bot.removeFromHandAndPutInDeck(game.getDeck(), card);
        }
        String drawMessage = bot.getName() + " pioche le " + chosenCard;
        LOGGER.info(drawMessage);
        bot.addDistrictInHand(chosenCard);
    }

    /**
     * L'algorithme du voleur pour le bot.
     * @param game L'état actuel du jeu.
     */
    public void thiefAlgorithm(Game game) {
        int numberOfTargets;
        int indexPlayerStolen;
        GameCharacterRole targetedCharacter;

        // Choose a random character and steal him
        numberOfTargets = game.getCharactersThatCanBeStolen().size();
        indexPlayerStolen = Utils.generateRandomNumber(numberOfTargets);
        targetedCharacter = game.getCharactersThatCanBeStolen().get(indexPlayerStolen).getRole();

        bot.getGameCharacter().specialEffect(bot, game, targetedCharacter);
    }

    /**
     * L'algorithme du roi pour le bot.
     * @param game L'état actuel du jeu.
     */
    public void kingAlgorithm(Game game) {
        bot.getGameCharacter().specialEffect(bot, game);
    }

    /**
     * Décide si le bot doit construire ou non.
     * @param gameState L'état actuel du jeu.
     */
    public void buildOrNot(GameState gameState) { //Builds districts in the bot's hand
        int builtThisTurn = 0;
        ArrayList<District> tempHand = new ArrayList<>(bot.getDistrictsInHand()); //Need to create a deep copy to avoid concurrent modification
        for (District district : tempHand) {

            if (bot.buildDistrict(district, gameState)) {
                builtThisTurn++;
                if ((!bot.getCharacterName().equals("Architecte")) || (builtThisTurn == 3)) {
                    break;
                }
            }
        }
    }

    /**
     * Obtient un booléen aléatoire.
     * @return Un booléen aléatoire.
     */
    protected boolean getRandomBoolean() {
        return Utils.generateRandomNumber(2) == 0;
    }

    /**
     * Méthode abstraite pour choisir entre prendre de l'or ou piocher une carte en début de tour.
     * @return 2 pour piocher une carte, 1 pour prendre de l'or.
     */
    public abstract int startOfTurnChoice();

    /**
     * Méthode abstraite pour choisir un personnage.
     * @param game L'état actuel du jeu.
     */
    public abstract void chooseCharacterAlgorithm(Game game);

    /**
     * Méthode abstraite pour l'algorithme du condottiere.
     * @param game L'état actuel du jeu.
     */
    public abstract void warlordAlgorithm(Game game);

    /**
     * Méthode abstraite pour l'algorithme du magicien.
     * @param game L'état actuel du jeu.
     */
    public abstract void magicianAlgorithm(Game game);

    /**
     * Méthode abstraite pour l'algorithme de l'assassin.
     * @param game L'état actuel du jeu.
     */
    public abstract void assassinAlgorithm(Game game);

    /**
     * Méthode abstraite pour l'algorithme du quartier chassé.
     * @param huntedQuarter Le quartier chassé.
     */
    public abstract void huntedQuarterAlgorithm(District huntedQuarter);

    /**
     * Méthode abstraite pour décider si le bot doit utiliser l'effet de la manufacture.
     * @return true si le bot doit utiliser l'effet de la manufacture, false sinon.
     */
    public abstract boolean manufactureChoice();

    /**
     * Méthode abstraite pour décider si le bot doit utiliser l'effet du cimetière.
     * @return true si le bot doit utiliser l'effet du cimetière, false sinon.
     */
    public abstract boolean graveyardChoice();

    /**
     * Méthode abstraite pour choisir une carte à défausser avec l'effet du laboratoire.
     * @return Un Optional contenant la carte à défausser, ou un Optional vide si aucune carte ne doit être défaussée.
     */
    public abstract Optional<District> laboratoryChoice();

    /**
     * Méthode abstraite pour choisir une carte parmi une liste de cartes.
     * @param threeCards La liste de cartes parmi lesquelles choisir.
     * @return La carte choisie.
     */
    public abstract District chooseCard(List<District> threeCards);

    /**
     * Méthode abstraite pour décider si le bot doit collecter de l'or avant de construire.
     * @return true si le bot doit collecter de l'or avant de construire, false sinon.
     */
    public abstract boolean collectGoldBeforeBuildChoice();
}