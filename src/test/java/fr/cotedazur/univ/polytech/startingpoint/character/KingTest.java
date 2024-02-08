package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.card.King;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KingTest {
    Game game;
    King king;
    Player kingPlayer;

    @BeforeEach
    void setUp() {
        InGameLogger.setup();
        InGameLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
        king = new King();
        kingPlayer = new Bot("kingPlayer");
    }

    @Test
    void kingInformationsTest() {
        assertEquals(4, king.getRunningOrder());
        assertEquals(DistrictColor.NOBLE, king.getColor());
    }

    @Test
    void crownOwnerTest() {
        // Add players to the game
        game.setPlayers(kingPlayer);
        // Set characters to players
        kingPlayer.setGameCharacter(king);

        king.specialEffect(kingPlayer, game);
        assertEquals(kingPlayer, game.getCrown().getOwner());
    }
}
