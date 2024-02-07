package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ArchitectTest {
    Game game;
    Architect architect;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setup();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
        architect = new Architect();
    }

    @Test
    void merchantInformationsTest() {
        assertEquals(7, architect.getRunningOrder());
        assertNull(architect.getColor());
    }
}