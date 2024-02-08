package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.character.card.King;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

class RichardAlgoTest {

    @BeforeEach
    void setUp() {
        CitadelsLogger.setGlobalLogLevel(Level.OFF);
    }

    @Test
    void collectGoldBeforeBuildChoiceTest() {
        Bot bot = new Bot("Bot", new RichardAlgo());
        bot.setGold(0);
        bot.setGameCharacter(new King());
        District district = new District("District 1", 1, DistrictColor.NOBLE);

        bot.addDistrictInHand(district);
        assertEquals(1, bot.getDistrictsInHand().size());
        assertEquals(district, bot.getDistrictsInHand().get(0));
        assertEquals(0, bot.getGold());
        assertTrue(bot.getLowestDistrictInHand().isPresent());
        assertEquals(district, bot.getLowestDistrictInHand().get());
        assertFalse(bot.getBotAlgo().collectGoldBeforeBuildChoice());

        bot.setGold(10);
        assertFalse(bot.getBotAlgo().collectGoldBeforeBuildChoice());
    }
}