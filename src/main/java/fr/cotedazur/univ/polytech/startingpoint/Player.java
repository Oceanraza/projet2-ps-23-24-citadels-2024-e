package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    District[] districtsInHand;
    District[] districtsBuilt;
    int gold;

    Player(){
        districtsInHand = new District[8];
        districtsBuilt = new District[8];
        gold = 0;
    }

    public District[] getDistrictsBuilt() {
        return districtsBuilt;
    }
}
