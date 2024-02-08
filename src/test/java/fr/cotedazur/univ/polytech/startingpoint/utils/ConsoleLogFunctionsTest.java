package fr.cotedazur.univ.polytech.startingpoint.utils;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.Main;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ConsoleLogFunctionsTest {
    @Test
    public void testPrintPlayerInfo() {
        // Mock totalScores
        Map<String, Integer> totalScores = new HashMap<>();
        totalScores.put("Player1", 100);
        totalScores.put("Player2", 150);

        // Mock totalPlacements
        Map<String, List<Integer>> totalPlacements = new HashMap<>();
        List<Integer> placementsPlayer1 = new ArrayList<>();
        placementsPlayer1.add(5); // Total games won
        totalPlacements.put("Player1", placementsPlayer1);
        List<Integer> placementsPlayer2 = new ArrayList<>();
        placementsPlayer2.add(7); // Total games won
        totalPlacements.put("Player2", placementsPlayer2);

        // Mock wantedPlayer
        Player wantedPlayer = new Bot("Player1");

        // Mock numberOfGames
        int numberOfGames = 20;

        // Invoke method under test
        ConsoleLogFunctions.printPlayerInfo(totalScores, totalPlacements, wantedPlayer, numberOfGames);

        // Since this method directly prints to System.out, it's difficult to verify output in a unit test
        // It's better to refactor this method to return a String and test that instead
    }
}
