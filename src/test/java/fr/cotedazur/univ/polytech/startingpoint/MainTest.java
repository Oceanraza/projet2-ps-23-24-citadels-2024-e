package fr.cotedazur.univ.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Bot") {
        };
    }

    @Test
    void testIsFinished() {
        assertFalse(Main.isFinished(player));
        for (int i = 0; i < 8; i++) {
            player.getDistrictsBuilt().add(new District("test", 0, DistrictColor.marchand));
        }
        assertTrue(Main.isFinished(player));
    }

    @Test
    void testIsWinner() {

    }
}