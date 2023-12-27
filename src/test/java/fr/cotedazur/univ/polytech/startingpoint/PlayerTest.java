package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.players.Bot;
import fr.cotedazur.univ.polytech.startingpoint.players.Player;
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
        player.addDistrictInHand(district);
        assertFalse(player.build(district));
        player.addGold(2);
        assertTrue(player.build(district));
        assertFalse(player.build(district));
        player.addDistrictInHand(district);
        assertFalse(player.build(district));
        player.addGold(4);
        district = new District("Temple", 4, DistrictColor.religieux); //We create another district to avoid pointer problems
        assertFalse(player.build(district));
    }

    @Test
    void isNotBuiltTest() {
        District district = new District("Temple", 4, DistrictColor.religieux);
        assertTrue(player.isNotBuilt(district));
        player.addDistrictBuilt(district);
        assertFalse(player.isNotBuilt(district));
        assertTrue(player.isNotBuilt(new District("Prison", 2, DistrictColor.militaire)));
    }

    @Test
    void districtsAlreadyBuiltTest() {
        District district = new District("Temple", 4, DistrictColor.religieux);
        assertTrue(player.districtsAlreadyBuilt());
        player.addDistrictInHand(district);
        assertFalse(player.districtsAlreadyBuilt());
        player.addDistrictBuilt(district);
        assertTrue(player.districtsAlreadyBuilt());
    }
}
