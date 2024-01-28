package fr.cotedazur.univ.polytech.startingpoint;

import static org.junit.jupiter.api.Assertions.*;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.character.*;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

class MagicianTest {
    King king;
    Bishop bishop;
    Warlord warlord;
    Magician magician;
    Bot bot;
    Game game;

    @BeforeEach
    void setUp() {
        king = new King();
        bishop = new Bishop();
        warlord = new Warlord();
        magician = new Magician();
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

        District distToSwitch = new District("test", 5, DistrictColor.marchand);
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
        District distToSwitch = game.drawCard();
        firstBuilder.addDistrictInHand(distToSwitch);
        firstBuilder.addDistrictInHand(distToSwitch);
        firstBuilder.getGameCharacter().specialEffect(firstBuilder, game, false);
        assertNotEquals(distToSwitch, firstBuilder.getDistrictsInHand().get(0));
    }
}