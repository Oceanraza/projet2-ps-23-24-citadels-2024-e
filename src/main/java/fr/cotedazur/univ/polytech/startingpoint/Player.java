package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    ArrayList<Quartier> districtsInHand;
    ArrayList<Quartier> districtsBuilt;
    int gold;

    Player(){
        districtsInHand = new ArrayList<>();
        districtsBuilt = new ArrayList<>();
        gold = 0;
    }

    public List<Quartier> getDistrictsBuilt() {
        return districtsBuilt;
    }
}
