package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.board.Deck;
import fr.cotedazur.univ.polytech.startingpoint.character.card.*;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart.EinsteinAlgo;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;


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
        CitadelsLogger.setup();
        CitadelsLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
        game.init();
        gameState = new GameState();
        king = new King();
        bishop = new Bishop();
        warlord = new Warlord();
        merchant = new Merchant();
        magician = new Magician();
        bot = new Bot("Bot") {
        };
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
        Bot bot = new Bot("Bot", new EinsteinAlgo());
        bot.setGold(10);
        bot.setGameCharacter(new King());
        ActionManager.startOfTurn(game, bot);
        assertEquals(1, bot.getDistrictsInHand().size());
        District districtPicked = bot.getDistrictsInHand().get(0);

        bot.buildDistrict(districtPicked, gameState);
        bot.setGold(0);
        assertEquals(0, bot.getDistrictsInHand().size());

        bot.addDistrictInHand(districtPicked);
        ActionManager.startOfTurn(game, bot);
        assertEquals(2, bot.getDistrictsInHand().size());
        District newDistrictPicked = bot.getDistrictsInHand().get(bot.getDistrictsInHand().size() - 1);

        ActionManager.startOfTurn(game, bot);
        while (newDistrictPicked.equals(districtPicked)) {
            ActionManager.startOfTurn(game, bot);
            newDistrictPicked = bot.getDistrictsInHand().get(bot.getDistrictsInHand().size() - 1);
        }
        assertEquals(2, bot.getGold());
    }
    @Test
    void getCrownTest() {
        bot.setGameCharacter(king);
        ActionManager.applyCharacterSpecialEffect(bot, game);
        assertEquals("Bot", game.getCrown().getOwner().getName());
    }

    @Test
    void getGoldTest() {
        bot.setGameCharacter(bishop);
        ActionManager.applyCharacterSpecialEffect(bot, game);
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
        Bot bot = new Bot("Bot", new EinsteinAlgo());

        bot.setGold(3);
        bot.addDistrictBuilt(new District("Manufacture", 0, DistrictColor.SPECIAL), gameState);
        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(3, bot.getDistrictsInHand().size());
    }

    @Test
    void laboratoryTest() {
        Bot bot = new Bot("Bot", new EinsteinAlgo());

        bot.setGold(0);
        bot.addDistrictBuilt(new District("Laboratoire", 0, DistrictColor.SPECIAL), gameState);
        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(0, bot.getGold());

        District marche = new District("Marche", 0, DistrictColor.TRADE);
        bot.addDistrictInHand(marche);
        bot.addDistrictInHand(marche);
        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(1, bot.getGold());

        bot.addDistrictBuilt(marche, gameState);
        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(2, bot.getGold());

        bot.addDistrictInHand(new District("District1", 0, DistrictColor.TRADE));
        bot.addDistrictInHand(new District("District2", 0, DistrictColor.TRADE));
        bot.addDistrictInHand(new District("District3", 0, DistrictColor.TRADE));
        bot.addDistrictInHand(new District("District4", 0, DistrictColor.TRADE));
        bot.addDistrictInHand(new District("District5", 0, DistrictColor.TRADE));
        bot.addDistrictInHand(new District("District6", 0, DistrictColor.TRADE));
        bot.addDistrictInHand(new District("District7", 0, DistrictColor.TRADE));
        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(3, bot.getGold());

        ActionManager.applySpecialCardsEffect(game, bot);
        assertEquals(3, bot.getGold());
    }

    @Test
    void graveyardTest() {
        Bot condottiere = new Bot("Condottiere", new EinsteinAlgo());
        Bot graveyardOwner = new Bot("Graveyard owner", new EinsteinAlgo());
        condottiere.setGameCharacter(new Warlord());
        graveyardOwner.setGameCharacter(new King());

        game.setPlayers(condottiere, graveyardOwner);
        Deck deck = game.getDeck();

        District builtDistrict = new District("District1", 0, DistrictColor.MILITARY);
        graveyardOwner.buildDistrict(builtDistrict, gameState);
        assertEquals(1, graveyardOwner.getCity().getDistrictsBuilt().size());

        condottiere.getGameCharacter().specialEffect(condottiere, game, graveyardOwner, builtDistrict);
        // District is destroyed because graveyardOwner has no graveyard yet
        assertEquals(0, graveyardOwner.getCity().getDistrictsBuilt().size());

        // Test if the destroyed district is put at the bottom of the deck
        assertEquals(deck.getCards().get(0), builtDistrict);

        graveyardOwner.setGold(5);
        District graveyard = new District("Cimetiere", 5, DistrictColor.SPECIAL);
        graveyardOwner.buildDistrict(graveyard, gameState);
        graveyardOwner.buildDistrict(builtDistrict, gameState);
        assertEquals(2, graveyardOwner.getCity().getDistrictsBuilt().size());

        assertEquals(0, graveyardOwner.getGold());
        condottiere.getGameCharacter().specialEffect(condottiere, game, graveyardOwner, builtDistrict);
        // District is destroyed because graveyardOwner has no money to get it back, he still has the graveyard
        assertEquals(1, graveyardOwner.getCity().getDistrictsBuilt().size());
        assertEquals(0, graveyardOwner.getDistrictsInHand().size());

        graveyardOwner.setGold(1);
        graveyardOwner.buildDistrict(builtDistrict, gameState);
        assertEquals(2, graveyardOwner.getCity().getDistrictsBuilt().size());

        condottiere.getGameCharacter().specialEffect(condottiere, game, graveyardOwner, builtDistrict);
        // GraveyardOwner has enough money to get the district back in his hand
        assertEquals(0, graveyardOwner.getGold());
        assertEquals(1, graveyardOwner.getCity().getDistrictsBuilt().size());
        assertEquals(1, graveyardOwner.getDistrictsInHand().size());

        graveyardOwner.buildDistrict(builtDistrict, gameState);
        graveyardOwner.getDistrictsInHand().clear();
        graveyardOwner.setGameCharacter(new Warlord());

        graveyardOwner.setGold(1);
        condottiere.getGameCharacter().specialEffect(condottiere, game, graveyardOwner, builtDistrict);
        // GraveyardOwner is a Warlord, he can't use the graveyard and his district is destroyed by the other warlord
        assertEquals(1, graveyardOwner.getGold());
        assertEquals(1, graveyardOwner.getCity().getDistrictsBuilt().size());
        assertEquals(0, graveyardOwner.getDistrictsInHand().size());
    }
}
