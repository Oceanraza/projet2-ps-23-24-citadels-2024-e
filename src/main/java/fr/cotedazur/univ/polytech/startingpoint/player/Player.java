package fr.cotedazur.univ.polytech.startingpoint.player;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.board.Deck;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.City;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;

import java.util.*;

public abstract class Player {
    private final List<District> districtsInHand;
    private final City city;
    private int gold;
    private final String name;
    private int score;
    private GameCharacter gameCharacter;
    private final Map<DistrictColor, Integer> numberOfDistrictsByColor;
    private Deck deck;

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

    // Getter
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

    // Setter
    public void setGold(int gold) {
        this.gold = gold;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void setGameCharacter(GameCharacter gameCharacter) {
        this.gameCharacter = gameCharacter;
    }

    // Functions to add or remove
    public void addDistrictInHand(District district) {
        this.districtsInHand.add(district);
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

    public Map<DistrictColor, Integer> getNumberOfDistrictsByColor() {return numberOfDistrictsByColor;}

    public void addGold(int gold) {
        this.gold += gold;
    }

    public abstract void play(Game game, GameState gameState);
    // Function to build a district
    public boolean buildDistrict(District district, GameState gameState) {
        // Checks if the player has enough gold to build the district. If so it is
        // built.
        if (gold >= district.getPrice() && this.city.isNotBuilt(district)) {
            addDistrictBuilt(district, gameState);
            gold -= district.getPrice();
            districtsInHand.remove(district);
            System.out.println(getName() + " a construit le quartier " + district.getName());
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

    public int calculateScore(){
        int tempScore = getGold();
        City playerCity = this.getCity();
        Set<DistrictColor> districtColors = new HashSet<>();
        for (District district : playerCity.getDistrictsBuilt()) {
            tempScore += district.getPrice();
            tempScore += district.getBonusPoints();
            districtColors.add(district.getColor());
        }
        if (districtColors.size() == DistrictColor.values().length) { // If the player has built all the district
            // colors
            tempScore += 3;
        }
        return tempScore;
    }

    public void calculateAndSetScore() {
        this.setScore(this.calculateScore());
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

    public Optional<District> getLowestDistrict(){
        List<District> sortedDistrictByScore = getCity().getDistrictsBuilt();
        if (sortedDistrictByScore.isEmpty()){return Optional.empty();}
        District minPriceDistrict = sortedDistrictByScore.stream()
                .min(Comparator.comparingDouble(District::getPrice))
                .orElse(null);
        return Optional.of(minPriceDistrict);
    }

    @Override
    public String toString() {
        if (gameCharacter == null) {
            return "\nC'est au tour de " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composée de: "
                    + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " d'or(s)\n" +
                    (!city.getDistrictsBuilt().isEmpty() ? "Et il a déjà posé: " + city : "Il n'a pas posé de quartiers.");
        }

        // If a character is chosen, we specify the character
        return "\nC'est au tour " + gameCharacter.getRole().toStringDuOrDeL() + " : " + name + "\n" + (!districtsInHand.isEmpty() ? "Et sa main est composée de: "
                + districtsInHand : "Sa main est vide. ") + "\n" + "Il a " + gold + " d'or(s)\n" +
                (!city.getDistrictsBuilt().isEmpty() ? "Et il a déjà posé: " + city : "Il n'a pas posé de quartiers.");
    }

    public void moveCardInDeck(District card, Deck deck) {
        if (this.getDistrictsInHand().contains(card)) {
            this.getDistrictsInHand().remove(card);
            deck.addDistrict(card);
        } else {
            System.out.println("La carte n'est pas dans la main du joueur");
        }
    }
}
