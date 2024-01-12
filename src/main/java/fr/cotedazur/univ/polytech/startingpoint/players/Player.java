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
    public void destroyDistrict(District districtToDestroy){
        for (District d : districtsBuilt){
            if (d.equals(districtToDestroy)){
                districtsBuilt.remove(d);
                return;
            }
        }
    }
    public void removeGold(int g){
        gold -= g;
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
    public void resetGameCharacter(){gameCharacter = null;}
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
        numberOfDistrictsByColor.replace(
                district.getColor(),
                numberOfDistrictsByColor.get(district.getColor()) + 1);
        this.districtsBuilt.add(district);
    }

    public Map<DistrictColor, Integer> getNumberOfDistrictsByColor() {return numberOfDistrictsByColor;}

    public void addGold(int gold) {
        this.gold += gold;
    }

    public abstract void play(Game game);
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

        // If a character is chosen, we specify the character
        return "\nC'est au tour du " + gameCharacter.getName() + " : " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composée de: "
                + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " or\n" +
                (!districtsBuilt.isEmpty() ? "Et il a déjà posé: " + districtsBuilt : "Il n'a pas posé de quartiers.");
    }
    public int calculateScore(){
        int tempScore = getGold();
        ArrayList<DistrictColor> districtColors = new ArrayList<>();
        for (District district : getDistrictsBuilt()) {
            tempScore += district.getPrice();
            districtColors.add(district.getColor());
        }
        if (districtColors.size() == DistrictColor.values().length) { // If the player has built all the district
            // colors
            tempScore += 3;
        }
        setScore(tempScore); // Initialize the player's score
        return tempScore;
    }

    public boolean equals(Player p) {
        return (p.getName().equals(getName()));
    }
}