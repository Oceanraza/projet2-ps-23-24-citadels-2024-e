package fr.cotedazur.univ.polytech.startingpoint;

import static fr.cotedazur.univ.polytech.startingpoint.Main.*;

import java.util.*;
import java.util.stream.Collectors;

import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.Character1;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.King;

public class Game {
    private ArrayList<District> gameDeck;
    private Crown crown;
    private List<Player> players;
    private Map<String, GameCharacter> allCharacters;
    private ArrayList<GameCharacter> availableChars;

    // Getter
    public ArrayList<District> getGameDeck() {
        return gameDeck;
    }
    public Crown getCrown() {
        return crown;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public ArrayList<GameCharacter> getAvailableChars() {
        availableCharacters();
        return availableChars;
    }

    // Setter
    public void setPlayers(Player firstBot, Player secondBot) { // Add players to the list of players
        players.add(firstBot);
        players.add(secondBot);
    }

    public List<Player> setRunningOrder() { // Set running order depending on the running order of the characters
        return this.getPlayers().stream()
                .sorted(Comparator.comparingInt(player -> player.getGameCharacter().getRunningOrder()))
                .collect(Collectors.toList());
    }

    // Add and remove
    public void addDistrictsInGameDeck(District district, int n) {
        for (int i = 0; i < n; i++) {
            gameDeck.add(district);
        }
    }

    public void removeDistrictInGameDeck(District district) {
        gameDeck.remove(district);
    }

    public void removeAvailableChar(GameCharacter cha) {
        availableChars.remove(cha);
    }

    public Game() {
        init();
    }

    public void init(){
        gameDeck = new ArrayList<>();
        allCharacters = new HashMap<>();
        availableChars = new ArrayList<>();

        // Adding religieux districts
        addDistrictsInGameDeck(new District("Temple", 1, DistrictColor.religieux), 3);
        addDistrictsInGameDeck(new District("Eglise", 2, DistrictColor.religieux), 4);
        addDistrictsInGameDeck(new District("Monastere", 3, DistrictColor.religieux), 3);
        addDistrictsInGameDeck(new District("Cathedrale", 5, DistrictColor.religieux), 2);

        addDistrictsInGameDeck(new District("Manoir", 3, DistrictColor.noble), 5);
        addDistrictsInGameDeck(new District("Chateau", 4, DistrictColor.noble), 4);
        addDistrictsInGameDeck(new District("Palais", 5, DistrictColor.noble), 2);

        addDistrictsInGameDeck(new District("Taverne", 1, DistrictColor.marchand), 5);
        addDistrictsInGameDeck(new District("Echoppe", 2, DistrictColor.marchand), 3);
        addDistrictsInGameDeck(new District("Marche", 2, DistrictColor.marchand), 4);
        addDistrictsInGameDeck(new District("Comptoir", 3, DistrictColor.marchand), 3);
        addDistrictsInGameDeck(new District("Port", 4, DistrictColor.marchand), 3);
        addDistrictsInGameDeck(new District("Hotel de ville", 5, DistrictColor.marchand), 2);

        addDistrictsInGameDeck(new District("Tour de guet", 1, DistrictColor.militaire), 3);
        addDistrictsInGameDeck(new District("Prison", 2, DistrictColor.militaire), 3);
        addDistrictsInGameDeck(new District("Caserne", 3, DistrictColor.militaire), 3);
        addDistrictsInGameDeck(new District("Forteresse", 5, DistrictColor.militaire), 2);

        addDistrictsInGameDeck(new District("Donjon", 3, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Laboratoire", 5, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Observatoire", 5, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Cour des miracles", 2, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Cimetiere", 5, DistrictColor.special), 1);

        // Create a crown
        crown = new Crown();

        // Create the list of players
        players = new ArrayList<Player>();

        // Creates the characters
        allCharacters.put("Roi", new King());
        allCharacters.put("Personnage 1", new Character1());
    }

    public District drawCard() {
        Random random = new Random();
        District cardDrawn = gameDeck.get(random.nextInt(gameDeck.size() - 1));
        gameDeck.remove(cardDrawn);
        return cardDrawn;
    }

    public void shuffleChars(int numberOfPlayers) { // numberOfPlayers needs to be used for automatic code but due to
                                                    // time reasons, it's postponed to a later day.
        while (!availableChars.isEmpty()) {
            availableChars.remove(0);
        }
        availableChars.add(allCharacters.get("Roi"));
        availableChars.add(allCharacters.get("Personnage 1"));
    }

    public void availableCharacters() {
        System.out.println("Les personnages disponibles sont : ");
        for (GameCharacter temp : availableChars) {
            System.out.print(temp.getName() + " ");
        }
        System.out.println(" ");
    }

    public String toString() {
        StringBuilder str = new StringBuilder("Les cartes dans le deck sont : \n");
        for (District district : gameDeck) {
            str.append(district.toString()).append('\n');
        }
        return str.toString();
    }
}
