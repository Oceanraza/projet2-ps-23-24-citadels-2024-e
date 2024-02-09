package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.character.card.King;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameTest {
    Game game;
    Bot bot;
    King king;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setupDemo();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
    }

    @Test
    void testGameInit() {
        assertEquals(0, game.getPlayers().size());
        assertEquals(65, game.getDeck().size());

        game.init();
        game.shuffleCharacters();

        assertEquals(6, game.getCharactersInGame().size());
        assertEquals(5, game.getAvailableChars().size());
    }

    @Test
    void testGameList() {
        game.init();

        // Shuffle characters
        game.availableChars.clear();
        game.charactersInGame = new ArrayList<>(game.allCharacters);
        // Remove warlord faceup
        game.charactersInGame.remove(7);
        // Remove architect faceup
        game.charactersInGame.remove(6);
        game.availableChars = new ArrayList<>(game.charactersInGame);
        // Remove merchant facedown
        game.availableChars.remove(5);

        assertEquals(5, game.getKillableCharacters().size());
        assertEquals(4, game.getCharactersThatCanBeStolen().size());

        // If a character is killed, he can be stolen
        game.bishop.setIsAlive(false);
        assertEquals(3, game.getCharactersThatCanBeStolen().size());
    }

    @Test
    void testChooseCharacter() {
        bot = new Bot("Bot");
        king = mock(King.class);

        game.setPlayers(bot);
        bot.setGameCharacter(king);
        assertEquals(king, bot.getGameCharacter());

        game.resetChars();
        assertNull(bot.getGameCharacter());
    }

    @Test
    void testKillCharacter() {
        bot = mock(Bot.class);

        game.init();
        game.shuffleCharacters();

        game.bishop.setIsAlive(false);
        game.bishop.setAttacker(bot);

        assertFalse(game.bishop.getIsAlive());
        assertEquals(bot, game.bishop.getAttacker());

        game.resetCharsState();

        assertTrue(game.bishop.getIsAlive());
        assertNull(game.bishop.getAttacker());
    }

    // Crown gets to the king even if he is killed
    @Test
    void crownOwnerTest() {
        bot = mock(Bot.class);
        king = new King();

        game.setPlayers(bot);
        bot.setGameCharacter(king);
        king.setAttacker(bot);
        game.playerKilled(king, bot);
        assertEquals(bot, game.getCrownOwner());
    }

    @Test
    void giveStartingCardsTest() {
        Player player = new Bot("toto");
        Player player2 = new Bot("titi");
        game.setPlayers(player, player2);
        game.giveStartingCards();

        assertEquals(4, player.getDistrictsInHand().size());
        assertEquals(4, player2.getDistrictsInHand().size());
    }

    @Test
    void printCrownOwnerTest() {
        Player player = new Bot("toto");
        game.getCrown().setOwner(player);
        assertEquals(player, game.printCrownOwner());
    }

    @Test
    void getPlayerWithMostDistrictsWhenTwoPlayersTest() {
        Player player = new Bot("toto");
        Player player2 = new Bot("titi");

        District d1 = new District("Temple", 1, DistrictColor.RELIGIOUS);
        District d2 = new District("Eglise", 2, DistrictColor.RELIGIOUS);
        District d3 = new District("q", 3, DistrictColor.TRADE);
        GameState gameState = new GameState();

        game.setPlayers(player, player2);

        player.addDistrictBuilt(d1, gameState);
        player.addDistrictBuilt(d2, gameState);

        player2.addDistrictBuilt(d1, gameState);
        player2.addDistrictBuilt(d2, gameState);
        player2.addDistrictBuilt(d3, gameState);

        assertEquals(player2, game.getPlayerWithMostDistricts());
    }

    @Test
    void getRichestPlayerWhenTwoPlayersTest() {
        Player player = new Bot("toto");
        Player player2 = new Bot("titi");

        game.setPlayers(player, player2);
        player.setGold(2);
        player2.setGold(4);

        assertEquals(player2, game.getRichestPlayer());
    }

    @Test
    void averageCitySizeWhenTwoPlayersTest() {
        Player player = new Bot("toto");
        Player player2 = new Bot("titi");

        District d1 = new District("Temple", 1, DistrictColor.RELIGIOUS);
        District d2 = new District("Eglise", 2, DistrictColor.RELIGIOUS);
        District d3 = new District("q", 3, DistrictColor.TRADE);
        District d4 = new District("q2", 3, DistrictColor.TRADE);
        GameState gameState = new GameState();

        game.setPlayers(player, player2);

        player.addDistrictBuilt(d1, gameState);
        player.addDistrictBuilt(d2, gameState);

        player2.addDistrictBuilt(d1, gameState);
        player2.addDistrictBuilt(d2, gameState);
        player2.addDistrictBuilt(d3, gameState);
        player2.addDistrictBuilt(d4, gameState);

        assertEquals(3, game.averageCitySize());
    }

    @Test
    void getPlayerWith6DistrictsTest() {
        Player player = new Bot("toto");
        Player player2 = new Bot("titi");

        District d1 = new District("Temple", 1, DistrictColor.RELIGIOUS);
        District d2 = new District("Eglise", 2, DistrictColor.RELIGIOUS);
        District d3 = new District("q", 3, DistrictColor.TRADE);
        District d4 = new District("q2", 3, DistrictColor.TRADE);
        District d5 = new District("q3", 3, DistrictColor.TRADE);
        District d6 = new District("q4", 3, DistrictColor.TRADE);
        GameState gameState = new GameState();

        game.setPlayers(player, player2);

        player.addDistrictBuilt(d1, gameState);
        player.addDistrictBuilt(d2, gameState);
        player.addDistrictBuilt(d3, gameState);
        player.addDistrictBuilt(d4, gameState);
        player.addDistrictBuilt(d5, gameState);
        player.addDistrictBuilt(d6, gameState);

        player2.addDistrictBuilt(d1, gameState);
        player2.addDistrictBuilt(d2, gameState);
        player2.addDistrictBuilt(d3, gameState);
        player2.addDistrictBuilt(d4, gameState);
        player2.addDistrictBuilt(d5, gameState);
        player2.addDistrictBuilt(d6, gameState);

        assertEquals(player, game.getPlayerWith6Districts());
    }

    @Test
    void getPlayerWithLowestDistrictPriceTest() {
        Player player = new Bot("toto");
        Player player2 = new Bot("titi");

        District d1 = new District("Temple", 1, DistrictColor.RELIGIOUS);
        District d2 = new District("Eglise", 2, DistrictColor.RELIGIOUS);
        District d3 = new District("q", 3, DistrictColor.TRADE);
        District d4 = new District("q2", 4, DistrictColor.TRADE);
        District d5 = new District("q3", 5, DistrictColor.TRADE);
        GameState gameState = new GameState();

        game.setPlayers(player, player2);

        player.addDistrictBuilt(d1, gameState);
        player.addDistrictBuilt(d2, gameState);
        player.addDistrictBuilt(d3, gameState);

        player2.addDistrictBuilt(d1, gameState);
        player2.addDistrictBuilt(d2, gameState);
        player2.addDistrictBuilt(d3, gameState);
        player2.addDistrictBuilt(d4, gameState);
        player2.addDistrictBuilt(d5, gameState);

        assertEquals(player, game.getPlayerWithLowestDistrictPrice());
    }

}
