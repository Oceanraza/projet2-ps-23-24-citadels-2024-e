package fr.cotedazur.univ.polytech.startingpoint.city;

import java.util.Objects;
import java.util.Optional;

/**
 * Classe représentant un quartier dans le jeu.
 */
public class District {
    private DistrictColor color;
    private final int price;
    private final String name;
    private final int bonusPoints;
    private Optional<Integer> builtAtTurn;

    /**
     * Constructeur de la classe District.
     *
     * @param name  le nom du quartier.
     * @param price le prix du quartier.
     * @param color la couleur du quartier.
     */
    public District(String name, int price, DistrictColor color) {
        this.color = color;
        this.price = price;
        this.name = name;
        this.bonusPoints = 0;
    }

    /**
     * Constructeur de la classe District avec des points bonus.
     * @param name le nom du quartier.
     * @param price le prix du quartier.
     * @param color la couleur du quartier.
     * @param bonusPoints les points bonus du quartier.
     */
    public District(String name, int price, DistrictColor color, int bonusPoints) {
        this.color = color;
        this.price = price;
        this.name = name;
        this.bonusPoints = bonusPoints;
    }

    /**
     * Retourne le nom du quartier.
     * @return le nom du quartier.
     */
    public String getName(){
        return name;
    }

    /**
     * Retourne le prix du quartier.
     * @return le prix du quartier.
     */
    public int getPrice(){
        return price;
    }

    /**
     * Retourne la couleur du quartier.
     * @return la couleur du quartier.
     */
    public DistrictColor getColor() {
        return color;
    }

    /**
     * Définit la couleur du quartier.
     * @param districtColor la nouvelle couleur du quartier.
     */
    public void setColor(DistrictColor districtColor) {
        color = districtColor;
    }

    /**
     * Retourne les points bonus du quartier.
     * @return les points bonus du quartier.
     */
    public int getBonusPoints() {
        return bonusPoints;
    }

    /**
     * Retourne le tour où le quartier a été construit.
     * @return le tour où le quartier a été construit.
     */
    public Optional<Integer> getTurnBuilt() {
        return builtAtTurn;
    }

    /**
     * Définit le tour où le quartier a été construit.
     * @param turn le tour où le quartier a été construit.
     */
    public void setTurnBuilt(int turn) {
        this.builtAtTurn = Optional.of(turn);
    }

    /**
     * Vérifie si deux quartiers sont égaux en comparant leurs noms.
     * @param o l'objet à comparer avec le quartier actuel.
     * @return vrai si les deux quartiers sont égaux, faux sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        District d = (District) o;
        return getName().equals(d.getName());
    }

    /**
     * Retourne le code de hachage du quartier.
     * @return le code de hachage du quartier.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, price, color);
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du quartier.
     * @return une représentation sous forme de chaîne de caractères du quartier.
     */
    @Override
    public String toString() {
        return name + "-" + price + "-" + color;
    }
}