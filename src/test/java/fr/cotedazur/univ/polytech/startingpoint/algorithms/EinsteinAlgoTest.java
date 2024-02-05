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

import java.util.List;

import static org.mockito.Mockito.*;

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
        bot = mock(Bot.class);
        city = mock(City.class); // Mock the City object
        deck = mock(Deck.class); // Mock the Deck object
        when(bot.getCity()).thenReturn(city); // Set the mocked City object to the Bot
        when(game.getDeck()).thenReturn(deck); // Set the mocked Deck object to the Game
        einsteinAlgo.setBot(bot);
    }

    @Test
    void shouldDrawThreeCardsWhenObservatoryIsBuilt() {
        District observatory = new District("Observatoire", 4, DistrictColor.special);
        when(bot.getCity().containsDistrict("Observatoire")).thenReturn(true);
        when(game.drawCard(this.bot)).thenReturn(observatory);

        einsteinAlgo.startOfTurn(game);

        verify(game, times(3)).drawCard(this.bot);
    }

    @Test
    void shouldDrawOneCardWhenHandIsEmpty() {
        List<District> districtsInHand = mock(List.class);
        when(bot.getDistrictsInHand()).thenReturn(districtsInHand);
        when(districtsInHand.isEmpty()).thenReturn(true);
        einsteinAlgo.startOfTurn(game);

        verify(game).drawCard(this.bot);
    }

    @Test
    void shouldDrawOneCardWhenAllDistrictsInHandAreBuilt() {
        when(bot.districtsInHandAreBuilt()).thenReturn(true);

        einsteinAlgo.startOfTurn(game);

        verify(game).drawCard(this.bot);
    }

    @Test
    void shouldAddTwoGoldWhenHandIsNotEmptyAndNotAllDistrictsAreBuilt() {
        List<District> districtsInHand = mock(List.class);
        when(bot.getDistrictsInHand()).thenReturn(districtsInHand);
        when(districtsInHand.isEmpty()).thenReturn(false);
        when(bot.districtsInHandAreBuilt()).thenReturn(false);

        einsteinAlgo.startOfTurn(game);

        verify(bot).addGold(2);
    }

    @Test
    void shouldDrawTwoCardsWhenLibraryIsBuiltAndHandSizeIsLessThanTwo() {
        when(bot.getCity().containsDistrict("Bibliothèque")).thenReturn(true);
        List<District> districtsInHand = mock(List.class);
        when(bot.getDistrictsInHand()).thenReturn(districtsInHand);
        when(districtsInHand.size()).thenReturn(1);
        when(bot.districtsInHandAreBuilt()).thenReturn(true);

        einsteinAlgo.startOfTurn(game);

        verify(game, times(2)).drawCard(this.bot);
    }

    @Test
    void shouldDrawOneCardWhenHandIsEmptyAndNoSpecialDistrictsAreBuilt() {
        when(bot.getCity().containsDistrict("Bibliothèque")).thenReturn(false);
        when(bot.getCity().containsDistrict("Observatoire")).thenReturn(false);
        List<District> districtsInHand = mock(List.class);
        when(bot.getDistrictsInHand()).thenReturn(districtsInHand);
        when(districtsInHand.isEmpty()).thenReturn(true);

        einsteinAlgo.startOfTurn(game);

        verify(game).drawCard(this.bot);
    }

    @Test
    void shouldAddTwoGoldWhenHandIsNotEmptyAndNoSpecialDistrictsAreBuilt() {
        when(bot.getCity().containsDistrict("Bibliothèque")).thenReturn(false);
        when(bot.getCity().containsDistrict("Observatoire")).thenReturn(false);
        List<District> districtsInHand = mock(List.class);
        when(bot.getDistrictsInHand()).thenReturn(districtsInHand);
        when(districtsInHand.isEmpty()).thenReturn(false);

        einsteinAlgo.startOfTurn(game);

        verify(bot).addGold(2);
    }

    @Test
    void shouldNotTakeBackDestroyedDistrictWhenPlayerIsCondottiere() {
        District destroyedDistrict = new District("Quartier", 3, DistrictColor.marchand);
        when(bot.getCity().containsDistrict("Cimetière")).thenReturn(true);
        when(bot.getGold()).thenReturn(2);
        when(bot.getCharacterName()).thenReturn("Condottiere");

        // Mock the districtsInHand list
        List<District> districtsInHand = mock(List.class);
        when(bot.getDistrictsInHand()).thenReturn(districtsInHand);

        einsteinAlgo.cemeteryLogic(game, bot, destroyedDistrict);

        verify(bot, never()).addGold(-1);
        verify(districtsInHand, never()).add(destroyedDistrict); // Use the mock here
    }
}