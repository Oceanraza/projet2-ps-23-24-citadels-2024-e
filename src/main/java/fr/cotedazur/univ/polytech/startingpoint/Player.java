package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    List<District> districtsInHand;
    List<District> districtsBuilt;
    int gold;
    String name;
    int score;
    Characters characters;

    Player(){
        districtsInHand = new ArrayList<>();
        districtsBuilt = new ArrayList<>();
        gold = 2;
    }

    Player(String name){
        this.name = name;
        districtsInHand = new ArrayList<>();
        districtsBuilt = new ArrayList<>();
        gold = 2;
        score = 0;
        characters = null;
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

    public String getCharacters() {
        return characters.getName();
    }

    public boolean build(District district) {
        // Checks if the player has enough gold to build the district. If so it is built.
        if (gold >= district.getPrice()) {
            districtsBuilt.add(district);
            gold -= district.getPrice();
            districtsInHand.remove(district);
            System.out.println("Le quartier " + district.getName() + " a été construit.");
            return true;
        }
        return false;
    }

    public void chooseCharacter(Characters characterChosen) {
        this.characters = characterChosen;
    }
}