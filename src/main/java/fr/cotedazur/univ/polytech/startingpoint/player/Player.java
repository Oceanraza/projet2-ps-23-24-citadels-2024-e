package fr.cotedazur.univ.polytech.startingpoint.player;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.board.Deck;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.City;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;

import java.util.*;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

/**
 * Classe abstraite représentant un joueur dans le jeu.
 */
public abstract class Player {
    private final List<District> districtsInHand;
    private final City city;
    private int gold;
    private final String name;
    private int score;
    private GameCharacter gameCharacter;
    private final Map<DistrictColor, Integer> numberOfDistrictsByColor;

    /**
     * Constructeur de la classe Player.
     *
     * @param name le nom du joueur.
     */
    protected Player(String name) {
        this.name = name;
        districtsInHand = new ArrayList<>();
        city = new City();
        gold = 2;
        score = 0;
        gameCharacter = null;
        numberOfDistrictsByColor = new EnumMap<>(DistrictColor.class);
        numberOfDistrictsByColor.put(DistrictColor.MILITARY, 0);
        numberOfDistrictsByColor.put(DistrictColor.NOBLE, 0);
        numberOfDistrictsByColor.put(DistrictColor.SPECIAL, 0);
        numberOfDistrictsByColor.put(DistrictColor.RELIGIOUS, 0);
        numberOfDistrictsByColor.put(DistrictColor.TRADE, 0);
    }

    // Getters
    public List<District> getDistrictsInHand() {
        return districtsInHand;
    }

    public City getCity() {
        return city;
    }

    public int getGold() {
        return gold;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public GameCharacter getGameCharacter() {
        return gameCharacter;
    }

    public String getCharacterName() {
        return gameCharacter.getRole().getRoleName();
    }

    // Setters
    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setGameCharacter(GameCharacter gameCharacter) {
        this.gameCharacter = gameCharacter;
    }

    // Fonctions pour ajouter ou supprimer
    public void addDistrictInHand(District district) {
        if (district != null) {
            this.districtsInHand.add(district);
        }
    }

    public void addDistrictBuilt(District district, GameState gameState) {
        numberOfDistrictsByColor.replace(
                district.getColor(),
                numberOfDistrictsByColor.get(district.getColor()) + 1);
        this.city.addDistrict(district, gameState);
    }

    public void removeGold(int g) {
        gold -= g;
    }

    public Map<DistrictColor, Integer> getNumberOfDistrictsByColor() {
        return numberOfDistrictsByColor;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    /**
     * Méthode abstraite pour jouer un tour de jeu.
     *
     * @param game      l'état actuel du jeu.
     * @param gameState l'état du jeu.
     */
    public abstract void play(Game game, GameState gameState);

    // Fonction pour construire un quartier
    public boolean buildDistrict(District district, GameState gameState) {
        // Vérifie si le joueur a assez d'or pour construire le quartier. Si c'est le cas, il est construit.
        if (gold >= district.getPrice() && this.city.isNotBuilt(district)) {
            addDistrictBuilt(district, gameState);
            gold -= district.getPrice();
            districtsInHand.remove(district);
            String buildDistrictMessage = COLOR_GREEN + getName() + " a construit le quartier " + district.getName() + COLOR_RESET;
            LOGGER.info(buildDistrictMessage);
            return true;
        }
        return false;
    }

    public boolean districtsInHandAreBuilt() {
        for (District district : districtsInHand) {
            if (city.isNotBuilt(district)) {
                return false;
            }
        }
        return true;
    }

    public int calculateScore() {
        int tempScore = getGold();
        City playerCity = this.getCity();
        Set<DistrictColor> districtColors = new HashSet<>();
        for (District district : playerCity.getDistrictsBuilt()) {
            tempScore += district.getPrice();
            tempScore += district.getBonusPoints();
            districtColors.add(district.getColor());
        }
        if (districtColors.size() == DistrictColor.values().length) { // Si le joueur a construit tous les quartiers
            // de couleurs
            tempScore += 3;
        }
        return tempScore;
    }

    public void calculateAndSetScore() {
        this.setScore(this.calculateScore());
    }

    public Optional<District> getLowestDistrict(List<District> districtList) {
        if (districtList.isEmpty()) {
            return Optional.empty();
        }
        District minPriceDistrict = districtList.stream()
                .min(Comparator.comparingDouble(District::getPrice))
                .orElse(null);
        return Optional.of(minPriceDistrict);
    }

    public Optional<District> getLowestDistrictBuilt() {
        return getLowestDistrict(getCity().getDistrictsBuilt());
    }

    public Optional<District> getLowestDistrictInHand() {
        return getLowestDistrict(getDistrictsInHand());
    }

    public void removeFromHandAndPutInDeck(Deck deck, District cardToDiscard) {
        if (this.getDistrictsInHand().contains(cardToDiscard)) {
            this.getDistrictsInHand().remove(cardToDiscard);
            deck.putCardAtBottom(cardToDiscard);
        } else {
            LOGGER.info("La carte n'est pas dans la main du joueur");
        }
    }

    @Override
    public String toString() {
        if (gameCharacter == null) {
            return "\nC'est au tour de " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composee de: "
                    + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " d'or(s)\n" +
                    (!city.getDistrictsBuilt().isEmpty() ? "Et il a deja pose: " + city : "Il n'a pas pose de quartiers.");
        }

        // Si un personnage est choisi, on spécifie le personnage
        return "\nC'est au tour " + gameCharacter.getRole().toStringDuOrDeL() + " : " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composee de: "
                + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " d'or(s)\n" +
                (!city.getDistrictsBuilt().isEmpty() ? "Et il a deja pose: " + city : "Il n'a pas pose de quartiers.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player p = (Player) o;
        return getName().equals(p.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}