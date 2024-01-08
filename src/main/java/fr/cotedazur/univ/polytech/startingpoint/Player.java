<<<<<<< HEAD
package fr.cotedazur.univ.polytech.startingpoint.players;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
=======
package fr.cotedazur.univ.polytech.startingpoint;
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
<<<<<<< HEAD
    private final List<District> districtsInHand;
    private final List<District> districtsBuilt;
    private int gold;
    private final String name;
    private int score;
    private GameCharacter gameCharacter;
    private Map<DistrictColor, Integer> numberOfDistrictsByColor;
=======
    List<District> districtsInHand;
    List<District> districtsBuilt;
    int gold;
    String name;
    int score;
    Characters characters;
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c

    protected Player(String name) {
        this.name = name;
        districtsInHand = new ArrayList<>();
        districtsBuilt = new ArrayList<>();
        gold = 2;
        score = 0;
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
<<<<<<< HEAD
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
=======
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c

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
<<<<<<< HEAD
    public void addDistrictBuilt(District district) {
        numberOfDistrictsByColor.replace(
                district.getColor(),
                numberOfDistrictsByColor.get(district.getColor()) + 1);
        this.districtsBuilt.add(district);
    }

    public Map<DistrictColor, Integer> getNumberOfDistrictsByColor() {return numberOfDistrictsByColor;}

    public void addGold(int gold) {
        this.gold += gold;
=======

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c
    }

    // Function to build a district
    public boolean build(District district) {
        // Checks if the player has enough gold to build the district. If so it is
        // built.
        if (gold >= district.getPrice() && isNotBuilt(district)) {
            districtsBuilt.add(district);
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
<<<<<<< HEAD
    public String toString() {
        if (gameCharacter == null) {
            return "\nC'est au tour de : " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composée de: "
                    + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " or\n" +
                    (!districtsBuilt.isEmpty() ? "Et il a déjà posé: " + districtsBuilt : "Il n'a pas posé de quartiers.");
        }
=======

    public void chooseCharacter(Characters characterChosen) {
        this.characters = characterChosen;
    }
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c

    public String toString() {
        return "\nC'est au tour de : " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composée de: "
                + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " or\n" +
                (!districtsBuilt.isEmpty() ? "Et il a déjà posé: " + districtsBuilt : "Il n'a pas posé de quartiers.");
    }
}