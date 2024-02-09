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
import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.LOGGER;

public abstract class SmartAlgo extends BaseAlgo {
    protected boolean lowestDistrictFound = false;

    protected SmartAlgo() {
        super();
    }

    // To know if the assassin can kill this character
    int isKillable(List<GameCharacter> killableCharacters, GameCharacterRole charEnum) {
        for (int i = 0; i < killableCharacters.size(); i++) {
            // If the character can be killed, this functions returns the index of this character
            if (killableCharacters.get(i).getRole() == charEnum) {
                return i;
            }
        }
        return -1;
    }

    public void lowestDistrictHasBeenFound() {
        lowestDistrictFound = true;
    }

    public int startOfTurnChoice() { // Always draws if needed
        if (bot.getDistrictsInHand().isEmpty() || bot.districtsInHandAreBuilt() || bot.getGameCharacter().getRole().equals(ARCHITECT)) {
            return 2; // Draw a card
        } else {
            return 1; // Take 2 gold coins
        }
    }

    public boolean manufactureChoice() { // Use manufacture effect if the player has less than 7 built + buildable districts
        Set<District> builtAndBuildableDistricts = new HashSet<>(bot.getCity().getDistrictsBuilt());
        builtAndBuildableDistricts.addAll(bot.getDistrictsInHand());
        return builtAndBuildableDistricts.size() < CITY_SIZE_TO_WIN - 1; // If player has 7+ built + buildable districts he doesn't need to use manufacture effect and can just draw normally
    }


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

    @Override
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
}
