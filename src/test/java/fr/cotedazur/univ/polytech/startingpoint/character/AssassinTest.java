package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssassinTest {
    Game game;
    Assassin assassin;
    Warlord warlord;
    King king;

    Player assassinPlayer;
    Player targetPlayer;

    @BeforeEach
    void setUp(){
        game = new Game();
        assassin = new Assassin();
        warlord = new Warlord();
        king = new King();

        // Create players
        assassinPlayer = new Bot("assassinPlayer");
        targetPlayer = new Bot("targetPlayer");
    }

    // Kill a character
    @Test
    void killCharacterTest() {
        // Add players to the game
        game.setPlayers(assassinPlayer, targetPlayer);
        // Set characters to players
        assassinPlayer.setGameCharacter(assassin);
        targetPlayer.setGameCharacter(warlord);

        assassin.killCharacter(assassinPlayer, game, GameCharacterRole.WARLORD);
        assertFalse(warlord.getIsAlive());
        assertEquals(assassinPlayer, warlord.getAttacker());
    }

    // Kill a character that is not selected by any player
    @Test
    void killUnselectedCharacterTest() {
        // Add players to the game
        game.setPlayers(assassinPlayer);
        // Set characters to players
        assassinPlayer.setGameCharacter(assassin);

        assassin.killCharacter(assassinPlayer, game, GameCharacterRole.WARLORD);
        assertTrue(warlord.getIsAlive());
        assertNull(warlord.getAttacker());
    }

    // Kill a character
    @Test
    void killSelectedCharacterTest() {
        // Add players to the game
        game.setPlayers(assassinPlayer, targetPlayer);
        // Set characters to players
        assassinPlayer.setGameCharacter(assassin);
        targetPlayer.setGameCharacter(king);

        assassin.killCharacter(assassinPlayer, game, GameCharacterRole.KING);
        assertFalse(king.getIsAlive());
        assertEquals(assassinPlayer, king.getAttacker());
    }
}
