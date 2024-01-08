package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameCharacterTest {
    King king;
    Eveque eveque;

    @BeforeEach
    void setUp() {
        king = new King();
        eveque = new Eveque();
    }

    @Test
    void kingTest() {
        assertEquals("Roi", king.getName());
        assertEquals(4, king.getRunningOrder());
    }

    @Test
    void evequeTest() {
        assertEquals("Eveque", eveque.getName());
        assertEquals(5, eveque.getRunningOrder());
    }
}