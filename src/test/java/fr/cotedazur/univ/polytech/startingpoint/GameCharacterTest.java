package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.character.*;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameCharacterTest {
    King king;
    Bishop bishop;
    Game game;

    @BeforeEach
    void setUp() {
        king = new King();
        bishop = new Bishop();
        game = new Game();
        game.init();
    }

    @Test
    void kingTest() {
        assertEquals("Roi", king.getName());
        assertEquals(4, king.getRunningOrder());
        assertEquals(game.getCrown().getOwner(), null);
        Bot p = new Bot("Daffy");
        p.setGameCharacter(new King());
        p.getGameCharacter().specialEffect(p, game);
        assertEquals(game.getCrown().getOwner(), p);
    }

    @Test
    void evequeTest() {
        assertEquals("Eveque", bishop.getName());
        assertEquals(5, bishop.getRunningOrder());
    }
}