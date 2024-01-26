package fr.cotedazur.univ.polytech.startingpoint.board;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.city.District;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<District> cards;
    private ArrayList<District> discardPile;

    private Game game;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void setDeck(ArrayList<District> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Les cartes dans le deck sont : \n");
        for (District district : this.cards) {
            str.append(district.toString()).append('\n');
        }
        return str.toString();
    }


    public void addDistrict(District district) {
        if (district != null) {
            this.cards.add(district);
        }
    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    public District drawCard() {
        if (this.isEmpty()) {
            throw new EmptyDeckException("Deck is empty");
        }
        return this.cards.remove(this.size() - 1);
    }

    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    public int size() {
        return this.cards.size();
    }


    public void discardDistrict(District district) {
        this.cards.remove(district);
        this.discardPile.add(district);
    }


    public ArrayList<District> getCards() {
        return this.cards;
    }
}