package fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.City;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RichardAlgoTest {

    @Test
    void shouldChooseCharacterBasedOnAlgorithm() {
        // Arrange
        RichardAlgo richardAlgo = new RichardAlgo();
        Game game = mock(Game.class);
        Bot bot = mock(Bot.class);
        doNothing().when(bot).chooseChar(any(Game.class), any(GameCharacterRole.class));
        richardAlgo.setBot(bot);  // Set the mock Bot

        // Act
        richardAlgo.chooseCharacterAlgorithm(game);

        // Assert
        verify(bot, times(1)).chooseChar(any(Game.class), any(GameCharacterRole.class));
    }

    @Test
    void shouldHandleNoAvailableCharactersInChooseCharacterAlgorithm() {
        // Arrange
        RichardAlgo richardAlgo = new RichardAlgo();
        Game game = mock(Game.class);
        Bot bot = mock(Bot.class);
        doNothing().when(bot).chooseChar(any(Game.class), any(GameCharacterRole.class));
        richardAlgo.setBot(bot);  // Set the mock Bot

        // Act
        richardAlgo.chooseCharacterAlgorithm(game);

        // Assert
        verify(bot, times(1)).chooseChar(any(Game.class), any(GameCharacterRole.class));
    }

    @Test
    void shouldExecuteWarlordAlgorithmSuccessfully() {
        // Arrange
        RichardAlgo richardAlgo = new RichardAlgo();
        Game game = mock(Game.class);
        Bot player = mock(Bot.class);  // Create a mock Player
        when(game.getPlayerWith6Districts()).thenReturn(player);  // Return the mock Player
        when(game.getPlayerWithLowestDistrictPrice()).thenReturn(player);  // Return the mock Player

        // Act
        richardAlgo.warlordAlgorithm(game);

        // Assert
        verify(game, times(1)).getPlayerWith6Districts();
        verify(game, times(1)).getPlayerWithLowestDistrictPrice();
    }

    @Test
    void shouldHandleNoWarlordInWarlordAlgorithm() {
        // Arrange
        RichardAlgo richardAlgo = new RichardAlgo();
        Game game = mock(Game.class);
        Bot bot = mock(Bot.class);  // Create a mock Bot
        richardAlgo.setBot(bot);  // Set the mock Bot
        when(game.getPlayerWith6Districts()).thenReturn(bot);  // Return the mock Player
        when(game.getPlayerWithLowestDistrictPrice()).thenReturn(bot);  // Return the mock Player

        // Act
        richardAlgo.warlordAlgorithm(game);

        // Assert
        verify(game, times(1)).getPlayerWith6Districts();
        verify(game, times(1)).getPlayerWithLowestDistrictPrice();
    }

    @Test
    void shouldExecuteMagicianAlgorithmSuccessfully() {
        // Arrange
        RichardAlgo richardAlgo = new RichardAlgo();
        Game game = mock(Game.class);
        Bot bot = mock(Bot.class);
        GameCharacter gameCharacter = mock(GameCharacter.class);  // Create a mock GameCharacter
        when(bot.getGameCharacter()).thenReturn(gameCharacter);  // Return the mock GameCharacter
        richardAlgo.setBot(bot);  // Set the mock Bot

        // Act
        richardAlgo.magicianAlgorithm(game);

        // Assert
        verify(gameCharacter, times(1)).specialEffect(any(), any(), any());
    }


    @Test
    void shouldNotPickArchitectWhenConditionsAreNotMet() {
        // Arrange
        RichardAlgo richardAlgo = new RichardAlgo();
        Game game = mock(Game.class);
        when(game.containsAvailableRole(GameCharacterRole.ARCHITECT)).thenReturn(false);

        // Act
        int result = richardAlgo.shouldPickArchitect(game);

        // Assert
        assertTrue(result == 0);
    }

    @Test
    void shouldPickAssassinWhenConditionsAreMet() {
        // Arrange
        RichardAlgo richardAlgo = new RichardAlgo();
        Game game = mock(Game.class);
        Bot bot = mock(Bot.class);
        City city = mock(City.class);  // Create a mock City
        when(bot.getCity()).thenReturn(city);  // Return the mock City
        when(city.size()).thenReturn(6);  // Return the size of the mock City
        richardAlgo.setBot(bot);  // Set the mock Bot
        when(game.containsAvailableRole(GameCharacterRole.ASSASSIN)).thenReturn(true);
        when(game.containsAvailableRole(GameCharacterRole.KING)).thenReturn(false);
        when(game.getPlayerWithMostDistricts()).thenReturn(bot);
        when(game.getPlayers()).thenReturn(new ArrayList<>(List.of(bot)));//

        // Act
        int result = richardAlgo.shouldPickAssassin(game);

        // Assert
        assertTrue(result > 0);
    }

    @Test
    void shouldNotPickAssassinWhenConditionsAreNotMet() {
        // Arrange
        RichardAlgo richardAlgo = new RichardAlgo();
        Game game = mock(Game.class);
        when(game.containsAvailableRole(GameCharacterRole.ASSASSIN)).thenReturn(false);

        // Act
        int result = richardAlgo.shouldPickAssassin(game);

        // Assert
        assertTrue(result == 0);
    }
}