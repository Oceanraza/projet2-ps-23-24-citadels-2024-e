package fr.cotedazur.univ.polytech.startingpoint;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.Character1;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.King;

public class ActionManagerTest {
    King king;
    Character1 character1;
    Bot bot;
    Game game;

    @BeforeEach
    void setUp() {
        king = new King();
        character1 = new Character1();
        bot = new Bot("Bot") {
        };
        game = new Game();
        game.init();
    }

    @Test
    void updateGoldTestWithoutDistrict() {
        bot.setGameCharacter(king);
        assertEquals(0, ActionManager.collectGold(bot));
    }

    @Test
    void updateGoldTestWithDistrict() {
        bot.setGameCharacter(king);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.noble);
        District district3 = new District("Quartier 3", 0, DistrictColor.religieux);
        bot.build(district1);
        bot.build(district2);
        bot.build(district3);
        assertEquals(2, ActionManager.collectGold(bot));
    }

    @Test
    void getCrownTest() {
        bot.setGameCharacter(king);
        ActionManager.applySpecialEffect(bot, game);
        assertEquals("Bot", game.getCrown().getOwner().getName());
    }

    @Test
    void getGoldTest() {
        bot.setGameCharacter(character1);
        ActionManager.applySpecialEffect(bot, game);
        assertEquals(4, bot.getGold());
    }

    @Test
    void chooseCharTestChar1() {
        game.shuffleChars(2);
        bot.chooseCharacterAlgorithm(game);
        assertEquals(bot.getGameCharacter().name, "Personnage 1");
    }

    @Test
    void chooseCharTestKing() {
        game.shuffleChars(2);
        bot.setGold(8);
        bot.addDistrictInHand(game.drawCard());
        bot.addDistrictBuilt(game.drawCard());
        bot.addDistrictBuilt(game.drawCard());
        bot.addDistrictBuilt(game.drawCard());
        bot.addDistrictBuilt(game.drawCard());
        bot.addDistrictBuilt(game.drawCard());
        bot.addDistrictBuilt(game.drawCard());
        bot.addDistrictBuilt(game.drawCard());
        bot.chooseCharacterAlgorithm(game);
        assertEquals(bot.getGameCharacter().name, "Roi");
    }

}
