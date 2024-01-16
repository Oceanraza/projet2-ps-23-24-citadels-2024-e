package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.character.*;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static fr.cotedazur.univ.polytech.startingpoint.Main.calculateScores;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    Player player;

    @BeforeEach
    void setUp() {
        player = new Bot("Test");
    }

    @Test
    void testIsFinished() {
        assertFalse(Main.isFinished(player));
        for (int i = 0; i < 8; i++) {
            String name = "District" + i;
            player.getCity().addDistrict(new District(name, 0, DistrictColor.MARCHAND));
        }
        assertTrue(Main.isFinished(player));
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
        List<Player> runningOrder = newGame.setRunningOrder();
        assertEquals(firstPlayer.getName(), runningOrder.get(1).getName());
        assertEquals(secondPlayer.getName(), runningOrder.get(0).getName());
        assertEquals(thirdPlayer.getName(), runningOrder.get(2).getName());
        assertEquals(fourthPlayer.getName(), runningOrder.get(3).getName());
    }
    @Test
    void testRunningOrderInTwoRounds() {
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
        List<Player> runningOrder = newGame.setRunningOrder();
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
        runningOrder = newGame.setRunningOrder();
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
            firstBuilder.getCity().addDistrict(new District(name, i, DistrictColor.MARCHAND));
        }

        secondPlayer.getCity().addDistrict(new District("marchand", 1, DistrictColor.MARCHAND));
        secondPlayer.getCity().addDistrict(new District("militaire", 1, DistrictColor.MILITAIRE));
        secondPlayer.getCity().addDistrict(new District("religieux", 1, DistrictColor.RELIGIEUX));
        secondPlayer.getCity().addDistrict(new District("noble", 1, DistrictColor.NOBLE));
        secondPlayer.getCity().addDistrict(new District("special", 1, DistrictColor.SPECIAL));

        List<Player> scoredPlayers = calculateScores(players, firstBuilder);

        assertEquals(34, scoredPlayers.get(0).getScore());
        assertEquals(10, scoredPlayers.get(1).getScore());

        // If the scores are equal
        GameCharacter king = new King();
        GameCharacter eveque = new Bishop();
        firstBuilder.setGameCharacter(eveque);
        secondPlayer.setGameCharacter(king);
        firstBuilder.setScore(0);
        secondPlayer.setScore(0);
        assertEquals(calculateScores(players, firstBuilder).get(0), firstBuilder);
    }
    @Test
    void testAnnounceWinner() { //We can test with two players since adding more players doesn't change the ending message
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        List<Player> players = Arrays.asList(firstBuilder, secondPlayer);

        for (int i = 0; i < 8; i++) {
            String name = "District" + i;
            firstBuilder.getCity().addDistrict(new District(name, i, DistrictColor.MARCHAND)); // 34 points
            secondPlayer.getCity().addDistrict(new District(name, i, DistrictColor.MARCHAND)); // 32 points
        }

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.announceWinner(players, firstBuilder);

        String expectedOutput = "Player 1 : 34 points"+ LINE_SEPARATOR+ "Player 2 : 32 points"+ LINE_SEPARATOR+ "Player 1 gagne la partie avec 34 points !"+ LINE_SEPARATOR;
        assertEquals(expectedOutput, outContent.toString());
    }
    @Test
    void testSortEvenPlayers(){
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Player thirdPlayer = new Bot("Player 3");
        Player fourthPlayer = new Bot("Player 4");
        List<Player> players = Arrays.asList(firstBuilder,thirdPlayer,fourthPlayer,secondPlayer);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        for (int i = 0; i < 8; i++) {
            String name = "District" + i;
            firstBuilder.getCity().addDistrict(new District(name, i, DistrictColor.MARCHAND)); // 34 points
            secondPlayer.getCity().addDistrict(new District(name, i, DistrictColor.MARCHAND)); // 32 points
            thirdPlayer.getCity().addDistrict(new District(name, i, DistrictColor.MARCHAND)); // 32 points
            fourthPlayer.getCity().addDistrict(new District(name, i, DistrictColor.MARCHAND)); // 32 points
        }
        firstBuilder.setGameCharacter(new Warlord());
        secondPlayer.setGameCharacter(new King());
        thirdPlayer.setGameCharacter(new Bishop());
        fourthPlayer.setGameCharacter(new Merchant());

        firstBuilder.setScore(firstBuilder.getScore()-2); //il passe a 32 points, égalité avec les autres
        calculateScores(players,firstBuilder);
        for (Player p : players) {
            System.out.println(p.getName() + ", score : " + p.getScore());
        }
        String expectedOutput = "Player 1, score : 34" + LINE_SEPARATOR
                + "Player 4, score : 32" + LINE_SEPARATOR
                + "Player 3, score : 32" + LINE_SEPARATOR
                + "Player 2, score : 32" + LINE_SEPARATOR;

        assertEquals(expectedOutput, outContent.toString());
    }

}