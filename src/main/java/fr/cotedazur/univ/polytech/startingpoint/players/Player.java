package fr.cotedazur.univ.polytech.startingpoint.players;

import fr.cotedazur.univ.polytech.startingpoint.Characters;
import fr.cotedazur.univ.polytech.startingpoint.District;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    List<District> districtsInHand;
    List<District> districtsBuilt;
    int gold;
    String name;
    int score;
    Characters characters;

    public Player(String name) {
        this.name = name;
        districtsInHand = new ArrayList<>();
        districtsBuilt = new ArrayList<>();
        gold = 2;
        score = 0;
    }

    public abstract void play(Game game);

    public List<District> getDistrictsBuilt() {
        return districtsBuilt;
    }

    public List<District> getDistrictsInHand() {
        return districtsInHand;
    }
    public int getGold() {
        return gold;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public void chooseCharacter(Characters chosenCharacter) {
        this.characters = chosenCharacter;
    }

    public String toString() {
        if (characters == null) {
            return "\nC'est au tour de : " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composée de: "
                    + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " or\n" +
                    (!districtsBuilt.isEmpty() ? "Et il a déjà posé: " + districtsBuilt : "Il n'a pas posé de quartiers.");
        }

        // If a character is chosen, we specify the character
        return "\nC'est au tour du " + characters.getName() + " : " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composée de: "
                + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " or\n" +
                (!districtsBuilt.isEmpty() ? "Et il a déjà posé: " + districtsBuilt : "Il n'a pas posé de quartiers.");
    }
}