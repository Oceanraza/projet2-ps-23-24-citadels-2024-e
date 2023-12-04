package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    ArrayList<District> districtsInHand;
    ArrayList<District> districtsBuilt;
    int gold;

    Player(){
        districtsInHand = new ArrayList<>();
        districtsBuilt = new ArrayList<>();
        gold = 0;
    }

    public List<District> getDistrictsBuilt() {
        return districtsBuilt;
    }
}
