package fr.cotedazur.univ.polytech.startingpoint.board;

import com.fasterxml.jackson.databind.JsonNode;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.exception.EmptyDeckException;
import fr.cotedazur.univ.polytech.startingpoint.exception.JsonFileReadException;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant le deck de cartes du jeu.
 */
public class Deck {
    private List<District> cards;

    /**
     * Constructeur par défaut. Initialise une nouvelle liste de cartes.
     */
    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Réinitialise le deck en lisant les cartes à partir d'un fichier JSON.
     */
    public void resetDeck() {
        // Spécifiez le chemin vers votre fichier JSON
        try {
            JsonNode tempNode = Utils.parseJsonFromFile
                    ("src/main/resources/init_database.json");
            cards.addAll(Utils.convertJsonNodeToDistrictList(tempNode.path("Game").path("Districts")));
        } catch (IOException e) {
            throw new JsonFileReadException("Erreur lors de la lecture du fichier JSON", e);
        }
    }

    /**
     * Retourne la liste des cartes dans le deck.
     *
     * @return la liste des cartes dans le deck.
     */
    public List<District> getCards() {
        return cards;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du deck.
     *
     * @return une représentation sous forme de chaîne de caractères du deck.
     */
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
     * Ajoute un quartier au deck.
     *
     * @param district le quartier à ajouter.
     */
    public void addDistrict(District district) {
        if (district != null) {
            this.cards.add(district);
        }
    }

    /**
     * Mélange le deck.
     */
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    /**
     * Pioche une carte du deck.
     *
     * @return la carte piochée.
     * @throws EmptyDeckException si le deck est vide.
     */
    public District drawCard() {
        if (this.isEmpty()) {
            throw new EmptyDeckException("Le deck est vide");
        }
        return this.cards.remove(this.size() - 1);
    }

    /**
     * Vérifie si le deck est vide.
     *
     * @return vrai si le deck est vide, faux sinon.
     */
    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    /**
     * Retourne le nombre de cartes dans le deck.
     *
     * @return le nombre de cartes dans le deck.
     */
    public int size() {
        return this.cards.size();
    }

    /**
     * Place une carte au bas du deck.
     *
     * @param card la carte à placer au bas du deck.
     */
    public void putCardAtBottom(District card) {
        this.cards.add(0, card);
    }
}