package fr.cotedazur.univ.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Bot") {
        };
    }

    @Test
    void testBuild() {
        District district = new District("Temple", 4, DistrictColor.religieux);
        assertFalse(player.build(district));
        player.districtsInHand.add(district);
        assertFalse(player.build(district));
        player.gold += 2;
        assertTrue(player.build(district));
    }
}
