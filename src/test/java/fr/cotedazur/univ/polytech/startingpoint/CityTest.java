package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.City;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictAlreadyBuiltException;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {
    GameState gameState;
    City city;
    District district;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        city = new City();
        district = new District("Castle", 5, DistrictColor.noble);
    }

    @Test
    void shouldAddDistrictToCity() {
        city.addDistrict(district, gameState);
        assertTrue(city.getDistrictsBuilt().contains(district));
    }

    @Test
    void shouldAddBuiltTurnToDistrict() {
        int turn = gameState.getTurn();
        city.addDistrict(district, gameState);
        Optional<Integer> turnBuilt = district.getTurnBuilt();
        assertTrue(turnBuilt.isPresent());
        assertEquals(turn, turnBuilt.get());
    }

    @Test
    void shouldNotAddDuplicateDistrictToCity() {
        city.addDistrict(district, gameState);
        try {
            city.addDistrict(district, gameState);
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
        city.addDistrict(district, gameState);
        assertFalse(city.isNotBuilt(district));
    }
}
