package fr.cotedazur.univ.polytech.startingpoint.board;

import com.fasterxml.jackson.databind.JsonNode;
import fr.cotedazur.univ.polytech.startingpoint.exception.JsonFileReadException;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.exception.EmptyDeckException;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Deck
 */

public class Deck {
    private List<District> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }
    public void resetDeck(){
        // Specify the path to your JSON file
        try {
            JsonNode tempNode = Utils.parseJsonFromFile
                    ("src/main/resources/init_database.json");
            cards.addAll(Utils.convertJsonNodeToDistrictList(tempNode.path("Game").path("Districts")));
        } catch (IOException e) {
            throw new JsonFileReadException("Error reading JSON file", e);
        }
    }

    public void setDeck(List<District> cards) {
        this.cards = cards;
    }

    public List<District> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Les cartes dans le deck sont : \n");
        for (District district : this.cards) {
            str.append(district).append('\n');
        }
        str.append("\n");
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

    public void putCardAtBottom(District card) {
        this.cards.add(0, card);
    }
}