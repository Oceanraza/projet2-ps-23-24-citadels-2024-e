package fr.cotedazur.univ.polytech.startingpoint;

<<<<<<< HEAD
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.*;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.players.Bot;
import fr.cotedazur.univ.polytech.startingpoint.players.Player;
=======
import fr.cotedazur.univ.polytech.startingpoint.characters.Character1;
import fr.cotedazur.univ.polytech.startingpoint.characters.King;
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c
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
        player = new Player("Test") {
        };
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
<<<<<<< HEAD
    void testRunningOrder() {
        Player firstPlayer = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Player thirdPlayer = new Bot("Player 3");
        Player fourthPlayer = new Bot("Player 4");
        Eveque eveque = new Eveque();
        King king = new King();
        Condottiere condottiere = new Condottiere();
        Marchand marchand = new Marchand();
        Game newGame = new Game();

        // Create list of players
        newGame.setPlayers(firstPlayer, secondPlayer, thirdPlayer, fourthPlayer);

        // Players choose a character
        firstPlayer.setGameCharacter(eveque);
        secondPlayer.setGameCharacter(king);
        thirdPlayer.setGameCharacter(marchand);
        fourthPlayer.setGameCharacter(condottiere);

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
        Eveque eveque = new Eveque();
        King king = new King();
        Condottiere condottiere = new Condottiere();
        Marchand marchand = new Marchand();
        Game newGame = new Game();

        // Create list of players
        newGame.setPlayers(firstPlayer, secondPlayer, thirdPlayer, fourthPlayer);

        // Players choose a character
        firstPlayer.setGameCharacter(eveque);
        secondPlayer.setGameCharacter(king);
        thirdPlayer.setGameCharacter(marchand);
        fourthPlayer.setGameCharacter(condottiere);

        // Set running order
        List<Player> runningOrder = newGame.setRunningOrder();
        assertEquals(secondPlayer.getName(), runningOrder.get(0).getName()); //has crown
        assertEquals(firstPlayer.getName(), runningOrder.get(1).getName());
        assertEquals(thirdPlayer.getName(), runningOrder.get(2).getName());
        assertEquals(fourthPlayer.getName(), runningOrder.get(3).getName());

        // Round 2
        // Players choose a character
        secondPlayer.setGameCharacter(eveque);
        firstPlayer.setGameCharacter(king); //has crown
        thirdPlayer.setGameCharacter(marchand);
        fourthPlayer.setGameCharacter(condottiere);
        // Set running order
        runningOrder = newGame.setRunningOrder();
        assertEquals(firstPlayer.getName(), runningOrder.get(0).getName()); // has crown
        assertEquals(secondPlayer.getName(), runningOrder.get(1).getName());
        assertEquals(thirdPlayer.getName(), runningOrder.get(2).getName());
        assertEquals(fourthPlayer.getName(), runningOrder.get(3).getName());
    }

    @Test
    void testCalculateScores() { //We can test with two players since adding more players doesn't change the scores
=======
    void testCalculateScores() {
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c
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

        assertEquals(34, scoredPlayers.get(0).score);
        assertEquals(10, scoredPlayers.get(1).score);

        // If the scores are equal
<<<<<<< HEAD
        GameCharacter king = new King();
        GameCharacter eveque = new Eveque();
        firstBuilder.setGameCharacter(eveque);
        secondPlayer.setGameCharacter(king);
=======
        Characters king = new King();
        Characters character1 = new Character1();
        firstBuilder.chooseCharacter(king);
        secondPlayer.chooseCharacter(character1);
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c
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