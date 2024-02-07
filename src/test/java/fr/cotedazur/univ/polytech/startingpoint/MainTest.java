package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.card.Bishop;
import fr.cotedazur.univ.polytech.startingpoint.character.card.King;
import fr.cotedazur.univ.polytech.startingpoint.character.card.Merchant;
import fr.cotedazur.univ.polytech.startingpoint.character.card.Warlord;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static fr.cotedazur.univ.polytech.startingpoint.Main.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    Player player;
    GameState gameState;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setupDemo();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        player = new Bot("Test");
        gameState = new GameState();
    }

    @Test
    void testRunningOrder() {
        Player firstPlayer = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Player thirdPlayer = new Bot("Player 3");
        Player fourthPlayer = new Bot("Player 4");
        Bishop bishop = new Bishop();
        King king = new King();
        Warlord warlord = new Warlord();
        Merchant merchant = new Merchant();
        Game newGame = new Game();

        // Create list of players
        newGame.setPlayers(firstPlayer, secondPlayer, thirdPlayer, fourthPlayer);

        // Players choose a character
        firstPlayer.setGameCharacter(bishop);
        secondPlayer.setGameCharacter(king);
        thirdPlayer.setGameCharacter(merchant);
        fourthPlayer.setGameCharacter(warlord);

        // Set running order
        List<Player> runningOrder = newGame.getRunningOrder();
        assertEquals(firstPlayer.getName(), runningOrder.get(1).getName());
        assertEquals(secondPlayer.getName(), runningOrder.get(0).getName());
        assertEquals(thirdPlayer.getName(), runningOrder.get(2).getName());
        assertEquals(fourthPlayer.getName(), runningOrder.get(3).getName());
    }
    @Test
    void testRunningOrderInTwoRounds(){
        Player firstPlayer = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Player thirdPlayer = new Bot("Player 3");
        Player fourthPlayer = new Bot("Player 4");
        Bishop bishop = new Bishop();
        King king = new King();
        Warlord warlord = new Warlord();
        Merchant merchant = new Merchant();
        Game newGame = new Game();

        // Create list of players
        newGame.setPlayers(firstPlayer, secondPlayer, thirdPlayer, fourthPlayer);

        // Players choose a character
        firstPlayer.setGameCharacter(bishop);
        secondPlayer.setGameCharacter(king);
        thirdPlayer.setGameCharacter(merchant);
        fourthPlayer.setGameCharacter(warlord);

        // Set running order
        List<Player> runningOrder = newGame.getRunningOrder();
        assertEquals(secondPlayer.getName(), runningOrder.get(0).getName()); //has crown
        assertEquals(firstPlayer.getName(), runningOrder.get(1).getName());
        assertEquals(thirdPlayer.getName(), runningOrder.get(2).getName());
        assertEquals(fourthPlayer.getName(), runningOrder.get(3).getName());

        // Round 2
        // Players choose a character
        secondPlayer.setGameCharacter(bishop);
        firstPlayer.setGameCharacter(king); //has crown
        thirdPlayer.setGameCharacter(merchant);
        fourthPlayer.setGameCharacter(warlord);
        // Set running order
        runningOrder = newGame.getRunningOrder();
        assertEquals(firstPlayer.getName(), runningOrder.get(0).getName()); // has crown
        assertEquals(secondPlayer.getName(), runningOrder.get(1).getName());
        assertEquals(thirdPlayer.getName(), runningOrder.get(2).getName());
        assertEquals(fourthPlayer.getName(), runningOrder.get(3).getName());
    }

    @Test
    void testCalculateScores() { //We can test with two players since adding more players doesn't change the scores
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        List<Player> players = Arrays.asList(firstBuilder, secondPlayer);

        for (int i = 0; i < 8; i++) {
            String name = "District" + i;
            firstBuilder.getCity().addDistrict(new District(name, i, DistrictColor.TRADE), gameState);
        }

        secondPlayer.getCity().addDistrict(new District("marchand", 1, DistrictColor.TRADE), gameState);
        secondPlayer.getCity().addDistrict(new District("militaire", 1, DistrictColor.MILITARY), gameState);
        secondPlayer.getCity().addDistrict(new District("religieux", 1, DistrictColor.RELIGIOUS), gameState);
        secondPlayer.getCity().addDistrict(new District("noble", 1, DistrictColor.NOBLE), gameState);
        secondPlayer.getCity().addDistrict(new District("special", 1, DistrictColor.SPECIAL), gameState);

        List<Player> scoredPlayers = calculateScores(players, firstBuilder, new GameState());

        assertEquals(34, scoredPlayers.get(0).getScore());
        assertEquals(10, scoredPlayers.get(1).getScore());

        // If the scores are equal
        GameCharacter king = new King();
        GameCharacter eveque = new Bishop();
        firstBuilder.setGameCharacter(eveque);
        secondPlayer.setGameCharacter(king);
        firstBuilder.setScore(0);
        secondPlayer.setScore(0);
        assertEquals(calculateScores(players, firstBuilder, new GameState()).get(0), firstBuilder);
    }


    @Test
    void testAnnounceWinner() { //We can test with two players since adding more players doesn't change the ending message
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        List<Player> players = Arrays.asList(firstBuilder, secondPlayer);

        for (int i = 0; i < 8; i++) {
            String name = "District" + i;
            firstBuilder.getCity().addDistrict(new District(name, i, DistrictColor.TRADE), gameState); // 34 points
            secondPlayer.getCity().addDistrict(new District(name, i, DistrictColor.TRADE), gameState); // 32 points
        }

        announceWinner(players, firstBuilder, new GameState());
        assertEquals(34, firstBuilder.getScore());
        assertEquals(32, secondPlayer.getScore());
    }

    @Test
    void testSortEvenPlayers(){
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Player thirdPlayer = new Bot("Player 3");
        Player fourthPlayer = new Bot("Player 4");
        List<Player> players = Arrays.asList(firstBuilder,thirdPlayer,fourthPlayer,secondPlayer);

        for (int i = 0; i < 8; i++) {
            String name = "District" + i;
            firstBuilder.getCity().addDistrict(new District(name, i, DistrictColor.TRADE), gameState); // 34 points
            secondPlayer.getCity().addDistrict(new District(name, i, DistrictColor.TRADE), gameState); // 32 points
            thirdPlayer.getCity().addDistrict(new District(name, i, DistrictColor.TRADE), gameState); // 32 points
            fourthPlayer.getCity().addDistrict(new District(name, i, DistrictColor.TRADE), gameState); // 32 points
        }
        firstBuilder.setGameCharacter(new Warlord());
        secondPlayer.setGameCharacter(new King());
        thirdPlayer.setGameCharacter(new Bishop());
        fourthPlayer.setGameCharacter(new Merchant());

        firstBuilder.setScore(firstBuilder.getScore()-2); //il passe a 32 points, égalité avec les autres
        calculateScores(players, firstBuilder, new GameState());

        assertEquals(34, firstBuilder.getScore());
        assertEquals(32, secondPlayer.getScore());
        assertEquals(32, thirdPlayer.getScore());
        assertEquals(32, fourthPlayer.getScore());
    }
    @Test
    public void testGetPlacement() {
        List<Player> players = new ArrayList<>();
        players.add(new Bot("Alice"));
        Player p2 = new Bot("Bob");
        players.add(p2);
        players.add(new Bot("Charlie"));
        players.add(new Bot("David"));

        int placement = Main.getPlacement(players, p2);

        // Verify if the correct placement is returned
        assertEquals(2, placement);

        // Test with a player not in the list
        Player notInList = new Bot("Eve");
        int nonExistentPlacement = Main.getPlacement(players, notInList);

        // Verify if -1 is returned for a player not in the list
        assertEquals(-1, nonExistentPlacement);
    }

    @Test
    void jCommanderTest() {
        String[] args2x1000games;
        String[] args1game;

        args2x1000games = new String[]{"--2thousands"};
        assertEquals(1000, jCommander(args2x1000games));
        args1game = new String[]{"--demo"};
        assertEquals(1, jCommander(args1game));
    }
}
