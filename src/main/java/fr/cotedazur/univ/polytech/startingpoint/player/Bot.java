package fr.cotedazur.univ.polytech.startingpoint.player;

import fr.cotedazur.univ.polytech.startingpoint.ActionManager;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.BaseAlgo;

import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

/**
 * Classe représentant un bot dans le jeu.
 */
public class Bot extends Player {

    private BaseAlgo botAlgo;

    /**
     * Retourne l'algorithme du bot.
     *
     * @return l'algorithme du bot.
     */
    public BaseAlgo getBotAlgo() {
        return botAlgo;
    }

    /**
     * Constructeur de la classe Bot.
     * @param name le nom du bot.
     * @param algo l'algorithme du bot.
     */
    public Bot(String name, BaseAlgo algo) {
        super(name);
        this.botAlgo = algo;
        botAlgo.setBot(this);
    }

    /**
     * Constructeur de la classe Bot pour les tests.
     * @param name le nom du bot.
     */
    public Bot(String name){ //for tests
        super(name);
    }

    /**
     * Vérifie si le bot peut construire un quartier ce tour.
     * @return vrai si le bot peut construire un quartier ce tour, faux sinon.
     */
    public boolean canBuildDistrictThisTurn() { // Checks If a district in Hand can be built with +2 gold
        for (District dist : getDistrictsInHand()) {
            if (dist.getPrice() <= this.getGold() + 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si un personnage est dans une liste.
     * @param cha la liste de personnages.
     * @param askedChar le personnage demandé.
     * @return vrai si le personnage est dans la liste, faux sinon.
     */
    public boolean isCharInList(List<GameCharacter> cha, GameCharacterRole askedChar) {
        for (GameCharacter temp : cha) {
            if (temp.getRole().equals(askedChar)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne un personnage dans une liste.
     * @param cha la liste de personnages.
     * @param askedChar le personnage demandé.
     * @return le personnage s'il est dans la liste, sinon vide.
     */
    public Optional<GameCharacter> getCharInList(List<GameCharacter> cha, GameCharacterRole askedChar) {
        for (GameCharacter gameCharacter : cha) {
            if (gameCharacter.getRole().equals(askedChar)) {
                return Optional.of(gameCharacter);
            }
        }
        return Optional.empty();
    }

    /**
     * Choisis un personnage pour le bot.
     * @param game l'état actuel du jeu.
     * @param askedChar le personnage demandé.
     */
    public void chooseChar(Game game, GameCharacterRole askedChar) {
        Optional<GameCharacter> chosenCharacter = getCharInList(game.getAvailableChars(), askedChar);
        if (chosenCharacter.isEmpty()) {
            return;
        }

        game.printAvailableCharacters();
        setGameCharacter(chosenCharacter.get());
        game.removeAvailableChar(chosenCharacter.get());
        String chosenCharMessage = COLOR_BLUE + this.getName() + " a choisi " + chosenCharacter.get().getRole().toStringLeOrLLowerCase() + COLOR_RESET;
        LOGGER.info(chosenCharMessage);
    }

    /**
     * Joue un tour de jeu pour le bot.
     * @param game l'état actuel du jeu.
     * @param gameState l'état du jeu.
     */
    @Override
    public void play(Game game, GameState gameState) {
        boolean collectGoldBeforeBuild = botAlgo.collectGoldBeforeBuildChoice();
        ActionManager.startOfTurn(game, this);

        if (collectGoldBeforeBuild) {
            ActionManager.collectGold(this);
        }

        ActionManager.applySpecialCardsEffect(game, this);
        botAlgo.charAlgorithmsManager(game);
        botAlgo.buildOrNot(gameState);

        if (!collectGoldBeforeBuild) {
            ActionManager.collectGold(this);
        }
    }

    /**
     * Vérifie si deux bots sont égaux en comparant leurs noms.
     * @param o l'objet à comparer avec le bot actuel.
     * @return vrai si les deux bots sont égaux, faux sinon.
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * Retourne le code de hachage du bot.
     * @return le code de hachage du bot.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}