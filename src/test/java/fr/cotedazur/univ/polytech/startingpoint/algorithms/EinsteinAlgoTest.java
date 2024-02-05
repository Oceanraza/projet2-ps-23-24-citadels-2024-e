package fr.cotedazur.univ.polytech.startingpoint.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.board.Deck;
import fr.cotedazur.univ.polytech.startingpoint.city.City;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.EinsteinAlgo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EinsteinAlgoTest {
    private EinsteinAlgo einsteinAlgo;
    private Game game;
    private Bot bot;
    private City city;
    private Deck deck;

    @BeforeEach
    void setUp() {
        einsteinAlgo = new EinsteinAlgo();
        game = mock(Game.class);
        bot = mock(Bot.class); // Create a mock Bot instead of a real instance
        einsteinAlgo.setBot(bot); // Set the bot instance variable of the EinsteinAlgo object
        city = mock(City.class);
        deck = mock(Deck.class);
    }

    @Test
    void shouldChooseCardWithLowestPrice() {
        List<District> threeCards = Arrays.asList(
                new District("District 1", 5, DistrictColor.NOBLE),
                new District("District 2", 3, DistrictColor.TRADE),
                new District("District 3", 4, DistrictColor.SPECIAL)
        );

        when(bot.getGold()).thenReturn(4);

        District chosenCard = einsteinAlgo.chooseCard(threeCards);

        assertEquals("District 2", chosenCard.getName());
    }

    @Test
    void shouldNotChooseCardIfNotAffordable() {
        List<District> threeCards = Arrays.asList(
                new District("District 1", 5, DistrictColor.NOBLE),
                new District("District 2", 6, DistrictColor.TRADE),
                new District("District 3", 7, DistrictColor.SPECIAL)
        );

        when(bot.getGold()).thenReturn(4);

        District chosenCard = einsteinAlgo.chooseCard(threeCards);

        assertNull(chosenCard);
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

        int choice = einsteinAlgo.startOfTurnChoice();

        assertEquals(1, choice);
    }
}