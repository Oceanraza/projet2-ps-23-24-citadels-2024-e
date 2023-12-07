package fr.cotedazur.univ.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {
    Player player;

    @BeforeEach
    void setUp() {
        player = new Player() {
        };
    }

    @Test
    void testIsFinished() {
        for (int i = 0; i < 8; i++) {
            player.getDistrictsBuilt()[i] = new District("test", 0, DistrictColor.marchand);
        }
        assertTrue(Main.isFinished(player));
    }
}