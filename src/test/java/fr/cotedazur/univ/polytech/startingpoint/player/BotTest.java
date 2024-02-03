package fr.cotedazur.univ.polytech.startingpoint.player;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import org.junit.jupiter.api.BeforeEach;

class BotTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    Game game;
    Bot bot;

    @BeforeEach
    void setUp() {
        game = new Game();
        bot = new Bot("Bot");
    }

    /*
    @Test
    void testPlay() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        bot.setGold(0);
        bot.play(game);
        String expectedOutput = bot.getName() + " pioche ";
        assert(outContent.toString().startsWith(expectedOutput));

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        District cardDrawn = bot.getDistrictsInHand().get(0);
        int cardDrawnPrice = cardDrawn.getPrice();
        bot.setGold(cardDrawnPrice - 2);
        assertFalse(bot.districtsInHand.isEmpty());
        bot.play(game);
        expectedOutput = bot.getName() + " prend deux pièces d'or." + LINE_SEPARATOR +
                bot.getName() + " a construit le quartier " + cardDrawn.getName() + LINE_SEPARATOR;
        assertEquals(expectedOutput, outContent.toString());
    }
     */
}