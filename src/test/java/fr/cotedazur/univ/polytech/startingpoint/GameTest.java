package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.character.card.King;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameTest {
    Game game;
    Bot bot;
    King king;

    @BeforeEach
    void setUp() {
        InGameLogger.setup();
        InGameLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
    }

    @Test
    void testGameInit() {
        assertEquals(0, game.getPlayers().size());
        assertEquals(65, game.getDeck().size());

        game.init();
        game.shuffleCharacters();

        assertEquals(6, game.getCharactersInGame().size());
        assertEquals(6, game.getAvailableChars().size());
    }

    @Test
    void testGameList() {
        game.init();

        // Shuffle characters
        game.availableChars.clear();
        game.charactersInGame = new ArrayList<>(game.allCharacters);
        // Remove warlord
        game.charactersInGame.remove(7);
        // Remove architect
        game.charactersInGame.remove(6);
        game.availableChars = new ArrayList<>(game.charactersInGame);

        assertEquals(5, game.getKillableCharacters().size());
        assertEquals(4, game.getCharactersThatCanBeStolen().size());

        // If a character is killed, he can be stolen
        game.bishop.setIsAlive(false);
        assertEquals(3, game.getCharactersThatCanBeStolen().size());
    }

    @Test
    void testChooseCharacter() {
        bot = new Bot("Bot");
        king = mock(King.class);

        game.setPlayers(bot);
        bot.setGameCharacter(king);
        assertEquals(king, bot.getGameCharacter());

        game.resetChars();
        assertNull(bot.getGameCharacter());
    }

    @Test
    void testKillCharacter() {
        bot = mock(Bot.class);

        game.init();
        game.shuffleCharacters();

        game.bishop.setIsAlive(false);
        game.bishop.setAttacker(bot);

        assertFalse(game.bishop.getIsAlive());
        assertEquals(bot, game.bishop.getAttacker());

        game.resetCharsState();

        assertTrue(game.bishop.getIsAlive());
        assertNull(game.bishop.getAttacker());
    }

    // Crown gets to the king even if he is killed
    @Test
    void crownOwnerTest() {
        bot = mock(Bot.class);
        king = new King();

        game.setPlayers(bot);
        bot.setGameCharacter(king);
        king.setAttacker(bot);
        game.playerKilled(king, bot);
        assertEquals(bot, game.getCrownOwner());
    }
}
