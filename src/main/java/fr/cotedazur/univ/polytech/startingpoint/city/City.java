package fr.cotedazur.univ.polytech.startingpoint.city;

import fr.cotedazur.univ.polytech.startingpoint.GameState;

import java.util.ArrayList;
import java.util.List;

public class City {
    private final List<District> districtsBuilt;

    public City() {
        this.districtsBuilt = new ArrayList<>();
    }

    public List<District> getDistrictsBuilt() {
        return districtsBuilt;
    }

    // Checks if a district is already built in the city
    public boolean isNotBuilt(District district) {
        if (districtsBuilt.isEmpty()) {
            return true;
        }
        for (District d : districtsBuilt) {
            if (d.getName().equals(district.getName())) {
                return false;
            }
        }
        return true;
    }

    // Adds a district to the city
    public void addDistrict(District district, GameState gameState) {
        if (isNotBuilt(district)) {
            districtsBuilt.add(district);
            district.setTurnBuilt(gameState.getTurn());
        } else throw new DistrictAlreadyBuiltException("Ce district a deja ete construit");
    }

    public void destroyDistrict(District districtToDestroy) {
        for (District d : districtsBuilt) {
            if (d.equals(districtToDestroy)) {
                districtsBuilt.remove(d);
                return;
            }
        }
    }

    public boolean containsDistrict(String districtName) {
        for(District d: districtsBuilt) {
            if(d.getName().equals(districtName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder city = new StringBuilder();
        for (District d : districtsBuilt) {
            city.append(d.getName()).append(" ");
        }
        return city.toString();
    }
}