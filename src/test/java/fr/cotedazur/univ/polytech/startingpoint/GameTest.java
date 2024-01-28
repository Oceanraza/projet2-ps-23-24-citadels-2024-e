package fr.cotedazur.univ.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;

    @BeforeEach
    void setUp() {
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
