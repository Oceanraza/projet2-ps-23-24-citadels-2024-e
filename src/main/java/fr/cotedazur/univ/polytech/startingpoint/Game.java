package fr.cotedazur.univ.polytech.startingpoint;

import com.fasterxml.jackson.databind.JsonNode;
import fr.cotedazur.univ.polytech.startingpoint.board.Deck;
import fr.cotedazur.univ.polytech.startingpoint.character.*;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static fr.cotedazur.univ.polytech.startingpoint.CitadelsLogger.LOGGER;

/**
 * The Game class is the main class of the game. It contains the deck, the crown, the players and the characters.
 * It also contains the methods to start the game, to shuffle the characters and to give the cards to the players.
 */
public class Game {
    private static final int START_CARDS_NUMBER = 4;
    private Deck deck;
    private Crown crown;
    private List<Player> players;
    private List<GameCharacter> allCharacters;
    private List<GameCharacter> charactersInGame;
    private List<GameCharacter> availableChars;

    Assassin assassin;
    Thief thief;
    King king;
    Bishop bishop;
    Merchant merchant;
    Warlord warlord;
    Magician magician;
    Architect architect;

    public Game() {
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

    public List<GameCharacter> getCharactersInGame() {
        return charactersInGame;
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

    private void removeCharactersInGame() {
        for (int i = 0; i < 2; i++) {
            int indexCharacter;
            GameCharacter cha;

            indexCharacter = Utils.generateRandomNumber(charactersInGame.size() - 1);
            cha = charactersInGame.get(indexCharacter);

            // The king must be available for the players
            while (cha.getRole().equals(GameCharacterRole.KING)) {
                indexCharacter = Utils.generateRandomNumber(charactersInGame.size() - 1);
                cha = charactersInGame.get(indexCharacter);
            }
            charactersInGame.remove(cha);
            LOGGER.info(cha.getRole().toStringLeOrL() + " ne sera pas joue ce tour");
        }
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
        thief = new Thief();
        king = new King();
        bishop = new Bishop();
        merchant = new Merchant();
        warlord = new Warlord();
        magician = new Magician();
        architect = new Architect();

        // Create the list of characters
        allCharacters.add(assassin);
        allCharacters.add(thief);
        allCharacters.add(king);
        allCharacters.add(bishop);
        allCharacters.add(merchant);
        allCharacters.add(warlord);
        allCharacters.add(magician);
        allCharacters.add(architect);

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
        // Reset the previous lists
        availableChars.clear();
        charactersInGame = new ArrayList<>(allCharacters);

        // Remove 2 characters from the list of characters in game
        removeCharactersInGame();

        availableChars = new ArrayList<>(charactersInGame);
    }

    private void giveStartingCards() {
        for (Player player : players) {
            for (int i = 0; i < START_CARDS_NUMBER; i++) {
                player.getDistrictsInHand().add(deck.drawCard());
            }
        }
    }

    public void printAvailableCharacters() {
        LOGGER.info("Les personnages disponibles sont : ");
        for (GameCharacter temp : availableChars) {
            LOGGER.info(temp.getRole() + " ");
        }
    }

    public void charSelectionFiller(){
        for (Player p: players){
            if (p.getGameCharacter() == null){
                Bot p2 = (Bot) p;
                LOGGER.info(p2.toString());
                //We create a new variable p2 to cast p to Bot each time
                //Good to note that you can't just cast the whole List
                p2.botAlgo.chooseCharacterAlgorithm(this);}
        }
    }

    // Removes characters of players
    public void resetChars() {
        for (Player p : players) {
            p.setGameCharacter(null);
        }
    }

    // Removes attacks on characters
    public void resetCharsState() {
        for (GameCharacter cha : allCharacters) {
            cha.setIsAlive(true);
            cha.setAttacker(null);
        }
    }

    public List<Player> getSortedPlayersByScore() {
        List<Player> sortedPlayersByScore = new ArrayList<>();
        for (Player player : getPlayers()) {
            player.calculateAndSetScore();
            sortedPlayersByScore.add(player);
        }
        Comparator<Player> playerComparator = Comparator
                .comparingInt(Player::getScore)
                .reversed();
        sortedPlayersByScore.sort(playerComparator);
        return sortedPlayersByScore;
    }

    public List<Player> getSortedPlayersByScoreForWarlord() {
        List<Player> sortedPlayersByScore = getSortedPlayersByScore();
        for (Player player : sortedPlayersByScore) {
            // Warlord can't destroy bishop's districts
            if (player.getGameCharacter().getRole().equals(GameCharacterRole.BISHOP)) {
                sortedPlayersByScore.remove(player);
                return sortedPlayersByScore;
            }
        }
        return sortedPlayersByScore;
    }

    public List<GameCharacter> getKillableCharacters() {
        List<GameCharacter> killableCharacters = new ArrayList<>(getCharactersInGame());
        for (GameCharacter cha : killableCharacters) {
            // Assassin can't kill himself
            if (cha.getRole().equals(GameCharacterRole.ASSASSIN)) {
                killableCharacters.remove(cha);
                break;
            }
        }
        return killableCharacters;
    }

    public List<GameCharacter> getCharactersThatCanBeStolen() {
        // Thief can't steal from the Assassin
        List<GameCharacter> charactersThatCanBeStolen = new ArrayList<>(getKillableCharacters());
        List<GameCharacter> temp = new ArrayList<>(getKillableCharacters());

        for (GameCharacter cha: temp) {
            // Thief can't steal from himself
            if (cha.getRole().equals(GameCharacterRole.THIEF)) {
                charactersThatCanBeStolen.remove(cha);
            }
            // Thief can't steal from a dead character
            else if (!cha.getIsAlive()) {
                LOGGER.info(cha.getRole().toStringLeOrL() + " a ete assassine. Il ne peut pas etre vole");
                charactersThatCanBeStolen.remove(cha);
            }
        }

        return charactersThatCanBeStolen;
    }

    public District drawCard(Player player) {
        District drawnDistrict = deck.drawCard();
        LOGGER.info(player.getName() + " pioche la carte " + drawnDistrict + ".");
        player.getDistrictsInHand().add(drawnDistrict);
        return drawnDistrict;
    }

    public Deck getDeck() {
        return deck;
    }

    @Override
    public String toString() {
        return deck.toString();
    }
}
