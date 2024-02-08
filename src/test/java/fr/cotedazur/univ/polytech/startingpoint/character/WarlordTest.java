package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.card.Warlord;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class WarlordTest {
    Game game;
    GameState gameState;
    Warlord warlord;
    Bot warlordPlayer;
    Bot targetPlayer;
    District district3cost;
    District district1cost;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setupDemo();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
        gameState = new GameState();
        warlord = new Warlord();

        // Create players
        warlordPlayer = new Bot("warlordPlayer");
        targetPlayer = new Bot("targetPlayer");

        // Create districts
        district3cost = new District("Quartier qui coute 3", 3, DistrictColor.NOBLE);
        district1cost = new District("Quartier qui coute 1", 1, DistrictColor.NOBLE);
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
}
