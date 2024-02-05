package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
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
        assertEquals(GameCharacterRole.KING, king.getRole());
        assertEquals(4, king.getRunningOrder());
        assertNull(game.getCrown().getOwner());
        Bot p =  new Bot("Daffy");
        p.setGameCharacter(new King());
        p.getGameCharacter().specialEffect(p,game);
        assertEquals(game.getCrown().getOwner(),p);
    }

    @Test
    void evequeTest() {
        assertEquals(GameCharacterRole.BISHOP, bishop.getRole());
        assertEquals(5, bishop.getRunningOrder());
    }
}