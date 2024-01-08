package fr.cotedazur.univ.polytech.startingpoint.players;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Player {
    private final List<District> districtsInHand;
    private final List<District> districtsBuilt;
    private int gold;
    private final String name;
    private int score;
    private GameCharacter gameCharacter;
    private Map<DistrictColor, Integer> numberOfDistrictsByColor;

    protected Player(String name) {
        this.name = name;
        districtsInHand = new ArrayList<>();
        districtsBuilt = new ArrayList<>();
        gold = 2;
        score = 0;
        gameCharacter = null;
        numberOfDistrictsByColor = new HashMap<>();
        numberOfDistrictsByColor.put(DistrictColor.militaire,0);
        numberOfDistrictsByColor.put(DistrictColor.noble,0);
        numberOfDistrictsByColor.put(DistrictColor.special,0);
        numberOfDistrictsByColor.put(DistrictColor.religieux,0);
        numberOfDistrictsByColor.put(DistrictColor.marchand,0);
    }

    public List<District> getDistrictsBuilt() {
        return districtsBuilt;
    }

    public List<District> getDistrictsInHand() {
        return districtsInHand;
    }
    public int getGold() {
        return gold;
    }
    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
    public GameCharacter getGameCharacter() {
        return gameCharacter;
    }
    public void resetGameCharacter(){gameCharacter = null;}
    public String getCharacterName() {
        return gameCharacter.getName();
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getName() {
        return name;
    }

    public Characters getCharacter() {
        return characters;
    }

    public String getCharactersName() {
        return characters.getName();
    }
    public void addDistrictBuilt(District district) {
        numberOfDistrictsByColor.replace(
                district.getColor(),
                numberOfDistrictsByColor.get(district.getColor()) + 1);
        this.districtsBuilt.add(district);
    }

    public Map<DistrictColor, Integer> getNumberOfDistrictsByColor() {return numberOfDistrictsByColor;}

    public void addGold(int gold) {
        this.gold += gold;
    }

    // Function to build a district
    public boolean build(District district) {
        // Checks if the player has enough gold to build the district. If so it is
        // built.
        if (gold >= district.getPrice() && isNotBuilt(district)) {
            addDistrictBuilt(district);
            gold -= district.getPrice();
            districtsInHand.remove(district);
            System.out.println(getName() + " a construit le quartier " + district.getName());
            return true;
        }
        return false;
    }

    public boolean isNotBuilt(District district) {
        if (districtsBuilt.isEmpty()) {
            return true;
        }
        for (District d : districtsBuilt) {
            if (d.getName().equals(district.getName())) { // Checks if the district has already been built or not
                return false;
            }
        }
        return true;
    }

    public boolean districtsAlreadyBuilt() {
        for (District d : districtsInHand) {
            if (isNotBuilt(d)) {
                return false;
            }
        }
        return true;
    }
    public String toString() {
        if (gameCharacter == null) {
            return "\nC'est au tour de : " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composée de: "
                    + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " or\n" +
                    (!districtsBuilt.isEmpty() ? "Et il a déjà posé: " + districtsBuilt : "Il n'a pas posé de quartiers.");
        }

    public String toString() {
        return "\nC'est au tour de : " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composée de: "
                + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " or\n" +
                (!districtsBuilt.isEmpty() ? "Et il a déjà posé: " + districtsBuilt : "Il n'a pas posé de quartiers.");
    }
}