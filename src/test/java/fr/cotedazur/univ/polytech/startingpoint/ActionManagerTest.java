package fr.cotedazur.univ.polytech.startingpoint;

import static org.junit.jupiter.api.Assertions.*;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictAlreadyBuiltException;
import fr.cotedazur.univ.polytech.startingpoint.character.*;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.EinsteinAlgo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ActionManagerTest {
    King king;
    Bishop bishop;
    Warlord warlord;
    Bot bot;
    Game game;
    GameState gameState;

    @BeforeEach
    void setUp(){
        game = new Game();
        game.init();
        gameState = new GameState();
        king = new King();
        bishop = new Bishop();
        warlord = new Warlord();
        bot = new Bot("Bot") {};
    }

    @Test
    void updateGoldWithoutDistrictTest() {
        bot.setGameCharacter(king);
        assertEquals(0, ActionManager.collectGold(bot));
    }

    @Test
    void updateGoldWithDistrictTest() {
        bot.setGameCharacter(king);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.noble);
        District district3 = new District("Quartier 3", 0, DistrictColor.religieux);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(district3, gameState);
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
        bot.setGameCharacter(bishop);
        ActionManager.applySpecialEffect(bot, game);
        assertEquals(2, bot.getGold());
    }

    @Test
    void chooseCharTestKingPower() {
        game.shuffleChars();
        bot.setGold(8);
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        bot.addDistrictInHand(game.drawCard(bot));
        for (int i = 0; i < 7; i++) {
            District district = game.drawCard(bot);
            try {
                bot.addDistrictBuilt(district, gameState);
            } catch (DistrictAlreadyBuiltException e) {
                i--;
            }
        }
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Roi", bot.getCharacterName());
    }

    @Test
    void chooseCharBishopGoldTest(){
        game.shuffleChars();
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.religieux);
        District district3 = new District("Quartier 3", 0, DistrictColor.religieux);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(district3, gameState);
        bot.addDistrictInHand(district1);
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Eveque", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }

    @Test
    void chooseCharMerchantGoldTest(){
        game.shuffleChars();
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.marchand);
        District district3 = new District("Quartier 3", 0, DistrictColor.marchand);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(district3, gameState);
        bot.addDistrictInHand(district1);
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Marchand", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }

    @Test
    void chooseCharWarlordGoldTest(){
        game.shuffleChars();
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.militaire);
        District district3 = new District("Quartier 3", 0, DistrictColor.militaire);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(district3, gameState);
        bot.addDistrictInHand(district1);
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Condottiere", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }

    @Test
    void chooseCharKingGoldTest(){
        game.shuffleChars();
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.noble);
        District district3 = new District("Quartier 3", 0, DistrictColor.religieux);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(district3, gameState);
        bot.addDistrictInHand(district1);
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Roi", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }

    @Test
    void magicSchoolBishopTest(){
        District district1 = new District("Quartier 1", 0, DistrictColor.religieux);
        District district2 = new District("Quartier 2", 0, DistrictColor.religieux);
        District magicSchool = new District("Ecole de magie", 0, DistrictColor.special);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(magicSchool, gameState);
        Bishop bishop = new Bishop();
        bot.setGameCharacter(bishop);
        assertEquals("Eveque", bot.getCharacterName());
        assertEquals(3,ActionManager.collectGold(bot));
    }

    @Test
    void magicSchoolMerchantTest(){
        District district1 = new District("Quartier 1", 0, DistrictColor.marchand);
        District district2 = new District("Quartier 2", 0, DistrictColor.marchand);
        District magicSchool = new District("Ecole de magie", 0, DistrictColor.special);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(magicSchool, gameState);
        Merchant merchant = new Merchant();
        bot.setGameCharacter(merchant);
        assertEquals("Marchand", bot.getCharacterName());
        assertEquals(3,ActionManager.collectGold(bot));
    }

    @Test
    void magicSchoolWarlordTest(){
        District district1 = new District("Quartier 1", 0, DistrictColor.militaire);
        District district2 = new District("Quartier 2", 0, DistrictColor.militaire);
        District magicSchool = new District("Ecole de magie", 0, DistrictColor.special);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(magicSchool, gameState);
        Warlord warlord = new Warlord();
        bot.setGameCharacter(warlord);
        assertEquals("Condottiere", bot.getCharacterName());
        assertEquals(3,ActionManager.collectGold(bot));
    }

    @Test
    void magicSchoolKingTest(){
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.noble);
        District magicSchool = new District("Ecole de magie", 0, DistrictColor.special);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(magicSchool, gameState);
        King king = new King();
        bot.setGameCharacter(king);
        assertEquals("Roi", bot.getCharacterName());
        assertEquals(3,ActionManager.collectGold(bot));
    }

    @Test
    void magicSchoolOtherCharacterTest(){
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.religieux);
        District magicSchool = new District("Ecole de magie", 0, DistrictColor.special);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(magicSchool, gameState);
        Magician magician = new Magician();
        bot.setGameCharacter(magician);
        assertEquals(0,ActionManager.collectGold(bot));
    }
}
