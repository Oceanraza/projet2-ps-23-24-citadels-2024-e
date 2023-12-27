package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private final List<District> districtsInHand;
    private final List<District> districtsBuilt;
    private int gold;
    private final String name;
    private int score;
    private GameCharacter gameCharacter;

    Player(String name) {
        this.name = name;
        districtsInHand = new ArrayList<>();
        districtsBuilt = new ArrayList<>();
        gold = 2;
        score = 0;
    }

    // Getter
    public List<District> getDistrictsInHand() {
        return districtsInHand;
    }
    public List<District> getDistrictsBuilt() {
        return districtsBuilt;
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
    public String getCharacterName() {
        return gameCharacter.getName();
    }

    // Setter
    public void setGold(int gold) {
        this.gold = gold;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void setGameCharacter(GameCharacter gameCharacter) {
        this.gameCharacter = gameCharacter;
    }

    // Functions to add
    public void addDistrictInHand(District district) {
        this.districtsInHand.add(district);
    }
    public void addDistrictBuilt(District district) {
        this.districtsBuilt.add(district);
    }
    public void addGold(int gold) {
        this.gold += gold;
    }

    public abstract void play(Game game);
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

    public String toString() {
        if (gameCharacter == null) {
            return "\nC'est au tour de : " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composée de: "
                    + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " or\n" +
                    (!districtsBuilt.isEmpty() ? "Et il a déjà posé: " + districtsBuilt : "Il n'a pas posé de quartiers.");
        }

        // If a character is chosen, we specify the character
        return "\nC'est au tour du " + gameCharacter.getName() + " : " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composée de: "
                + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " or\n" +
                (!districtsBuilt.isEmpty() ? "Et il a déjà posé: " + districtsBuilt : "Il n'a pas posé de quartiers.");
    }
}