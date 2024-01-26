package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
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

    @Test
    void testCalculateScore() {
        player.setGold(2);
        assertEquals(2, player.calculateScore());
        District religieux = new District("religieux", 1, DistrictColor.religieux);
        District noble = new District("noble", 1, DistrictColor.noble);
        District militaire = new District("militaire", 1, DistrictColor.militaire);
        District marchand = new District("marchand", 1, DistrictColor.marchand);
        player.addDistrictBuilt(religieux);
        player.addDistrictBuilt(noble);
        player.addDistrictBuilt(militaire);
        player.addDistrictBuilt(marchand);
        assertEquals(2+4, player.calculateScore());
        District special = new District("special", 1, DistrictColor.special, 1);
        player.addDistrictBuilt(special);
        assertEquals(2+5+3+1, player.calculateScore());
    }

    @Test
    void testCalculateAndSetScore() {
        player.setGold(2);
        player.calculateAndSetScore();
        assertEquals(2, player.getScore());
    }
}
