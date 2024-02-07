package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.card.*;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MagicianTest {
    Assassin assassin;
    Magician magician;
    King king;
    Bishop bishop;
    Warlord warlord;
    Bot bot;
    Game game;

    @BeforeEach
    void setUp() {
        CitadelsLogger.setup();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        assassin = new Assassin();
        magician = new Magician();
        king = new King();
        bishop = new Bishop();
        warlord = new Warlord();

        bot = new Bot("Bot") {
        };
        game = new Game();
        game.init();
    }

    @Test
    void switchTest() { //Tests if the hands have been switched
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Game game = new Game();
        game.setPlayers(firstBuilder, secondPlayer);
        firstBuilder.setGold(5);
        firstBuilder.setGameCharacter(magician);
        secondPlayer.setGameCharacter(king);

        District distToSwitch = new District("test", 5, DistrictColor.TRADE);
        secondPlayer.addDistrictInHand(distToSwitch);
        secondPlayer.addDistrictInHand(distToSwitch);
        secondPlayer.addDistrictInHand(distToSwitch);
        firstBuilder.addDistrictInHand(distToSwitch);

        assertEquals(3, secondPlayer.getDistrictsInHand().size());
        assertEquals(1, firstBuilder.getDistrictsInHand().size());
        firstBuilder.getGameCharacter().specialEffect(firstBuilder, game, true, secondPlayer);
        assertEquals(1, secondPlayer.getDistrictsInHand().size());
        assertEquals(3, firstBuilder.getDistrictsInHand().size());
    }

    @Test
    void switchWithDeckTest() { //Tests if the card has been switched
        Player firstBuilder = new Bot("Player 1");
        Game game = new Game();
        game.init();
        game.setPlayers(firstBuilder);
        firstBuilder.setGameCharacter(magician);
        District distToSwitch = new District("ToSwitch", 0, DistrictColor.TRADE);
        firstBuilder.addDistrictInHand(distToSwitch);
        firstBuilder.getGameCharacter().specialEffect(firstBuilder, game, false);
        assertNotEquals(distToSwitch, firstBuilder.getDistrictsInHand().get(0));
    }
}