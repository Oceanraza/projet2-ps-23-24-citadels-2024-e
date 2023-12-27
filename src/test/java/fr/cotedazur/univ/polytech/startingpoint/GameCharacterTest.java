package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.Character1;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.King;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameCharacterTest {
    King king;
    Character1 character1;

    @BeforeEach
    void setUp() {
        king = new King();
        character1 = new Character1();
    }

    @Test
    void kingTest() {
        assertEquals("Roi", king.getName());
        assertEquals(4, king.getRunningOrder());
    }

    @Test
    void character1Test() {
        assertEquals("Personnage 1", character1.getName());
        assertEquals(1, character1.getRunningOrder());
    }
}