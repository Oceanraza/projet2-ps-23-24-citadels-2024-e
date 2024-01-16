package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.List.copyOf;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void testAddCardNumber() {
        assertFalse(game.getGameDeck().isEmpty());
        int normalCardCount = game.getGameDeck().size();
        District district = new District("Test", 1, DistrictColor.SPECIAL);
        game.addDistrictsInGameDeck(district, 4);
        assertEquals(normalCardCount + 4, game.getGameDeck().size());
    }

    @Test
    void testDrawCard() {
        List<District> normalGameDeck = copyOf(game.getGameDeck());
        District cardDrawn = game.drawCard();
        int cardCountBeforeDraw = 0;
        for(District district: normalGameDeck) {
            if (district.equals(cardDrawn)) {
                cardCountBeforeDraw++;
            }
        }
        int cardCountAfterDraw = 0;
        for(District district: game.getGameDeck()) {
            if (district.equals(cardDrawn)) {
                cardCountAfterDraw++;
            }
        }
        assertTrue(normalGameDeck.contains(cardDrawn));
        assertEquals(cardCountBeforeDraw - 1, cardCountAfterDraw);
    }
}
