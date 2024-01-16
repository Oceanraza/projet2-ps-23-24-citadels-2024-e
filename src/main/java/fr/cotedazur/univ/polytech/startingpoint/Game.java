package fr.cotedazur.univ.polytech.startingpoint;


import java.util.*;
import java.util.stream.Collectors;

import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.*;


import java.util.*;
import java.util.stream.Collectors;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.players.*;


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
    public void setPlayers(Player... bots) { // Add players to the list of players
        players.addAll(Arrays.asList(bots));
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

        addDistrictsInGameDeck(new District("Cour des miracles", 2, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Donjon", 3, DistrictColor.special), 2);
        addDistrictsInGameDeck(new District("Laboratoire", 5, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Manufacture", 5, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Observatoire", 5, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Cimetiere", 5, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Bibliotheque", 6, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Ecole de magie", 6, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Universite", 6, DistrictColor.special), 1);
        addDistrictsInGameDeck(new District("Dracoport", 6, DistrictColor.special), 1);

        // Create a crown
        crown = new Crown();

        // Create the list of players
        players = new ArrayList<Player>();

        // Creates the characters
        allCharacters.put("Roi", new King());
        allCharacters.put("Marchand", new Marchand());
        allCharacters.put("Eveque", new Eveque());
        allCharacters.put("Condottiere", new Condottiere());
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
        availableChars.add(allCharacters.get("Marchand"));
        availableChars.add(allCharacters.get("Eveque"));
        availableChars.add(allCharacters.get("Condottiere"));
    }

    public void availableCharacters() {
        System.out.println("Les personnages disponibles sont : ");
        for (GameCharacter temp : availableChars) {
            System.out.print(temp.getName() + " ");
        }
        System.out.println(" ");
    }
    public void charSelectionFiller(){
        for (Player p: players){
            if (p.getGameCharacter() == null){
                Bot p2 = (Bot) p;
                System.out.println(p2);
                //We create a new variable p2 to cast p to Bot each time
                //Good to note that you can't just cast the whole List
                p2.botAlgo.chooseCharacterAlgorithm(this);}
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder("Les cartes dans le deck sont : \n");
        for (District district : gameDeck) {
            str.append(district.toString()).append('\n');
        }
        return str.toString();
    }

    public void setAllCharsToNull() {
        for (Player  p: players){
            p.setGameCharacter(null);
        }
    }
    public ArrayList<Player> getSortedPlayersByScore(){
        ArrayList<Player> sortedPlayersByScore = new ArrayList<>();
        for (Player p : getPlayers()){
            p.calculateScore();
            sortedPlayersByScore.add(p);
        }
        Comparator<Player> playerComparator = Comparator
                .comparingInt(Player::getScore)
                .reversed();
        sortedPlayersByScore.sort(playerComparator);
        return sortedPlayersByScore;
    }
}
