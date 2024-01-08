package fr.cotedazur.univ.polytech.startingpoint;

<<<<<<< HEAD
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.*;
=======
import fr.cotedazur.univ.polytech.startingpoint.characters.Character1;
import fr.cotedazur.univ.polytech.startingpoint.characters.King;
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharactersTest {
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
<<<<<<< HEAD
    void evequeTest() {
        assertEquals("Eveque", eveque.getName());
        assertEquals(5, eveque.getRunningOrder());
=======
    void character1Test() {
        assertEquals("Character1", character1.getName());
        assertEquals(1, character1.getRunningOrder());
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c
    }
}