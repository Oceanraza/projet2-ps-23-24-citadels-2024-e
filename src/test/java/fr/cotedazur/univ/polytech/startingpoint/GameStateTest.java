package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameStateTest {
    Player player;
    GameState gameState;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setup();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        gameState = new GameState();
        player = new Bot("Test");
    }
    @Test
    void testIsFinished() {
        assertFalse(gameState.isFinished(player));
        for (int i = 0; i < 8; i++) {
            String name = "District" + i;
            player.getCity().addDistrict(new District(name, 0, DistrictColor.TRADE), gameState);
        }
        assertTrue(gameState.isFinished(player));
    }
}
