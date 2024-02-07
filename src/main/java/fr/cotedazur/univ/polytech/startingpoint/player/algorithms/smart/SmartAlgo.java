package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.BaseAlgo;

import java.util.*;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.ARCHITECT;
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
        return builtAndBuildableDistricts.size() < 8 - 1; // If player has 7 built + buildable districts he can just draw without paying 3 gold coins
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

    public void graveyardLogic(District destroyedDistrict) {
        if (bot.getCity().containsDistrict("Cimetiere") && bot.getGold() >= 1 && !bot.getCharacterName().equals("Condottiere")) {
            String graveyardMessage = bot.getName() + " utilise le Cimetiere pour reprendre le " + destroyedDistrict + " dans sa main.";
            LOGGER.info(graveyardMessage);
            bot.getDistrictsInHand().add(destroyedDistrict);
            bot.removeGold(1);
        }
    }

}