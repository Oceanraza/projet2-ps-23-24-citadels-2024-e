package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    List<District> districtsInHand;
    List<District> districtsBuilt;
    int gold;

    Player(){
        districtsInHand = new ArrayList<>();
        districtsBuilt = new ArrayList<>();
        gold = 2;
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
}
