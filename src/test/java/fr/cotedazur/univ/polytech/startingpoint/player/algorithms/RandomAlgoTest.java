package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RandomAlgoTest {
    RandomAlgo randomAlgo;
    Game game;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setupDemo();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        randomAlgo = mock(RandomAlgo.class);
        game = mock(Game.class);
    }

    @Test
    void drawStartOfTurnChoiceTest() {
        Mockito.when(randomAlgo.getRandomBoolean()).thenReturn(false);
        Mockito.when(randomAlgo.startOfTurnChoice()).thenCallRealMethod();
        int randomInt = randomAlgo.startOfTurnChoice();
        assertEquals(2, randomInt);
    }

    @Test
    void takeGoldStartOfTurnChoiceTest() {
        Mockito.when(randomAlgo.getRandomBoolean()).thenReturn(true);
        Mockito.when(randomAlgo.startOfTurnChoice()).thenCallRealMethod();
        int randomInt = randomAlgo.startOfTurnChoice();
        assertEquals(1, randomInt);
    }

    @Test
    void manufactureChoiceTest() {
        Mockito.when(randomAlgo.getRandomBoolean()).thenReturn(true);
        Mockito.when(randomAlgo.manufactureChoice()).thenCallRealMethod();
        boolean randomBoolean = randomAlgo.manufactureChoice();
        assertTrue(randomBoolean);
    }

    @Test
    void graveyardChoiceTest() {
        Mockito.when(randomAlgo.getRandomBoolean()).thenReturn(false);
        Mockito.when(randomAlgo.manufactureChoice()).thenCallRealMethod();
        boolean randomBoolean = randomAlgo.manufactureChoice();
        assertFalse(randomBoolean);
    }

    /* TODO
    @Test
    void chooseCharacterAlgorithmTest() {
        List<GameCharacter> availableChars = new ArrayList<>();
        Warlord warlord = new Warlord();
        Bishop bishop = new Bishop();
        King king = new King();

        availableChars.add(warlord);
        availableChars.add(bishop);
        availableChars.add(king);

        Mockito.when(game.getAvailableChars()).thenReturn(availableChars);
        mockStatic(Utils.class);

        randomAlgo.chooseCharacterAlgorithm(game);
        when(randomAlgo.setPlayer(Mockito.any(Bot.class)))
        assertEquals(king, randomAlgo.bot.getGameCharacter());
    }
    */
}
