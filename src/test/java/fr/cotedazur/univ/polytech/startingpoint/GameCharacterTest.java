package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.character.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameCharacterTest {
    King king;
    Bishop bishop;

    @BeforeEach
    void setUp() {
        king = new King();
        bishop = new Bishop();
    }

    @Test
    void kingTest() {
        assertEquals("Roi", king.getName());
        assertEquals(4, king.getRunningOrder());
    }

    @Test
    void evequeTest() {
        assertEquals("Eveque", bishop.getName());
        assertEquals(5, bishop.getRunningOrder());
    }
}