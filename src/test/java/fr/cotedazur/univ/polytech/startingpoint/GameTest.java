package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {
    Game game;

    @BeforeEach
    void setUp() {
        InGameLogger.setup();
        InGameLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
    }

    @Test
    void testGame() {
        assertEquals(0, game.getPlayers().size());
        assertEquals(65, game.getDeck().size());
        assertEquals(0, game.getPlayers().size());
        assertEquals(0, game.getPlayers().size());
        assertEquals(0, game.getPlayers().size());
    }
}
