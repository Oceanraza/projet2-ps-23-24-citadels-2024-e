package fr.cotedazur.univ.polytech.startingpoint.board;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.exception.EmptyDeckException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeckTest {
    Deck deck;
    District district1;
    District district2;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        district1 = new District("District 1", 1, DistrictColor.TRADE);
        district2 = new District("District 2", 2, DistrictColor.MILITARY);
    }

    @Test
    void addDistrictShouldAddDistrictToDeck() {
        deck.addDistrict(district1);
        assertEquals(1, deck.size());
    }

    @Test
    void addDistrictShouldNotAddNullDistrict() {
        deck.addDistrict(null);
        assertEquals(0, deck.size());
    }

    @Test
    void drawCardShouldReturnLastCardAndRemoveItFromDeck() {
        deck.addDistrict(district1);
        District drawnCard = deck.drawCard();
        assertEquals(district1, drawnCard);
        assertEquals(0, deck.size());
    }

    @Test
    void drawCardShouldThrowExceptionWhenDeckIsEmpty() {
        assertThrows(EmptyDeckException.class, deck::drawCard);
    }

    @Test
    void putCardAtBottomTest() {
        District discardedDistrict = new District("Discarded District", 1, DistrictColor.NOBLE);
        deck.addDistrict(district1);
        deck.addDistrict(district2);
        deck.putCardAtBottom(discardedDistrict);
        assertEquals(district2, deck.drawCard());
        assertEquals(district1, deck.drawCard());
        assertEquals(discardedDistrict, deck.drawCard());
    }

    @Test
    void toStringTest() {
        deck.addDistrict(district1);
        deck.addDistrict(district2);
        String str = "Les cartes dans le deck sont : \nDistrict 1-1-marchand\nDistrict 2-2-militaire\n\n";
        assertEquals(str, deck.toString());
    }
}
