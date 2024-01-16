package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;

    @BeforeEach
    void setUp() {
        player = new Bot("Picsou");
    }

    @Test
    void testBuildDistrict() {
        District district = new District("Temple", 4, DistrictColor.religieux);
        assertFalse(player.buildDistrict(district));
        player.addDistrictInHand(district);
        assertFalse(player.buildDistrict(district));
        player.addGold(2);
        assertTrue(player.buildDistrict(district));
        assertFalse(player.buildDistrict(district));
        player.addDistrictInHand(district);
        assertFalse(player.buildDistrict(district));
        player.addGold(4);
        district = new District("Temple", 4, DistrictColor.religieux); //We create another district to avoid pointer problems
        assertFalse(player.buildDistrict(district));
    }

    @Test
    void testDistrictsInHandAreBuilt() {
        District district = new District("Temple", 4, DistrictColor.religieux);
        assertTrue(player.districtsInHandAreBuilt());
        player.addDistrictInHand(district);
        assertFalse(player.districtsInHandAreBuilt());
        player.addDistrictBuilt(district);
        assertTrue(player.districtsInHandAreBuilt());
    }
}
