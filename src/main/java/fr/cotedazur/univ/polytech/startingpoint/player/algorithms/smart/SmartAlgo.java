package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.BaseAlgo;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.*;

import static fr.cotedazur.univ.polytech.startingpoint.Game.CITY_SIZE_TO_WIN;
import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.*;

/**
 * Classe abstraite représentant un algorithme intelligent pour un bot.
 * Cette classe est une extension de la classe BaseAlgo.
 */
public abstract class SmartAlgo extends BaseAlgo {
    protected boolean lowestDistrictFound = false;

    /**
     * Constructeur de la classe SmartAlgo.
     */
    protected SmartAlgo() {
        super();
    }

    /**
     * Méthode pour vérifier si un personnage peut être tué.
     *
     * @param killableCharacters Liste des personnages qui peuvent être tués.
     * @param charEnum           Le rôle du personnage à vérifier.
     * @return L'index du personnage dans la liste s'il peut être tué, -1 sinon.
     */
    int isKillable(List<GameCharacter> killableCharacters, GameCharacterRole charEnum) {
        for (int i = 0; i < killableCharacters.size(); i++) {
            if (killableCharacters.get(i).getRole() == charEnum) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Méthode pour indiquer que le quartier le plus bas a été trouvé.
     */
    public void lowestDistrictHasBeenFound() {
        lowestDistrictFound = true;
    }

    /**
     * Méthode pour choisir entre prendre de l'or ou piocher une carte en début de tour.
     *
     * @return 2 pour piocher une carte, 1 pour prendre de l'or.
     */
    public int startOfTurnChoice() {
        if (bot.getDistrictsInHand().isEmpty() || bot.districtsInHandAreBuilt() || bot.getGameCharacter().getRole().equals(ARCHITECT)) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * Méthode pour décider si le bot doit utiliser l'effet de la manufacture.
     *
     * @return true si le bot doit utiliser l'effet de la manufacture, false sinon.
     */
    public boolean manufactureChoice() {
        Set<District> builtAndBuildableDistricts = new HashSet<>(bot.getCity().getDistrictsBuilt());
        builtAndBuildableDistricts.addAll(bot.getDistrictsInHand());
        return builtAndBuildableDistricts.size() < CITY_SIZE_TO_WIN - 1;
    }

    /**
     * Méthode pour choisir une carte à défausser avec l'effet du laboratoire.
     * @return Un Optional contenant la carte à défausser, ou un Optional vide si aucune carte ne doit être défaussée.
     */
    public Optional<District> laboratoryChoice() {
        List<District> districtsBuilt = bot.getCity().getDistrictsBuilt();
        List<District> districtsInHand = bot.getDistrictsInHand();
        for (District district : districtsInHand) {
            if (districtsBuilt.contains(district) || Collections.frequency(districtsInHand, district) > 1) {
                return Optional.of(district);
            }
        }
        if (districtsBuilt.size() + districtsInHand.size() > 8) {
            return Optional.of(districtsInHand.remove(districtsInHand.size() - 1));
        }
        return Optional.empty();
    }

    /**
     * Méthode pour choisir le quartier à viser avec l'effet du quartier chassé.
     * @param huntedQuarter Le quartier viser.
     */
    public void huntedQuarterAlgorithm(District huntedQuarter) {
        Set<DistrictColor> colorList = new HashSet<>();
        List<District> districtsBuilt = bot.getCity().getDistrictsBuilt();
        for (District district : districtsBuilt) {
            colorList.add(district.getColor());
        }
        DistrictColor[] districtColors = DistrictColor.values();
        if (colorList.size() == districtColors.length - 1) {
            for (DistrictColor districtColor : districtColors) {
                if (!colorList.contains(districtColor)) {
                    huntedQuarter.setColor(districtColor);
                }
            }
        }
    }

    /**
     * Méthode pour décider si le bot doit collecter de l'or avant de construire.
     * @return true si le bot doit collecter de l'or avant de construire, false sinon.
     */
    @Override
    public boolean collectGoldBeforeBuildChoice() {
        Optional<District> lowestDistrict = bot.getLowestDistrictInHand();
        return lowestDistrict.isPresent() && (bot.getGold() < lowestDistrict.get().getPrice());
    }

    /**
     * Méthode pour choisir un personnage à tuer avec l'effet de l'assassin.
     * @param game L'état actuel du jeu.
     */
    @Override
    public void assassinAlgorithm(Game game) {
        List<GameCharacter> killableCharacters;
        int indexKilledCharacter;
        GameCharacterRole targetedCharacter;

        int indexWarlord;
        int indexKing;

        killableCharacters = game.getKillableCharacters();
        indexWarlord = isKillable(killableCharacters, WARLORD);
        indexKing = isKillable(killableCharacters, KING);

        // Kill the warlord if possible
        if (indexWarlord != -1) {
            indexKilledCharacter = indexWarlord;
        }
        // Kill the king if the warlord can't be killed
        else if (indexKing != -1) {
            indexKilledCharacter = indexKing;
        }
        // Kill a random character if neither the warlord nor the king can be killed
        else {
            int numberOfTargets = game.getKillableCharacters().size();
            indexKilledCharacter = Utils.generateRandomNumber(numberOfTargets);
        }

        targetedCharacter = game.getKillableCharacters().get(indexKilledCharacter).getRole();
        bot.getGameCharacter().specialEffect(bot, game, targetedCharacter);
    }

    /**
     * Méthode pour choisir une carte parmi une liste de cartes.
     * @param cards La liste de cartes parmi lesquelles choisir.
     * @return La carte choisie.
     */
    public District chooseCard(List<District> cards) {
        District chosenCard = null;
        int minCost = Integer.MAX_VALUE;
        for (District card : cards) {
            if (card.getPrice() <= bot.getGold() && card.getPrice() < minCost) {
                chosenCard = card;
                minCost = card.getPrice();
            }
        }
        return chosenCard;
    }
}