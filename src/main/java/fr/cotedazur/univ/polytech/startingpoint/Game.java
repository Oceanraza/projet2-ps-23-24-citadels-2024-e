package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.board.Deck;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.character.card.*;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger.*;

/**
 * The Game class is the main class of the game. It contains the deck, the crown, the players and the characters.
 * It also contains the methods to start the game, to shuffle the characters and to give the cards to the players.
 */
public class Game {
    public static final int CITY_SIZE_TO_WIN = 8;
    private static final int START_CARDS_NUMBER = 4;

    private Deck deck = new Deck();
    private Crown crown;
    private List<Player> players;
    protected List<GameCharacter> allCharacters;
    protected List<GameCharacter> charactersInGame;
    protected List<GameCharacter> availableChars;

    protected Assassin assassin;
    protected Thief thief;
    protected King king;
    protected Bishop bishop;
    protected Merchant merchant;
    protected Warlord warlord;
    protected Magician magician;
    protected Architect architect;

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
    public boolean containsAvailableRole(GameCharacterRole role) {
        return availableChars.stream()
                .anyMatch(gameCharacter -> gameCharacter.getRole().equals(role));
    }

    public boolean containsAvailableRoles(GameCharacterRole... roles) { // Check if the available characters contain at least one of the roles
        List<GameCharacterRole> rolesList = Arrays.asList(roles);
        return availableChars.stream()
                .anyMatch(gameCharacter -> rolesList.contains(gameCharacter.getRole()));
    }


    public List<GameCharacter> getCharactersInGame() {
        return charactersInGame;
    }

    // Setter
    public void setPlayers(Player... bots) { // Add players to the list of players
        players.addAll(Arrays.asList(bots));
    }

    public List<Player> getRunningOrder() { // Set running order depending on the running order of the characters
        return this.getPlayers().stream()
                .sorted(Comparator.comparingInt(player -> player.getGameCharacter().getRunningOrder()))
                .toList();
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
            String wontBePlayedMessage = cha.getRole().toStringLeOrLUpperCase() + " ne sera pas joue ce tour";
            LOGGER.info(wontBePlayedMessage);
        }
    }

    // Init starts off the game by creating the deck, the crown, the players and the characters
    public void init() {
        deck.resetDeck();
        allCharacters = new ArrayList<>();
        availableChars = new ArrayList<>();

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
                player.addDistrictInHand(deck.drawCard());
            }
        }
    }

    public void printAvailableCharacters() {
        LOGGER.info(COLOR_GREEN + "Les personnages disponibles sont : " + COLOR_RESET);
        String availableCharsMessage;
        for (GameCharacter temp : availableChars) {
            availableCharsMessage = COLOR_GREEN + temp.getRole() + COLOR_RESET;
            LOGGER.info(availableCharsMessage);
        }
    }

    public void charSelectionFiller(int startingPos) {
        int i = startingPos + 1;
        while (i != (startingPos)) {
            if (i == players.size()) {
                i = 0;
                if (startingPos == 0) {
                    return;
                }
            }
            Bot p = (Bot) players.get(i);
            if (p.getGameCharacter() == null) {
                String playerInfo = p.toString();
                LOGGER.info(playerInfo);
                p.getBotAlgo().chooseCharacterAlgorithm(this);
            }
            i++;

        }
    }

    public Bot getCrownOwner() {
        return (Bot) this.getCrown().getOwner();
    }

    public Bot printCrownOwner() {
        Bot crownOwner = getCrownOwner();
        String crownOwnerMessage = "La couronne appartient a " + (crownOwner != null ? crownOwner.getName() : "personne");
        LOGGER.info(crownOwnerMessage);
        return crownOwner;
    }

    public void characterSelection(Bot crownOwner, int cOpos) { //cO = crownOwner
        if (crownOwner != null) {
            String crownOwnerInfos = crownOwner.toString();
            LOGGER.info(crownOwnerInfos);
            crownOwner.getBotAlgo().chooseCharacterAlgorithm(this);
        } else {
            cOpos = 0; //There's no crownOwner, therefore the first player starts
            Bot p1 = (Bot) players.get(0);
            LOGGER.info(p1.toString());
            p1.getBotAlgo().chooseCharacterAlgorithm(this);
            //The first player is treated here to keep charSelectionFiller logic
        }
        charSelectionFiller(cOpos);
    }

    public void playerKilled(GameCharacter characterKilled, Player playerKilled) {
        String isKilledMessage = "\n" + characterKilled.getRole().toStringLeOrLUpperCase() + " a ete tue par " + characterKilled.getAttacker().getName();
        String cannotPlayMessage = playerKilled.getName() + " ne pourra pas jouer ce tour !";
        LOGGER.info(isKilledMessage);
        LOGGER.info(cannotPlayMessage);
        // If the king is killed, he gets the crown at the end of this turn
        if (characterKilled.getRole() == GameCharacterRole.KING) {
            this.getCrown().setOwner(playerKilled);
            LOGGER.info("Il recuperera la couronne a la fin de ce tour");
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

        for (GameCharacter cha : temp) {
            // Thief can't steal from himself
            if (cha.getRole().equals(GameCharacterRole.THIEF)) {
                charactersThatCanBeStolen.remove(cha);
            }
            // Thief can't steal from a dead character
            else if (!cha.getIsAlive()) {
                String deadCharacterMessage = cha.getRole().toStringLeOrLUpperCase() + " est mort. Il ne peut pas etre vole";
                LOGGER.info(deadCharacterMessage);
                charactersThatCanBeStolen.remove(cha);
            }
        }

        return charactersThatCanBeStolen;
    }

    public District drawCard(Player player) {
        District drawnDistrict = deck.drawCard();
        String drawCardMessage = player.getName() + " pioche la carte " + drawnDistrict + ".";
        LOGGER.info(drawCardMessage);
        player.addDistrictInHand(drawnDistrict);
        return drawnDistrict;
    }

    public Deck getDeck() {
        return deck;
    }

    @Override
    public String toString() {
        return deck.toString();
    }

    public void resetGame() {
        init();
    }

    public Player getPlayerWithMostDistricts() {
        return players.stream()
                .max((p1, p2) -> Integer.compare(p1.getCity().size(), p2.getCity().size()))
                .orElse(null);
    }

    public Player getRichestPlayer() {
        return players.stream().
                max((p1, p2) -> Integer.compare(p1.getGold(), p2.getGold()))
                .orElse(null);
    }

    public double averageCitySize() {
        return getPlayers().stream().mapToInt(player -> player.getCity().size()).average().getAsDouble();
    }

    public Player getPlayerWith6Districts() {
        return players.stream()
                .filter(player -> player.getCity().size() == 6)
                .findFirst()
                .orElse(null);
    }

    public Player getPlayerWithMostCardInHand() {
        return players.stream()
                .max((p1, p2) -> Integer.compare(p1.getDistrictsInHand().size(), p2.getDistrictsInHand().size()))
                .orElse(null);
    }

    public Player getPlayerWithLowestDistrictPrice() {
        Player playerWithLowestDistrictPrice = null;
        int lowestPrice = Integer.MAX_VALUE;

        for (Player player : players) {
            for (District district : player.getDistrictsInHand()) {
                if (district.getPrice() < lowestPrice) {
                    lowestPrice = district.getPrice();
                    playerWithLowestDistrictPrice = player;
                }
            }
        }

        return playerWithLowestDistrictPrice;
    }
}
