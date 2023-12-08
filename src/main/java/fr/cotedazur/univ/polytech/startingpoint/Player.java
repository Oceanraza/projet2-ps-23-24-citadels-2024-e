package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    List<District> districtsInHand;
    List<District> districtsBuilt;
    int gold;
    String name;
    int score;

    Player(String name){
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
    public String getName() {
        return name;
    }

    public boolean build(District district) {
        for (District d : districtsBuilt){
            if (d.getName().equals(district.getName())){ //Checks if the district has already been built or not
                return false;
            }
        }
        // Checks if the player has enough gold to build the district. If so it is built.
        if (gold >= district.getPrice()){
            districtsBuilt.add(district);
            gold -= district.getPrice();
            districtsInHand.remove(district);
            System.out.println(getName() + " a construit le quartier " + district.getName());
            return true;
        }
        return false;
    }
    public String toString(){
        return "\nC'est au tour de : " + name + "\n" + (districtsInHand.size() > 0 ? "Et sa main est composée de: " + districtsInHand : "Sa main est vide. ") +  "\n" + "Il a " + gold + " or\n" + (districtsBuilt.size() > 0 ? "Et il a déjà posé: " + districtsBuilt : "Il n'a pas posé de quartiers.");

    }
}