package fr.cotedazur.univ.polytech.startingpoint.board;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import org.junit.jupiter.api.Test;


import java.util.Collections;


import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void addDistrictShouldAddDistrictToDeck() {
        Deck deck = new Deck();
        District district = new District("Test District", 1, DistrictColor.marchand);
        deck.addDistrict(district);
        assertEquals(1, deck.size());
    }

    @Test
    void addDistrictShouldNotAddNullDistrict() {
        Deck deck = new Deck();
        deck.addDistrict(null);
        assertEquals(0, deck.size());
    }


    @Test
    void drawCardShouldReturnLastCardAndRemoveItFromDeck() {
        Deck deck = new Deck();
        District district = new District("Test District", 1, DistrictColor.marchand);
        deck.addDistrict(district);
        District drawnCard = deck.drawCard();
        assertEquals(district, drawnCard);
        assertEquals(0, deck.size());
    }

    @Test
    void drawCardShouldThrowExceptionWhenDeckIsEmpty() {
        Deck deck = new Deck();
        assertThrows(EmptyDeckException.class, deck::drawCard);
    }

}
