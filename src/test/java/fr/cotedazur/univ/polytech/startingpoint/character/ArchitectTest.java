package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.ActionManager;
import fr.cotedazur.univ.polytech.startingpoint.CitadelsLogger;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.BaseAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.EinsteinAlgo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArchitectTest {
    King king;
    Bishop bishop;
    Architect architect;
    Warlord warlord;
    Magician magician;
    Bot bot;
    Game game;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setup();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        king = new King();
        bishop = new Bishop();
        warlord = new Warlord();
        magician = new Magician();
        architect = new Architect();
        bot = new Bot("Bot") {
        };
        game = new Game();
        game.init();
    }

    @Test
    void DrawsTwoTest() { //Tests if the player drew two additionnal cards
        BaseAlgo botAlgo = new EinsteinAlgo();
        Bot firstBuilder = new Bot("Player 1", botAlgo);
        botAlgo.setPlayer(firstBuilder);
        Game game = new Game();
        game.setPlayers(firstBuilder);
        firstBuilder.setGold(5);
        firstBuilder.setGameCharacter(architect);
        assertEquals(0, firstBuilder.getDistrictsInHand().size());
        assertEquals(botAlgo.startOfTurnChoice(), 2); //draws card because doesn't have any/has architect
        ActionManager.startOfTurn(game, firstBuilder);
        assertEquals(3, firstBuilder.getDistrictsInHand().size());
    }

    @Test
    void CanBuildMoreThanOneDistrictTest() { //Tests if the player builds more than one district if he can
        BaseAlgo botAlgo = new EinsteinAlgo();
        Bot firstBuilder = new Bot("Player 1", botAlgo);
        botAlgo.setPlayer(firstBuilder);
        Game game = new Game();
        game.setPlayers(firstBuilder);
        firstBuilder.setGold(10);

        firstBuilder.getDistrictsInHand().add(new District("test", 2, DistrictColor.TRADE));
        firstBuilder.getDistrictsInHand().add(new District("test2", 2, DistrictColor.TRADE));
        firstBuilder.getDistrictsInHand().add(new District("test3", 2, DistrictColor.TRADE));

        firstBuilder.setGameCharacter(architect);
        assertEquals(0, firstBuilder.getCity().getDistrictsBuilt().size());
        botAlgo.buildOrNot(new GameState());
        assertEquals(3, firstBuilder.getCity().getDistrictsBuilt().size());
    }

    @Test
    void EinsteinAlgoChoosesArchitectTest() { //Tests if the player builds more than one district if he can
        BaseAlgo botAlgo = new EinsteinAlgo();
        Bot firstBuilder = new Bot("Player 1", botAlgo);
        botAlgo.setPlayer(firstBuilder);
        Game game = new Game();
        //game.shuffleCharacters();
        game.getAvailableChars().add(new Architect());
        game.getAvailableChars().add(new Magician());
        game.getAvailableChars().add(new Merchant());
        game.getAvailableChars().add(new King());
        game.setPlayers(firstBuilder);
        firstBuilder.setGold(10);
        botAlgo.chooseCharacterAlgorithm(game);
        assertEquals(GameCharacterRole.ARCHITECT, firstBuilder.getGameCharacter().getRole());
    }
}