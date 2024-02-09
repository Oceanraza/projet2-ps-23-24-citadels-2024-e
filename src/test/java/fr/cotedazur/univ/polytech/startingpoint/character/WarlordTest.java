package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.card.Bishop;
import fr.cotedazur.univ.polytech.startingpoint.character.card.King;
import fr.cotedazur.univ.polytech.startingpoint.character.card.Warlord;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import static fr.cotedazur.univ.polytech.startingpoint.Game.CITY_SIZE_TO_WIN;
import static org.junit.jupiter.api.Assertions.*;

class WarlordTest {
    Game game;
    GameState gameState;
    Warlord warlord;
    Bot warlordPlayer;
    Bot targetPlayer;
    Bot bot;
    District district3cost;
    District district1cost;
    Bishop bishop;
    King king;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setupDemo();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        bishop = new Bishop();
        king = new King();

        game = new Game();
        gameState = new GameState();
        warlord = new Warlord();

        // Create players
        warlordPlayer = new Bot("warlordPlayer");
        targetPlayer = new Bot("targetPlayer");
        bot = new Bot("Bot");

        // Create districts
        district3cost = new District("Quartier qui coute 3", 3, DistrictColor.NOBLE);
        district1cost = new District("Quartier qui coute 1", 1, DistrictColor.NOBLE);
    }

    @Test
    void getLowestDistrictTest() {
        District district1 = new District("Quartier 1", 3, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 1, DistrictColor.NOBLE);
        District district3 = new District("Quartier 3", 4, DistrictColor.RELIGIOUS);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(district3, gameState);
        assertTrue(bot.getLowestDistrictBuilt().isPresent());
        assertEquals(district2, bot.getLowestDistrictBuilt().get());
    }

    @Test
    void warlordInformationsTest() {
        assertEquals(8, warlord.getRunningOrder());
        assertEquals(DistrictColor.MILITARY, warlord.getColor());
    }

    @Test
    void destroyDistrictTest() {
        // Add players to the game
        game.setPlayers(warlordPlayer, targetPlayer);
        // Set characters to players
        warlordPlayer.setGameCharacter(warlord);
        // Add districts
        targetPlayer.addDistrictBuilt(district1cost, gameState);
        targetPlayer.addDistrictBuilt(district3cost, gameState);
        // Set gold
        warlordPlayer.setGold(5);

        warlord.specialEffect(warlordPlayer, game, targetPlayer, district3cost);

        assertEquals(3, warlordPlayer.getGold());
        assertFalse(targetPlayer.getCity().containsDistrict(district3cost.getName()));
        assertEquals(1, targetPlayer.getCity().getDistrictsBuilt().size());
    }

    @Test
    void getWrongDistrictTest() {
        assertEquals(Optional.empty(), bot.getLowestDistrictBuilt());
    }

    @Test
    void getSortedPlayersByScoreTest() {
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Player thirdPlayer = new Bot("Player 3");
        Player fourthPlayer = new Bot("Player 4");
        Game game = new Game();
        game.setPlayers(firstBuilder, secondPlayer, thirdPlayer, fourthPlayer);
        thirdPlayer.setGameCharacter(bishop);
        firstBuilder.setGameCharacter(king);
        secondPlayer.setGameCharacter(king);
        fourthPlayer.setGameCharacter(king);

        for (int i = 0; i < CITY_SIZE_TO_WIN; i++) {
            firstBuilder.getCity().getDistrictsBuilt().add(new District("test", 4, DistrictColor.TRADE));
            secondPlayer.getCity().getDistrictsBuilt().add(new District("test", 5, DistrictColor.TRADE));
            thirdPlayer.getCity().getDistrictsBuilt().add(new District("test", 2, DistrictColor.TRADE));
            fourthPlayer.getCity().getDistrictsBuilt().add(new District("test", 1, DistrictColor.TRADE));
        }
        List<Player> expectedOutput = new ArrayList<>();
        expectedOutput.add(secondPlayer);
        expectedOutput.add(firstBuilder);
        expectedOutput.add(fourthPlayer);
        //third player is missing because he is the bishop, therefore he can't be an option
        assertEquals(expectedOutput, game.getSortedPlayersByScoreForWarlord());
    }

    @Test
    void destroyDistrictTestForFree() {
        // Add players to the game
        game.setPlayers(warlordPlayer, targetPlayer);
        // Set characters to players
        warlordPlayer.setGameCharacter(warlord);
        // Add districts
        targetPlayer.addDistrictBuilt(district1cost, gameState);
        targetPlayer.addDistrictBuilt(district3cost, gameState);
        // Set gold
        warlordPlayer.setGold(5);

        warlord.specialEffect(warlordPlayer, game, targetPlayer, district1cost);

        assertEquals(5, warlordPlayer.getGold());
        assertFalse(targetPlayer.getCity().containsDistrict(district1cost.getName()));
        assertEquals(1, targetPlayer.getCity().getDistrictsBuilt().size());
    }

    @Test
    void WarlordGameCanDestroyFirstTest() {
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Game game = new Game();
        game.setPlayers(firstBuilder, secondPlayer);
        firstBuilder.setGold(5);
        firstBuilder.setGameCharacter(warlord);
        secondPlayer.setGameCharacter(king);

        District distToDestroy = new District("test", 5, DistrictColor.TRADE);
        secondPlayer.getCity().getDistrictsBuilt().add(distToDestroy);

        assertEquals(1, secondPlayer.getCity().getDistrictsBuilt().size());
        firstBuilder.getGameCharacter().specialEffect(firstBuilder, game, secondPlayer, distToDestroy);
        assertEquals(1, firstBuilder.getGold());
        assertEquals(0, secondPlayer.getCity().getDistrictsBuilt().size());
    }
}
