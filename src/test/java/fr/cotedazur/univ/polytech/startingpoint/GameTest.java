package fr.cotedazur.univ.polytech.startingpoint;

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
        assertFalse(game.gameDeck.isEmpty());
        int normalCardCount = game.gameDeck.size();
        District district = new District("Test", 1, DistrictColor.special);
        game.addCardNumber(district, 4);
        assertEquals(normalCardCount + 4, game.gameDeck.size());
    }

    @Test
    void testDrawCard() {
        List<District> normalGameDeck = copyOf(game.gameDeck);
        District cardDrawn = game.drawCard();
        int cardCountBeforeDraw = 0;
        for(District district: normalGameDeck) {
            if (district.equals(cardDrawn)) {
                cardCountBeforeDraw++;
            }
        }
        int cardCountAfterDraw = 0;
        for(District district: game.gameDeck) {
            if (district.equals(cardDrawn)) {
                cardCountAfterDraw++;
            }
        }
        assertTrue(normalGameDeck.contains(cardDrawn));
        assertEquals(cardCountBeforeDraw - 1, cardCountAfterDraw);
    }
}
