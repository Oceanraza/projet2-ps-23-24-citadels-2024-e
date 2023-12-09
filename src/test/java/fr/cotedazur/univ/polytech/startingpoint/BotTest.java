package fr.cotedazur.univ.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    Game game;
    Bot bot;

    @BeforeEach
    void setUp() {
        game = new Game() {
        };
        bot = new Bot("Bot") {
        };
    }

    @Test
    void testPlay() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        bot.setGold(0);
        bot.play(game);
        String expectedOutput = bot.getName() + " pioche." + LINE_SEPARATOR;
        assertEquals(expectedOutput, outContent.toString());

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
}