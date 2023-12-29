package fr.cotedazur.univ.polytech.startingpoint;

import static org.junit.jupiter.api.Assertions.*;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.*;
import fr.cotedazur.univ.polytech.startingpoint.players.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
;
 class ActionManagerTest {
    King king;
    Eveque eveque;
    Bot bot;
    Game game;

    @BeforeEach
    void setUp() {
        king = new King();
        eveque = new Eveque();
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
        bot.setGameCharacter(eveque);
        ActionManager.applySpecialEffect(bot, game);
        assertEquals(2, bot.getGold());
    }


    @Test
    void chooseCharTestKing() {
        game.shuffleChars(4);
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
        assertEquals("Roi", bot.getCharacterName());
    }
    @Test
     void chooseCharTest(){
        game.shuffleChars(4);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.religieux);
        District district3 = new District("Quartier 3", 0, DistrictColor.religieux);
        bot.build(district1);
        bot.build(district2);
        bot.build(district3);
        bot.chooseCharacterAlgorithm(game);
        assertEquals("Eveque", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }


}
