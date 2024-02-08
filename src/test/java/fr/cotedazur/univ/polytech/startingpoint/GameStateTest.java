package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameStateTest {
    Player player1;
    Player player2;
    GameState gameState;
    List<Player> players;

    @BeforeEach
    void setUp() {
        InGameLogger.setup();
        InGameLogger.setGlobalLogLevel(Level.OFF);

        gameState = new GameState();
        player1 = new Bot("Bot 1");
        player2 = new Bot("Bot 2");
        players = new ArrayList<>();
    }
    @Test
    void finishedGameTest() {
        players.add(player1);
        players.add(player2);

        assertFalse(gameState.isGameFinished(players));

        for (int i = 0; i < 8; i++) {
            String name = "District" + i;
            player1.getCity().addDistrict(new District(name, 0, DistrictColor.TRADE), gameState);
        }
        assertTrue(gameState.isFinished(player1));
        assertTrue(gameState.isGameFinished(players));
    }
}
