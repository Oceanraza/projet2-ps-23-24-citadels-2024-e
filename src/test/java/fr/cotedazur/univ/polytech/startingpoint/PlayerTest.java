package fr.cotedazur.univ.polytech.startingpoint;

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
    void testBuild() {
        District district = new District("Temple", 4, DistrictColor.religieux);
        assertFalse(player.build(district));
        player.districtsInHand.add(district);
        assertFalse(player.build(district));
        player.gold += 2;
        assertTrue(player.build(district));
        assertFalse(player.build(district));
        player.districtsInHand.add(district);
        assertFalse(player.build(district));
        player.gold += 4;
        district = new District("Temple", 4, DistrictColor.religieux); //We create another district to avoid pointer problems
        assertFalse(player.build(district));
    }

    @Test
    void isNotBuiltTest() {
        District district = new District("Temple", 4, DistrictColor.religieux);
        assertTrue(player.isNotBuilt(district));
        player.districtsBuilt.add(district);
        assertFalse(player.isNotBuilt(district));
        assertTrue(player.isNotBuilt(new District("Prison", 2, DistrictColor.militaire)));
    }

    @Test
    void districtsAlreadyBuiltTest() {
        District district = new District("Temple", 4, DistrictColor.religieux);
        assertTrue(player.districtsAlreadyBuilt());
        player.districtsInHand.add(district);
        assertFalse(player.districtsAlreadyBuilt());
        player.districtsBuilt.add(district);
        assertTrue(player.districtsAlreadyBuilt());
    }
}
