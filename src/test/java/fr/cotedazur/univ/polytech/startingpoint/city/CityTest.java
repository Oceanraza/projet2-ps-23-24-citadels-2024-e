package fr.cotedazur.univ.polytech.startingpoint.city;

import fr.cotedazur.univ.polytech.startingpoint.GameState;
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
        district = new District("Chateau", 5, DistrictColor.NOBLE);
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
            assertEquals("Ce district a deja ete construit", e.getMessage());
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
