package fr.cotedazur.univ.polytech.startingpoint.board;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.city.District;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Deck
 */

public class Deck {
    private List<District> cards;
    private List<District> discardPile;
    private Game game;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void setDeck(List<District> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Les cartes dans le deck sont : \n");
        for (District district : this.cards) {
            str.append(district).append('\n');
        }
        return str.toString();
    }

    /**
     * Adds a district to the deck.
     *
     * @param district the district to be added
     */

    public void addDistrict(District district) {
        if (district != null) {
            this.cards.add(district);
        }
    }

    /**
     * Shuffles the deck.
     */

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    /**
     * Draws a card from the deck.
     *
     * @return the card drawn
     * @throws EmptyDeckException if the deck is empty
     */
    public District drawCard() {
        if (this.isEmpty()) {
            throw new EmptyDeckException("Deck is empty");
        }
        return this.cards.remove(this.size() - 1);
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */

    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    /**
     * Returns the number of cards in the deck.
     *
     * @return the number of cards in the deck
     */

    public int size() {
        return this.cards.size();
    }

    /**
     * Discards a district.
     *
     * @param district the district to be discarded
     */
    public void discardDistrict(District district) {
        this.cards.remove(district);
        this.discardPile.add(district);
    }

    public List<District> getCards() {
        return this.cards;
    }

    public void discard(District card) {
        this.cards.remove(card);
        this.discardPile.add(card);
    }
}