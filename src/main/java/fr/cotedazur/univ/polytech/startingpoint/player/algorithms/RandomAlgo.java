package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

/**
 * Cette classe représente l'algorithme aléatoire
 * Elle contient l'algorithme aléatoire pour le bot
 */
public class RandomAlgo extends BaseAlgo {
    /**
     * Constructeur de la classe RandomAlgo.
     */
    public RandomAlgo() {
        super();
        algoName = "Random";
    }

    /**
     * Méthode pour choisir entre prendre de l'or ou piocher une carte en début de tour.
     *
     * @return 2 pour piocher une carte, 1 pour prendre de l'or.
     */
    @Override
    public int startOfTurnChoice() {
        if (getRandomBoolean()) {
            return 1; // Prendre 2 pièces d'or
        }
        return 2; // Piocher une carte
    }

    /**
     * Méthode pour décider si le bot doit collecter de l'or avant de construire.
     * @return true si le bot doit collecter de l'or avant de construire, false sinon.
     */
    @Override
    public boolean collectGoldBeforeBuildChoice() {
        return getRandomBoolean();
    }

    /**
     * Méthode pour choisir un personnage de manière aléatoire.
     * @param game L'état actuel du jeu.
     */
    @Override
    public void chooseCharacterAlgorithm(Game game) {
        List<GameCharacter> availableChars = game.getAvailableChars();
        GameCharacter chosenChar = game.getAvailableChars().get(Utils.generateRandomNumber(availableChars.size()));
        bot.chooseChar(game, chosenChar.getRole());
    }

    /**
     * Méthode pour choisir de détruire un bâtiment d'un joueur aléatoire ou non.
     * @param game L'état actuel du jeu.
     */
    @Override
    public void warlordAlgorithm(Game game) {
        if (getRandomBoolean()) { // Avoir 50% de chance de décider de détruire un bâtiment d'un joueur aléatoire ou non
            List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
            playerList.remove(bot);
            Collections.shuffle(playerList);
            for (Player targetedPlayer : playerList) {
                List<District> allDistricts = targetedPlayer.getCity().getDistrictsBuilt();
                Collections.shuffle(allDistricts);
                for (District d : allDistricts) {
                    if (Utils.canDestroyDistrict(d, bot)) {
                        bot.getGameCharacter().specialEffect(bot, game, targetedPlayer, d);
                        return;
                    }
                }
            }
        } else {
            LOGGER.info(COLOR_RED + "Il ne détruit aucun quartier" + COLOR_RESET);
        }
    }

    /**
     * Méthode pour choisir d'assassiner un personnage ou non.
     * @param game L'état actuel du jeu.
     */
    @Override
    public void assassinAlgorithm(Game game) {
        if (getRandomBoolean()) { // a 50% de chance de décider d'assassiner un personnage
            int numberOfTargets;
            int indexPlayerKilled;
            GameCharacterRole targetedCharacter;

            // Choisi un personnage aléatoire et le tue
            numberOfTargets = game.getKillableCharacters().size();
            indexPlayerKilled = Utils.generateRandomNumber(numberOfTargets);
            targetedCharacter = game.getKillableCharacters().get(indexPlayerKilled).getRole();

            bot.getGameCharacter().specialEffect(bot, game, targetedCharacter);
        } else {
            LOGGER.info(COLOR_RED + "Il n'assassine personne" + COLOR_RESET);
        }
    }

    /**
     * Méthode pour choisir d'utiliser l'effet du magicien ou non.
     * @param game L'état actuel du jeu.
     */
    @Override
    public void magicianAlgorithm(Game game) {
        if (oneChanceOutOfTwo) { // avoir 50% de chance de décider de détruire un bâtiment d'un joueur aléatoire ou non
            List<Player> playerList = game.getSortedPlayersByScore();
            playerList.remove(bot);
            bot.getGameCharacter().specialEffect(bot, game, true, playerList.get(Utils.generateRandomNumber(playerList.size())));
        } else {
            bot.getGameCharacter().specialEffect(bot, game, false);
        }
    }

    /**
     * Méthode pour décider si le bot doit construire ou non.
     * @param gameState L'état actuel du jeu.
     */
    @Override
    public void buildOrNot(GameState gameState) { //construit s'il le peut
        int builtThisTurn = 0;

        for (District district : bot.getDistrictsInHand()) {
            if (bot.buildDistrict(district, gameState)) {
                builtThisTurn++;
                if ((!bot.getCharacterName().equals("Architect")) || (builtThisTurn == 3)) {
                    break;
                }
            }
        }
    }

    /**
     * Méthode pour choisir la couleur du quartier chassé.
     * @param huntedQuarter Le quartier chassé.
     */
    @Override
    public void huntedQuarterAlgorithm(District huntedQuarter) {
        huntedQuarter.setColor(DistrictColor.values()[Utils.generateRandomNumber(DistrictColor.values().length)]);
    }

    /**
     * Méthode pour décider si le bot doit utiliser l'effet de la manufacture.
     * @return true si le bot doit utiliser l'effet de la manufacture, false sinon.
     */
    @Override
    public boolean manufactureChoice() {
        return oneChanceOutOfTwo;
    }

    /**
     * Méthode pour choisir une carte à défausser avec l'effet du laboratoire.
     * @return Un Optional contenant la carte à défausser, ou un Optional vide si aucune carte ne doit être défaussée.
     */
    @Override
    public Optional<District> laboratoryChoice() {
        if (oneChanceOutOfTwo) {
            List<District> districtsInHand = bot.getDistrictsInHand();
            return !districtsInHand.isEmpty() ? Optional.ofNullable(districtsInHand.get(Utils.generateRandomNumber(districtsInHand.size()))) : Optional.empty();
        }
        return Optional.empty();
    }

    /**
     * Méthode pour décider si le bot doit utiliser l'effet du cimetière.
     * @return true si le bot doit utiliser l'effet du cimetière, false sinon.
     */
    @Override
    public boolean graveyardChoice() {
        return oneChanceOutOfTwo;
    }

    /**
     * Méthode pour que le bot choisisse une carte parmi trois.
     * @param game L'état actuel du jeu.
     * @param threeCards Les trois cartes parmi lesquelles choisir.
     */
    @Override
    public void botChoosesCard(Game game, List<District> threeCards) {
        District chosenCard = chooseCard(threeCards);
        bot.addDistrictInHand(chosenCard);
    }

    /**
     * Méthode pour choisir une carte parmi une liste de cartes.
     * @param cards La liste de cartes parmi lesquelles choisir.
     * @return La carte choisie.
     */
    public District chooseCard(List<District> cards) {
        return cards.get(Utils.generateRandomNumber(cards.size()));
    }
}