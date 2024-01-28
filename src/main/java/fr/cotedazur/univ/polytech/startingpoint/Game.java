package fr.cotedazur.univ.polytech.startingpoint;

import java.util.*;
import java.util.stream.Collectors;

import fr.cotedazur.univ.polytech.startingpoint.board.Deck;
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
    private ArrayList<Player> players;
    private ArrayList<GameCharacter> allCharacters;
    private ArrayList<GameCharacter> charactersInGame;
    private ArrayList<GameCharacter> availableChars;

    Assassin assassin;
    King king;
    Bishop bishop;
    Merchant merchant;
    Warlord warlord;
    Magician magician;

    public Game(){
        init();
    }

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

    // Init starts off the game by creating the deck, the crown, the players and the characters
    public void init() {
        deck = new Deck();
        allCharacters = new ArrayList<>();
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
        assassin = new Assassin();
        king = new King();
        bishop = new Bishop();
        merchant = new Merchant();
        warlord = new Warlord();
        magician = new Magician();

        // Create the list of characters
        allCharacters.add(assassin);
        allCharacters.add(king);
        allCharacters.add(bishop);
        allCharacters.add(merchant);
        allCharacters.add(warlord);
        allCharacters.add(magician);

        // Give the cards to the players
        startCardGame();
    }

    public void startCardGame() {
        // Shuffle the deck
        deck.shuffle();

        // Give 4 cards to each player
        giveStartingCards();
    }

    public void shuffleCharacters() {
        int characterRemoved1;
        int characterRemoved2;

        // Reset the previous lists
        availableChars.clear();
        charactersInGame = new ArrayList<> (allCharacters);

        // Remove 2 characters from the list of characters in game
        characterRemoved1 = Utils.generateRandomNumber(charactersInGame.size() - 1);
        // The king must be available for the players
        while (charactersInGame.get(characterRemoved1).getRole().equals(GameCharacterRole.KING)) {
            characterRemoved1 = Utils.generateRandomNumber(charactersInGame.size() - 1);
        }

        System.out.println("Le " + charactersInGame.get(characterRemoved1) + " ne sera pas joué ce tour");
        charactersInGame.remove(characterRemoved1);

        // Later, we will remove 2 characters from the charactersInGame list :
        // characterRemoved2 = Utils.generateRandomNumber(charactersInGame.size() - 1);
        // while (charactersInGame.get(characterRemoved2).getRole().getRoleName().equals("Roi")) {
        //    characterRemoved1 = Utils.generateRandomNumber(charactersInGame.size() - 1);
        //}
        // System.out.println("\nLe " + charactersInGame.get(characterRemoved2) + " ne sera pas joué ce tour");
        // charactersInGame.remove(characterRemoved2);

        availableChars = new ArrayList<> (charactersInGame);

    private void giveStartingCards() {
        for (Player player : players) {
            for (int i = 0; i < START_CARDS_NUMBER; i++) {
                player.getDistrictsInHand().add(deck.drawCard());
            }
        }
    }

    public void printAvailableCharacters() {
        System.out.println("Les personnages disponibles sont : ");
        for (GameCharacter temp : availableChars) {
            System.out.print(temp.getRole() + " ");
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

    // Removes characters of players
    public void resetChars() {
        for (Player p: players) {
            p.setGameCharacter(null);
        }
    }

    // Removes attacks on characters
    public void resetCharsState() {
        for (GameCharacter cha: allCharacters) {
            cha.setIsAlive(true);
            cha.setAttacker(null);
        }
    }

    public List<Player> getSortedPlayersByScoreForWarlord(){
        List<Player> sortedPlayersByScore = new ArrayList<>();
        for (Player p : getPlayers()){
            if (!p.getGameCharacter().getRole().equals(GameCharacterRole.BISHOP)){
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

    public District drawCard(Player player) {
        District drawnDistrict = deck.drawCard();
        System.out.println(player.getName() + " pioche le " + drawnDistrict);
        player.getDistrictsInHand().add(drawnDistrict);
        return drawnDistrict;
    }

    public Deck getDeck() {
        return deck;
    }

    public ArrayList<GameCharacter> getKillableCharacters() {
        ArrayList<GameCharacter> killableCharacters = charactersInGame;
        killableCharacters.remove(assassin);
        return killableCharacters;
    }

    public void killCharacter(Player assassin, GameCharacterRole killedCharacter) {
        GameCharacter targetCharacter;
        for (Player target: getPlayers()) {
            targetCharacter = target.getGameCharacter();
            if (targetCharacter.getRole().equals(killedCharacter)) {
                targetCharacter.setIsAlive(false);
                targetCharacter.setAttacker(assassin);
                return;
            }
        }
        System.out.println("Le personnage tué n'est pas en jeu !");
    }

    public String toString() {
        return deck.toString();
    }
}
