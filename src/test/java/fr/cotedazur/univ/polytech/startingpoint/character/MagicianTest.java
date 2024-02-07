package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.card.*;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

class MagicianTest {
    Game game;
    Magician magician;
    Bot magicianPlayer;
    Bot targetPlayer;
    District district1;
    District district2;
    District district3;

    @BeforeEach
    void setUp() {
        InGameLogger.setup();
        InGameLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
        magician = new Magician();

        // Create players
        magicianPlayer = new Bot("magicianPlayer");
        targetPlayer = new Bot("targetPlayer");

        // Create districts
        district1 = new District("Quartier 1", 1, DistrictColor.NOBLE);
        district2 = new District("Quartier 2", 2, DistrictColor.MILITARY);
        district2 = new District("Quartier 3", 3, DistrictColor.MILITARY);
    }

    @Test
    void magicianInformationsTest() {
        assertEquals(3, magician.getRunningOrder());
        assertNull(magician.getColor());
    }

    @Test
    void switchCardsWithPlayerTest() { // Tests if the hands have been switched
        // Add players to the game
        game.setPlayers(magicianPlayer, targetPlayer);
        // Set characters to players
        magicianPlayer.setGameCharacter(magician);
        // Add districts
        magicianPlayer.addDistrictInHand(district1);
        targetPlayer.addDistrictInHand(district2);
        targetPlayer.addDistrictInHand(district3);

        assertEquals(1, magicianPlayer.getDistrictsInHand().size());
        assertEquals(2, targetPlayer.getDistrictsInHand().size());

        // Switch cards in hand
        magician.specialEffect(magicianPlayer, game, true, targetPlayer);
        assertEquals(2, magicianPlayer.getDistrictsInHand().size());
        assertEquals(1, targetPlayer.getDistrictsInHand().size());
    }

    @Test
    void switchCardsWithPlayerEmptyHandTest() { // Tests if the hands have been switched
        // Add players to the game
        game.setPlayers(magicianPlayer, targetPlayer);
        // Set characters to players
        magicianPlayer.setGameCharacter(magician);
        // Add districts
        targetPlayer.addDistrictInHand(district1);
        targetPlayer.addDistrictInHand(district2);
        targetPlayer.addDistrictInHand(district3);

        assertEquals(0, magicianPlayer.getDistrictsInHand().size());
        assertEquals(3, targetPlayer.getDistrictsInHand().size());

        // Switch cards in hand
        magician.specialEffect(magicianPlayer, game, true, targetPlayer);
        assertEquals(3, magicianPlayer.getDistrictsInHand().size());
        assertEquals(0, targetPlayer.getDistrictsInHand().size());
    }

    @Test
    void switchCardsWithDeckTest() { // Tests if the hands have been switched
        // Add players to the game
        game.setPlayers(magicianPlayer);
        // Set characters to players
        magicianPlayer.setGameCharacter(magician);
        // Add districts
        magicianPlayer.addDistrictInHand(district1);
        magicianPlayer.addDistrictInHand(district2);
        magicianPlayer.addDistrictInHand(district3);

        assertEquals(3, magicianPlayer.getDistrictsInHand().size());

        // Switch cards in hand
        magician.specialEffect(magicianPlayer, game, false);
        assertEquals(3, magicianPlayer.getDistrictsInHand().size());
        assertFalse(magicianPlayer.getDistrictsInHand().contains(district1));
        assertFalse(magicianPlayer.getDistrictsInHand().contains(district2));
        assertFalse(magicianPlayer.getDistrictsInHand().contains(district3));
    }

    @Test
    void switchCardsWithDeckEmptyHandTest() { // Tests if the hands have been switched
        // Add players to the game
        game.setPlayers(magicianPlayer);
        // Set characters to players
        magicianPlayer.setGameCharacter(magician);

        assertEquals(0, magicianPlayer.getDistrictsInHand().size());

        // Switch cards in hand
        magician.specialEffect(magicianPlayer, game, false);
        assertEquals(0, magicianPlayer.getDistrictsInHand().size());
    }
}
