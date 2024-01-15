package fr.cotedazur.univ.polytech.startingpoint.city;

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
    public void addDistrict(District district) {
        if (isNotBuilt(district)) {
            districtsBuilt.add(district);
        } else throw new DistrictAlreadyBuiltException("This district is already built");
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