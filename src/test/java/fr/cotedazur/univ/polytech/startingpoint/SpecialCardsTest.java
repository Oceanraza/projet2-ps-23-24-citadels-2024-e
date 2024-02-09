package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.character.card.Bishop;
import fr.cotedazur.univ.polytech.startingpoint.character.card.King;
import fr.cotedazur.univ.polytech.startingpoint.character.card.Warlord;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart.EinsteinAlgo;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static fr.cotedazur.univ.polytech.startingpoint.Main.finalChoice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpecialCardsTest {
    King king;
    Bishop bishop;
    Warlord warlord;
    Game game;
    GameState gameState;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setupDemo();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
        game.init();
        gameState = new GameState();

        king = new King();
        bishop = new Bishop();
        warlord = new Warlord();
    }

    @Test
    void donjonTest() {
        Player firstBuilder = new Bot("Player 1", new EinsteinAlgo());
        Bot secondPlayer = new Bot("Player 2", new EinsteinAlgo());
        Player thirdPlayer = new Bot("Player 3", new EinsteinAlgo());
        Player fourthPlayer = new Bot("Player 4", new EinsteinAlgo());
        Game game = new Game();
        game.setPlayers(firstBuilder, secondPlayer, thirdPlayer, fourthPlayer);
        firstBuilder.setGold(4);
        secondPlayer.setGold(4);
        thirdPlayer.setGameCharacter(king);
        firstBuilder.setGameCharacter(king);
        secondPlayer.setGameCharacter(king);
        fourthPlayer.setGameCharacter(king);

        firstBuilder.buildDistrict(new District("Donjon", 3, DistrictColor.SPECIAL), gameState);

        assertEquals(1, firstBuilder.getCity().getDistrictsBuilt().size());
        secondPlayer.getBotAlgo().warlordAlgorithm(game); //La seule carte construite est le donjon, qui ne peut être détruit, cet algo ne détruit alors rien
        assertEquals(1, firstBuilder.getCity().getDistrictsBuilt().size());
    }

    @Test
    void huntedQuarterTest() {
        Bot firstPlayer = new Bot("Player 1", new EinsteinAlgo());
        Bot secondPlayer = new Bot("Player 2", new EinsteinAlgo());

        // Create list of players
        game.setPlayers(firstPlayer, secondPlayer);
        firstPlayer.setGold(0);
        secondPlayer.setGold(0);

        assertEquals(0, gameState.getTurn());
        for (Player player : game.getPlayers()) {
            player.getCity().addDistrict(new District("Marchand", 0, DistrictColor.TRADE), gameState);
            player.getCity().addDistrict(new District("Militaire", 0, DistrictColor.MILITARY), gameState);
            player.getCity().addDistrict(new District("Religieux", 0, DistrictColor.RELIGIOUS), gameState);
            player.getCity().addDistrict(new District("Special", 0, DistrictColor.SPECIAL), gameState);
        }
        firstPlayer.getCity().addDistrict(new District("Cour des miracles", 0, DistrictColor.SPECIAL), gameState);
        assertEquals(5, firstPlayer.getCity().getDistrictsBuilt().size());

        gameState.nextTurn();
        assertEquals(1, gameState.getTurn());
        secondPlayer.getCity().addDistrict(new District("Cour des miracles", 0, DistrictColor.SPECIAL), gameState);
        District huntedQuarter = secondPlayer.getCity().getDistrictsBuilt().get(4);
        assertTrue(huntedQuarter.getTurnBuilt().isPresent());
        assertEquals(1, huntedQuarter.getTurnBuilt().get());

        finalChoice(game.getPlayers(), gameState);
        assertEquals(3, firstPlayer.calculateScore()); // +3 HuntedQuarter goes from special to noble
        assertEquals(0, secondPlayer.calculateScore()); // HuntedQuarter's effect can't be used (built at the last turn)
    }
}
