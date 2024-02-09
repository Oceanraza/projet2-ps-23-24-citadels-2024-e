package fr.cotedazur.univ.polytech.startingpoint.player;

import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;
    GameState gameState;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setupDemo();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        gameState = new GameState();
        player = new Bot("Picsou");
    }

    @Test
    void testBuildDistrict() {
        GameState gameState = new GameState();
        District district = new District("Temple", 4, DistrictColor.RELIGIOUS);
        assertFalse(player.buildDistrict(district, gameState));
        player.addDistrictInHand(district);
        assertFalse(player.buildDistrict(district, gameState));
        player.addGold(2);
        assertTrue(player.buildDistrict(district, gameState));
        assertFalse(player.buildDistrict(district, gameState));
        player.addDistrictInHand(district);
        assertFalse(player.buildDistrict(district, gameState));
        player.addGold(4);
        district = new District("Temple", 4, DistrictColor.RELIGIOUS); //We create another district to avoid pointer problems
        assertFalse(player.buildDistrict(district, gameState));
    }

    @Test
    void testDistrictsInHandAreBuilt() {
        District district = new District("Temple", 4, DistrictColor.RELIGIOUS);
        assertTrue(player.districtsInHandAreBuilt());
        player.addDistrictInHand(district);
        assertFalse(player.districtsInHandAreBuilt());
        player.addDistrictBuilt(district, new GameState());
        assertTrue(player.districtsInHandAreBuilt());
    }

    @Test
    void testCalculateScore() {
        player.setGold(2);
        assertEquals(2, player.calculateScore());
        District religieux = new District("religieux", 1, DistrictColor.RELIGIOUS);
        District noble = new District("noble", 1, DistrictColor.NOBLE);
        District militaire = new District("militaire", 1, DistrictColor.MILITARY);
        District marchand = new District("marchand", 1, DistrictColor.TRADE);
        player.addDistrictBuilt(religieux, gameState);
        player.addDistrictBuilt(noble, gameState);
        player.addDistrictBuilt(militaire, gameState);
        player.addDistrictBuilt(marchand, gameState);
        assertEquals(2 + 4, player.calculateScore());
        District special = new District("special", 1, DistrictColor.SPECIAL, 1);
        player.addDistrictBuilt(special, gameState);
        assertEquals(2 + 5 + 3 + 1, player.calculateScore());
    }

    @Test
    void testCalculateAndSetScore() {
        player.setGold(2);
        player.calculateAndSetScore();
        assertEquals(2, player.getScore());
    }
}
