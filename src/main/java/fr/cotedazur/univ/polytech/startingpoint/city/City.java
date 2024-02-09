package fr.cotedazur.univ.polytech.startingpoint.city;

import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.exception.DistrictAlreadyBuiltException;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une ville dans le jeu.
 */
public class City {
    private final List<District> districtsBuilt;

    /**
     * Constructeur de la classe City.
     */
    public City() {
        this.districtsBuilt = new ArrayList<>();
    }

    /**
     * Retourne la liste des quartiers construits dans la ville.
     *
     * @return la liste des quartiers construits.
     */
    public List<District> getDistrictsBuilt() {
        return districtsBuilt;
    }

    /**
     * Vérifie si un quartier n'est pas déjà construit dans la ville.
     *
     * @param district le quartier à vérifier.
     * @return vrai si le quartier n'est pas déjà construit, faux sinon.
     */
    public boolean isNotBuilt(District district) {
        if (districtsBuilt.isEmpty()) {
            return true;
        }
        for (District d : districtsBuilt) {
            if (d.getName().equals(district.getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ajoute un quartier à la ville.
     *
     * @param district  le quartier à ajouter.
     * @param gameState l'état actuel du jeu.
     * @throws DistrictAlreadyBuiltException si le quartier est déjà construit.
     */
    public void addDistrict(District district, GameState gameState) {
        if (isNotBuilt(district)) {
            districtsBuilt.add(district);
            district.setTurnBuilt(gameState.getTurn());
        } else throw new DistrictAlreadyBuiltException("Ce district a deja ete construit");
    }

    /**
     * Détruit un quartier de la ville.
     *
     * @param districtToDestroy le quartier à détruire.
     */
    public void destroyDistrict(District districtToDestroy) {
        for (District d : districtsBuilt) {
            if (d.equals(districtToDestroy)) {
                districtsBuilt.remove(d);
                return;
            }
        }
    }

    /**
     * Vérifie si un quartier est présent dans la ville.
     *
     * @param districtName le nom du quartier à vérifier.
     * @return vrai si le quartier est présent, faux sinon.
     */
    public boolean containsDistrict(String districtName) {
        for (District d : districtsBuilt) {
            if (d.getName().equals(districtName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la ville.
     *
     * @return une représentation sous forme de chaîne de caractères de la ville.
     */
    @Override
    public String toString() {
        StringBuilder city = new StringBuilder();
        for (int i = 0; i < districtsBuilt.size(); i++) {
            city.append(districtsBuilt.get(i).getName());
            if (i < districtsBuilt.size() - 1) {
                city.append(", ");
            }
        }
        city.append(".");
        return city.toString();
    }

    /**
     * Retourne la taille de la ville (nombre de quartiers construits).
     *
     * @return la taille de la ville.
     */
    public int size() {
        return districtsBuilt.size();
    }
}