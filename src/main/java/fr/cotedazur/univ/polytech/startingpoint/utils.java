package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

//this class is used for basic methods that only have niche purposes and
// are generally just math.
public class Utils {
    public static boolean canDestroyDistrict(District d, Player p){
        return (d.getPrice() -1 < p.getGold());
    }

}
