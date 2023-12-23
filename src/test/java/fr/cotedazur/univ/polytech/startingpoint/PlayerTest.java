package fr.cotedazur.univ.polytech.startingpoint;

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
        player.getDistrictsInHand().add(district);
        assertFalse(player.build(district));
        player.setGold(player.getGold() + 4);
        assertTrue(player.build(district));
        assertFalse(player.build(district));
        player.getDistrictsInHand().add(district);
        assertFalse(player.build(district));
        player.setGold(player.getGold() + 4);
        district = new District("Temple", 4, DistrictColor.religieux); //We create another district to avoid pointer problems
        assertFalse(player.build(district));
    }

    @Test
    void isNotBuiltTest() {
        District district = new District("Temple", 4, DistrictColor.religieux);
        assertTrue(player.isNotBuilt(district));
        player.getDistrictsBuilt().add(district);
        assertFalse(player.isNotBuilt(district));
        assertTrue(player.isNotBuilt(new District("Prison", 2, DistrictColor.militaire)));
    }

    @Test
    void districtsAlreadyBuiltTest() {
        District district = new District("Temple", 4, DistrictColor.religieux);
        assertTrue(player.districtsAlreadyBuilt());
        player.getDistrictsInHand().add(district);
        assertFalse(player.districtsAlreadyBuilt());
        player.getDistrictsBuilt().add(district);
        assertTrue(player.districtsAlreadyBuilt());
    }
}
