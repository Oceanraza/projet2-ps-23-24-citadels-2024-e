package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ThiefTest {
    Game game;
    Thief thief;
    Assassin assassin;
    Warlord warlord;

    Player thiefPlayer;
    Player assassinPlayer;
    Player targetPlayer;

    @BeforeEach
    void setUp() {
        game = new Game();
        thief = new Thief();
        assassin = new Assassin();
        warlord = new Warlord();

        // Create players
        thiefPlayer = new Bot("thiefPlayer");
        assassinPlayer = new Bot("assassinPlayer");
        targetPlayer = new Bot("targetPlayer");
    }

    // Steal a character
    @Test
    void stealCharacterTest() {
        // Add players to the game
        game.setPlayers(thiefPlayer, targetPlayer);
        // Set characters to players
        thiefPlayer.setGameCharacter(thief);
        targetPlayer.setGameCharacter(warlord);
        // Set gold
        thiefPlayer.setGold(2);
        targetPlayer.setGold(3);

        thief.specialEffect(thiefPlayer, game, GameCharacterRole.WARLORD);

        assertEquals(thiefPlayer, warlord.getAttacker());
        assertEquals(5, thiefPlayer.getGold());
        assertEquals(0, targetPlayer.getGold());
    }

    // Steal a character that is not selected by any player
    @Test
    void stealUnselectedCharacterTest() {
        // Add players to the game
        game.setPlayers(thiefPlayer);
        // Set characters to players
        thiefPlayer.setGameCharacter(thief);
        // Set gold
        thiefPlayer.setGold(2);

        thief.specialEffect(thiefPlayer, game, GameCharacterRole.WARLORD);

        assertNull(warlord.getAttacker());
        assertEquals(2, thiefPlayer.getGold());
    }

    // Thief should not be able to steal himself
    @Test
    void shouldNotStealHimself() {
        // Add players to the game
        game.setPlayers(thiefPlayer);
        // Set characters to players
        thiefPlayer.setGameCharacter(thief);
        // Set gold
        thiefPlayer.setGold(2);

        try {
            thief.specialEffect(thiefPlayer, game, GameCharacterRole.THIEF);
        } catch (CannotAttackException exception) {
            assertEquals("Le voleur ne peut pas se voler lui-meme", exception.getMessage());
            assertNull(thief.getAttacker());
            assertEquals(2, thiefPlayer.getGold());
        }
    }

    // Thief should not be able to steal the assassin
    @Test
    void shouldNotStealAssassin() {
        // Add players to the game
        game.setPlayers(thiefPlayer, assassinPlayer);
        // Set characters to players
        thiefPlayer.setGameCharacter(thief);
        assassinPlayer.setGameCharacter(assassin);
        // Set gold
        thiefPlayer.setGold(2);
        assassinPlayer.setGold(3);

        try {
            thief.specialEffect(thiefPlayer, game, GameCharacterRole.ASSASSIN);
        } catch (CannotAttackException exception) {
            assertEquals("Le voleur ne peut pas voler l'assassin", exception.getMessage());
            assertNull(assassin.getAttacker());
            assertEquals(2, thiefPlayer.getGold());
            assertEquals(3, assassinPlayer.getGold());
        }
    }

    // Thief should not be able to steal the assassin's target
    @Test
    void shouldNotStealAssassinsTarget() {
        // Add players to the game
        game.setPlayers(thiefPlayer, targetPlayer);
        // Set characters to players
        thiefPlayer.setGameCharacter(thief);
        targetPlayer.setGameCharacter(warlord);
        // Set gold
        thiefPlayer.setGold(2);
        targetPlayer.setGold(3);

        warlord.setIsAlive(false);

        try {
            thief.specialEffect(thiefPlayer, game, GameCharacterRole.WARLORD);
        } catch (CannotAttackException exception) {
            assertEquals("Le voleur ne peut pas voler le personnage assassine", exception.getMessage());
            assertEquals(2, thiefPlayer.getGold());
            assertEquals(3, targetPlayer.getGold());
        }
    }
}
