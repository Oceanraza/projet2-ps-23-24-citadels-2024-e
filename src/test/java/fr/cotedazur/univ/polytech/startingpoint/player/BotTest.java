package fr.cotedazur.univ.polytech.startingpoint.player;

import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import org.junit.jupiter.api.BeforeEach;

import java.util.logging.Level;

class BotTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    Game game;
    GameState gameState;
    Bot bot;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setup();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
        gameState = new GameState();
        bot = new Bot("Bot");
    }


    /* Vieux test, à actualiser
    @Test
    void testPlay() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        bot.setGold(0);
        bot.play(game, gameState);
        String expectedOutput = bot.getName() + " pioche ";
        assert(outContent.toString().startsWith(expectedOutput));

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        District cardDrawn = bot.getDistrictsInHand().get(0);
        int cardDrawnPrice = cardDrawn.getPrice();
        bot.setGold(cardDrawnPrice - 2);
        assertFalse(bot.getDistrictsInHand().isEmpty());
        bot.play(game, gameState);
        expectedOutput = bot.getName() + " prend deux pièces d'or." + LINE_SEPARATOR +
                bot.getName() + " a construit le quartier " + cardDrawn.getName() + LINE_SEPARATOR;
        assertEquals(expectedOutput, outContent.toString());
    }
     */
}