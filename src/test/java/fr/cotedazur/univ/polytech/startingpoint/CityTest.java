package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.City;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictAlreadyBuiltException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {
    City city;
    District district;

    @BeforeEach
    void setUp() {
        city = new City();
        district = new District("Castle", 5, DistrictColor.NOBLE);
    }

    @Test
    void shouldAddDistrictToCity() {
        city.addDistrict(district);
        assertTrue(city.getDistrictsBuilt().contains(district));
    }

    @Test
    void shouldNotAddDuplicateDistrictToCity() {
        city.addDistrict(district);
        try {
            city.addDistrict(district);
        } catch (DistrictAlreadyBuiltException e) {
            assertEquals("This district is already built", e.getMessage());
            assertEquals(1, city.getDistrictsBuilt().size());
        }
    }

    @Test
    void shouldReturnTrueIfDistrictIsNotBuilt() {
        assertTrue(city.isNotBuilt(district));
    }

    @Test
    void shouldReturnFalseIfDistrictIsBuilt() {
        city.addDistrict(district);
        assertFalse(city.isNotBuilt(district));
    }
}
