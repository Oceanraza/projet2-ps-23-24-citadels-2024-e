package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.card.Bishop;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BishopTest {
    Game game;
    Bishop bishop;

    @BeforeEach
    void setUp() {
        InGameLogger.setup();
        InGameLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
        bishop = new Bishop();
    }

    @Test
    void bishopInformationsTest() {
        assertEquals(5, bishop.getRunningOrder());
        assertEquals(DistrictColor.RELIGIOUS, bishop.getColor());
    }
}
