package fr.cotedazur.univ.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
            player.getDistrictsBuilt().add(new Quartier("test", 0, QuartierColor.marchand));
        }
        assertEquals(true, Main.isFinished(player));
    }
}