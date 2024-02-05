package fr.cotedazur.univ.polytech.startingpoint;

import static org.junit.jupiter.api.Assertions.*;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.character.*;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictAlreadyBuiltException;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.EinsteinAlgo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActionManagerTest {
    King king;
    Bishop bishop;
    Warlord warlord;
    Merchant merchant;
    Magician magician;
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
        merchant = new Merchant();
        magician = new Magician();

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
        District district1 = new District("Quartier 1", 0, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 0, DistrictColor.NOBLE);
        District district3 = new District("Quartier 3", 0, DistrictColor.RELIGIOUS);
        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(district3, gameState);
        assertEquals(2, ActionManager.collectGold(bot));
    }

    @Test
    void startOfTurnTest() {
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        bot.setGold(10);
        ActionManager.startOfTurn(game, bot);
        assertEquals(1, bot.getDistrictsInHand().size());

        District districtPicked = bot.getDistrictsInHand().get(0);
        bot.buildDistrict(districtPicked, gameState);
        bot.setGold(0);
        assertEquals(0, bot.getDistrictsInHand().size());

        bot.addDistrictInHand(districtPicked);
        ActionManager.startOfTurn(game, bot);
        assertEquals(2, bot.getDistrictsInHand().size());

        ActionManager.startOfTurn(game, bot);
        assertEquals(2, bot.getGold());
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
    void collectBishopGoldTest() {
        District district1 = new District("Quartier 1", 0, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 0, DistrictColor.RELIGIOUS);
        District district3 = new District("Quartier 3", 0, DistrictColor.RELIGIOUS);

        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(district3, gameState);

        bot.setGameCharacter(bishop);
        assertEquals(2, ActionManager.collectGold(bot));
    }

    @Test
    void collectMerchantGoldTest() {
        District district1 = new District("Quartier 1", 0, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 0, DistrictColor.TRADE);
        District district3 = new District("Quartier 3", 0, DistrictColor.TRADE);

        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(district3, gameState);

        bot.setGameCharacter(merchant);
        assertEquals(2, ActionManager.collectGold(bot));
    }

    @Test
    void collectWarlordGoldTest() {
        District district1 = new District("Quartier 1", 0, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 0, DistrictColor.MILITARY);
        District district3 = new District("Quartier 3", 0, DistrictColor.MILITARY);

        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(district3, gameState);

        bot.setGameCharacter(warlord);
        assertEquals(2, ActionManager.collectGold(bot));
    }

    @Test
    void collectKingGoldTest() {
        District district1 = new District("Quartier 1", 0, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 0, DistrictColor.NOBLE);
        District district3 = new District("Quartier 3", 0, DistrictColor.RELIGIOUS);

        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(district3, gameState);

        bot.setGameCharacter(king);
        assertEquals(2, ActionManager.collectGold(bot));
    }

    @Test
    void magicSchoolBishopTest(){
        District district1 = new District("Quartier 1", 0, DistrictColor.RELIGIOUS);
        District district2 = new District("Quartier 2", 0, DistrictColor.RELIGIOUS);
        District magicSchool = new District("Ecole de magie", 0, DistrictColor.SPECIAL);

        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(magicSchool, gameState);

        bot.setGameCharacter(bishop);
        assertEquals(2, ActionManager.collectGold(bot));
    }

    @Test
    void magicSchoolMerchantTest() {
        District district1 = new District("Quartier 1", 0, DistrictColor.TRADE);
        District district2 = new District("Quartier 2", 0, DistrictColor.TRADE);
        District magicSchool = new District("Ecole de magie", 0, DistrictColor.SPECIAL);

        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(magicSchool, gameState);

        bot.setGameCharacter(merchant);
        assertEquals(2, ActionManager.collectGold(bot));
    }

    @Test
    void magicSchoolWarlordTest() {
        District district1 = new District("Quartier 1", 0, DistrictColor.MILITARY);
        District district2 = new District("Quartier 2", 0, DistrictColor.MILITARY);
        District magicSchool = new District("Ecole de magie", 0, DistrictColor.SPECIAL);

        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(magicSchool, gameState);

        bot.setGameCharacter(warlord);
        assertEquals(2, ActionManager.collectGold(bot));
    }

    @Test
    void magicSchoolKingTest() {
        District district1 = new District("Quartier 1", 0, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 0, DistrictColor.NOBLE);
        District magicSchool = new District("Ecole de magie", 0, DistrictColor.SPECIAL);

        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(magicSchool, gameState);

        bot.setGameCharacter(king);
        assertEquals(2, ActionManager.collectGold(bot));
    }

    @Test
    void magicSchoolOtherCharacterTest() {
        District district1 = new District("Quartier 1", 0, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 0, DistrictColor.RELIGIOUS);
        District magicSchool = new District("Ecole de magie", 0, DistrictColor.SPECIAL);

        bot.buildDistrict(district1, gameState);
        bot.buildDistrict(district2, gameState);
        bot.buildDistrict(magicSchool, gameState);

        bot.setGameCharacter(magician);
        assertEquals(0, ActionManager.collectGold(bot));
    }

    @Test
    void manufactureTest() {
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);

        bot.setGold(3);
        bot.addDistrictBuilt(new District("Manufacture", 0, DistrictColor.special), gameState);
        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(3, bot.getDistrictsInHand().size());
    }

    @Test
    void laboratoryTest() {
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);

        bot.setGold(0);
        bot.addDistrictBuilt(new District("Laboratoire", 0, DistrictColor.special), gameState);
        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(0, bot.getGold());

        District marche = new District("Marche", 0, DistrictColor.marchand);
        bot.addDistrictInHand(marche);
        bot.addDistrictInHand(marche);
        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(1, bot.getGold());

        bot.addDistrictBuilt(marche, gameState);
        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(2, bot.getGold());

        bot.addDistrictInHand(new District("District1", 0, DistrictColor.marchand));
        bot.addDistrictInHand(new District("District2", 0, DistrictColor.marchand));
        bot.addDistrictInHand(new District("District3", 0, DistrictColor.marchand));
        bot.addDistrictInHand(new District("District4", 0, DistrictColor.marchand));
        bot.addDistrictInHand(new District("District5", 0, DistrictColor.marchand));
        bot.addDistrictInHand(new District("District6", 0, DistrictColor.marchand));
        bot.addDistrictInHand(new District("District7", 0, DistrictColor.marchand));
        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(3, bot.getGold());

        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(3, bot.getGold());
    }
}
