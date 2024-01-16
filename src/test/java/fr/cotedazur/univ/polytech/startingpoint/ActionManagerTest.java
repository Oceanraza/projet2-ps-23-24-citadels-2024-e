package fr.cotedazur.univ.polytech.startingpoint;

import static org.junit.jupiter.api.Assertions.*;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictAlreadyBuiltException;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.*;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.BotAlgorithms.EinsteinAlgo;
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
        bot.buildDistrict(district1);
        bot.buildDistrict(district2);
        bot.buildDistrict(district3);
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
    void chooseCharTestKingPower() {
        game.shuffleChars(4);
        bot.setGold(8);
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        bot.addDistrictInHand(game.drawCard());
        for (int i = 0; i < 7; i++) {
            District district = game.drawCard();
            try {
                bot.addDistrictBuilt(district);
            } catch (DistrictAlreadyBuiltException e) {
                i--;
            }
        }
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Roi", bot.getCharacterName());
    }
    @Test
    void chooseCharTestEvequeGold(){
        game.shuffleChars(4);
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.religieux);
        District district3 = new District("Quartier 3", 0, DistrictColor.religieux);
        bot.buildDistrict(district1);
        bot.buildDistrict(district2);
        bot.buildDistrict(district3);
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Eveque", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }

    @Test
    void chooseCharTestMarchandGold(){
        game.shuffleChars(4);
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.marchand);
        District district3 = new District("Quartier 3", 0, DistrictColor.marchand);
        bot.buildDistrict(district1);
        bot.buildDistrict(district2);
        bot.buildDistrict(district3);
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Marchand", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }

    @Test
    void chooseCharTestCondottiereGold(){
        game.shuffleChars(4);
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.militaire);
        District district3 = new District("Quartier 3", 0, DistrictColor.militaire);
        bot.buildDistrict(district1);
        bot.buildDistrict(district2);
        bot.buildDistrict(district3);
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Condottiere", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }

    @Test
    void chooseCharTestKingGold(){
        game.shuffleChars(4);
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.noble);
        District district3 = new District("Quartier 3", 0, DistrictColor.religieux);
        bot.buildDistrict(district1);
        bot.buildDistrict(district2);
        bot.buildDistrict(district3);
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Roi", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }




}
