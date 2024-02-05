package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    Player player;
    GameState gameState;

    @BeforeEach
    void setUp() {
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
