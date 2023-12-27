package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.Character1;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.King;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.players.Bot;
import fr.cotedazur.univ.polytech.startingpoint.players.Player;
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
            player.getDistrictsBuilt().add(new District("test", 0, DistrictColor.marchand));
        }
        assertTrue(Main.isFinished(player));
    }

    @Test
    void testRunningOrder() {
        Player firstPlayer = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Character1 character1 = new Character1();
        King king = new King();
        Game newGame = new Game();

        // Create list of players
        newGame.setPlayers(firstPlayer, secondPlayer);

        // Players choose a character
        firstPlayer.setGameCharacter(king);
        secondPlayer.setGameCharacter(character1);

        // Set running order
        List<Player> runningOrder = newGame.setRunningOrder();
        assertEquals(secondPlayer.getName(), runningOrder.get(0).getName());
        assertEquals(firstPlayer.getName(), runningOrder.get(1).getName());
    }

    @Test
    void testRunningOrder2() {
        Player firstPlayer = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Character1 character1 = new Character1();
        King king = new King();
        Game newGame = new Game();

        // Create list of players
        newGame.setPlayers(firstPlayer, secondPlayer);

        // Players choose a character
        firstPlayer.setGameCharacter(character1);
        secondPlayer.setGameCharacter(king);

        // Set running order
        List<Player> runningOrder = newGame.setRunningOrder();
        assertEquals(firstPlayer.getName(), runningOrder.get(0).getName());
        assertEquals(secondPlayer.getName(), runningOrder.get(1).getName());
    }

    @Test
    void testRunningOrderInTwoRounds() {
        Player firstPlayer = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Character1 character1 = new Character1();
        King king = new King();
        Game newGame = new Game();
        List<Player> runningOrder;

        // Create list of players
        newGame.setPlayers(firstPlayer, secondPlayer);

        // Round 1
        // Players choose a character
        firstPlayer.setGameCharacter(king);
        secondPlayer.setGameCharacter(character1);

        // Set running order
        runningOrder = newGame.setRunningOrder();
        assertEquals(secondPlayer.getName(), runningOrder.get(0).getName());
        assertEquals(firstPlayer.getName(), runningOrder.get(1).getName());

        // Round 2
        // Players choose a character
        firstPlayer.setGameCharacter(character1);
        secondPlayer.setGameCharacter(king);

        // Set running order
        runningOrder = newGame.setRunningOrder();
        assertEquals(firstPlayer.getName(), runningOrder.get(0).getName());
        assertEquals(secondPlayer.getName(), runningOrder.get(1).getName());
    }

    @Test
    void testCalculateScores() {
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        List<Player> players = Arrays.asList(firstBuilder, secondPlayer);

        for (int i = 0; i < 8; i++) {
            firstBuilder.getDistrictsBuilt().add(new District("test", i, DistrictColor.marchand));
        }

        secondPlayer.getDistrictsBuilt().add(new District("marchand", 1, DistrictColor.marchand));
        secondPlayer.getDistrictsBuilt().add(new District("militaire", 1, DistrictColor.militaire));
        secondPlayer.getDistrictsBuilt().add(new District("religieux", 1, DistrictColor.religieux));
        secondPlayer.getDistrictsBuilt().add(new District("noble", 1, DistrictColor.noble));
        secondPlayer.getDistrictsBuilt().add(new District("special", 1, DistrictColor.special));

        List<Player> scoredPlayers = calculateScores(players, firstBuilder);

        assertEquals(34, scoredPlayers.get(0).getScore());
        assertEquals(10, scoredPlayers.get(1).getScore());

        // If the scores are equal
        GameCharacter king = new King();
        GameCharacter character1 = new Character1();
        firstBuilder.setGameCharacter(king);
        secondPlayer.setGameCharacter(character1);
        firstBuilder.setScore(0);
        secondPlayer.setScore(0);
        assertEquals(calculateScores(players, firstBuilder).get(0), firstBuilder);
    }
    @Test
    void testAnnounceWinner() {
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        List<Player> players = Arrays.asList(firstBuilder, secondPlayer);

        for (int i = 0; i < 8; i++) {
            firstBuilder.getDistrictsBuilt().add(new District("test", i, DistrictColor.marchand)); // 34 points
            secondPlayer.getDistrictsBuilt().add(new District("test", i, DistrictColor.marchand)); // 32 points
        }

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.announceWinner(players, firstBuilder);

        String expectedOutput = "Player 1 : 34 points"+ LINE_SEPARATOR+ "Player 2 : 32 points"+ LINE_SEPARATOR+ "Player 1 gagne la partie avec 34 points !"+ LINE_SEPARATOR;
        assertEquals(expectedOutput, outContent.toString());
    }
}