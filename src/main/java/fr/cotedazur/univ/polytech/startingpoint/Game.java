package fr.cotedazur.univ.polytech.startingpoint;

import java.util.*;
import java.util.stream.Collectors;

import fr.cotedazur.univ.polytech.startingpoint.board.Deck;
import fr.cotedazur.univ.polytech.startingpoint.character.*;


import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.*;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;


public class Game {
    private static final int START_CARDS_NUMBER = 4;
    private Deck deck;
    private Crown crown;
    private List<Player> players;
    private Map<String, GameCharacter> allCharacters;
    private ArrayList<GameCharacter> availableChars;
    private int number;
    // Getter

    public Crown getCrown() {
        return crown;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public List<GameCharacter> getAvailableChars() {
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

    public void removeAvailableChar(GameCharacter cha) {
        availableChars.remove(cha);
    }

    public Game(){
        init();
    }

    // Init starts off the game by creating the deck, the crown, the players and the characters
    public void init(){
        deck = new Deck();
        allCharacters = new HashMap<>();
        availableChars = new ArrayList<>();

            // Specify the path to your JSON file
        try {
            JsonNode tempNode = Utils.parseJsonFromFile
                    ("src/main/resources/init_database.json");
            deck = Utils.convertJsonNodeToDistrictList(tempNode.path("Game").path("Districts"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Create a crown
        crown = new Crown();

        // Create the list of players
        players = new ArrayList<>();

        // Creates the characters
        allCharacters.put("Roi", new King());
        allCharacters.put("Marchand", new Merchant());
        allCharacters.put("Eveque", new Bishop());
        allCharacters.put("Condottiere", new Warlord());

        // Give the cards to the players
        startCardGame();
    }

    public void startCardGame() {
        // Shuffle the deck
        deck.shuffle();

        // Give 4 cards to each player
        giveStartingCards();

    }

    private void giveStartingCards() {
        for (Player player : players) {
            for (int i = 0; i < START_CARDS_NUMBER; i++) {
                player.getDistrictsInHand().add(deck.drawCard());
            }
        }
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

    public void printAvailableCharacters() {
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
        return deck.toString();
    }

    public void setAllCharsToNull() {
        for (Player  p: players){
            p.setGameCharacter(null);
        }
    }
    public List<Player> getSortedPlayersByScoreForWarlord(){
        List<Player> sortedPlayersByScore = new ArrayList<>();
        for (Player p : getPlayers()){
            if (!p.getGameCharacter().getName().equals("Eveque")){
                p.calculateAndSetScore();
                sortedPlayersByScore.add(p);
            }
        }
        Comparator<Player> playerComparator = Comparator
                .comparingInt(Player::getScore)
                .reversed();
        sortedPlayersByScore.sort(playerComparator);
        return sortedPlayersByScore;
    }

    public District drawCard() {
        return deck.drawCard();
    }

    public Deck getDeck() {
        return deck;
    }

}
