package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.character.card.King;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EinsteinAlgoTest {
    private Game game;
    private EinsteinAlgo einsteinAlgo;
    private Bot bot;
    private District district1;
    private District district2;
    private District district3;
    private List<District> threeCards;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setupDemo();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
        einsteinAlgo = new EinsteinAlgo();
        bot = mock(Bot.class); // Create a mock Bot instead of a real instance
        einsteinAlgo.setBot(bot); // Set the bot instance variable of the EinsteinAlgo object

        district1 = new District("District 1", 5, DistrictColor.NOBLE);
        district2 = new District("District 2", 3, DistrictColor.TRADE);
        district3 = new District("District 3", 4, DistrictColor.SPECIAL);
    }

    @Test
    void shouldChooseCardWithLowestPrice() {
        threeCards = new ArrayList<>();
        threeCards.add(district1);
        threeCards.add(district2);
        threeCards.add(district3);

        when(bot.getGold()).thenReturn(4);

        einsteinAlgo.botChoosesCard(game, threeCards);
        assertFalse(threeCards.contains(district2));
    }

    @Test
    void shouldNotChooseCardIfNotAffordable() {
        threeCards = new ArrayList<>();
        threeCards.add(district1);
        threeCards.add(district2);
        threeCards.add(district3);

        when(bot.getGold()).thenReturn(2);

        einsteinAlgo.botChoosesCard(game, threeCards);
        assertEquals(3, threeCards.size());
    }

    @Test
    void shouldChooseToDrawCardWhenHandIsEmpty() {
        when(bot.getDistrictsInHand()).thenReturn(Collections.emptyList());

        int choice = einsteinAlgo.startOfTurnChoice();

        assertEquals(2, choice);
    }

    @Test
    void shouldChooseToDrawCardWhenAllDistrictsInHandAreBuilt() {
        when(bot.districtsInHandAreBuilt()).thenReturn(true);

        int choice = einsteinAlgo.startOfTurnChoice();

        assertEquals(2, choice);
    }

    @Test
    void shouldChooseToTakeGoldWhenHandIsNotEmptyAndNotAllDistrictsAreBuilt() {
        List<District> districtsInHand = Arrays.asList(
                new District("District 1", 5, DistrictColor.NOBLE)
        );

        when(bot.getDistrictsInHand()).thenReturn(districtsInHand);
        when(bot.districtsInHandAreBuilt()).thenReturn(false);
        GameCharacter gameCharacter = mock(GameCharacter.class); // Create a mock GameCharacter
        when(bot.getGameCharacter()).thenReturn(gameCharacter); // Set the GameCharacter of the bot
        when(gameCharacter.getRole()).thenReturn(GameCharacterRole.ASSASSIN); // Set the role of the GameCharacter
        int choice = einsteinAlgo.startOfTurnChoice();

        assertEquals(1, choice);
    }

    @Test
    void collectGoldBeforeBuildChoiceTest() {
        Bot bot = new Bot("Bot", new EinsteinAlgo());
        bot.setGold(0);
        bot.setGameCharacter(new King());
        District district = new District("District 1", 1, DistrictColor.NOBLE);

        bot.addDistrictInHand(district);
        assertEquals(1, bot.getDistrictsInHand().size());
        assertEquals(district, bot.getDistrictsInHand().get(0));
        assertEquals(0, bot.getGold());
        assertTrue(bot.getLowestDistrictInHand().isPresent());
        assertEquals(district, bot.getLowestDistrictInHand().get());
        assertTrue(bot.getBotAlgo().collectGoldBeforeBuildChoice());

        bot.setGold(10);
        assertFalse(bot.getBotAlgo().collectGoldBeforeBuildChoice());
    }
}
