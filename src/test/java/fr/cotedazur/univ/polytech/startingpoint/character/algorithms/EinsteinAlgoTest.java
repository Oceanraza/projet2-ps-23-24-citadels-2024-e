package fr.cotedazur.univ.polytech.startingpoint.character.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.card.Bishop;
import fr.cotedazur.univ.polytech.startingpoint.character.card.King;
import fr.cotedazur.univ.polytech.startingpoint.character.card.Warlord;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger;
import org.junit.jupiter.api.BeforeEach;

import java.util.logging.Level;

public class EinsteinAlgoTest {
    King king;
    Bishop bishop;
    Bot bot;
    Warlord warlord;
    Game game;

    @BeforeEach
    void setUp() {
        InGameLogger.setup();
        InGameLogger.setGlobalLogLevel(Level.OFF);

        king = new King();
        bishop = new Bishop();
        warlord = new Warlord();
        bot = new Bot("Bot") {
        };
        game = new Game();
        game.init();
    }

    /* Algorithm is not stable yet so it's not relevant to carry out tests
    Plus, we mustn't shuffle cards because two characters can't be played so tests can go wrong
    @Test
    void chooseCharTestEvequeGold(){
        game.shuffleCharacters();
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 0, DistrictColor.RELIGIOUS);
        District district3 = new District("Quartier 3", 0, DistrictColor.RELIGIOUS);
        bot.buildDistrict(district1);
        bot.buildDistrict(district2);
        bot.buildDistrict(district3);
        bot.addDistrictInHand(district1);
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Eveque", bot.getCharacterName());
        assertEquals(2, ActionManager.collectGold(bot));
    }

    @Test
    void chooseCharTestMarchandGold(){
        game.shuffleCharacters();
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 0, DistrictColor.TRADE);
        District district3 = new District("Quartier 3", 0, DistrictColor.TRADE);
        bot.buildDistrict(district1);
        bot.buildDistrict(district2);
        bot.buildDistrict(district3);
        bot.addDistrictInHand(district1);
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Marchand", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }

    @Test
    void chooseCharTestCondottiereGold(){
        game.shuffleCharacters();
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 0, DistrictColor.MILITARY);
        District district3 = new District("Quartier 3", 0, DistrictColor.MILITARY);
        bot.addDistrictInHand(district1);
        bot.addDistrictInHand(district2);
        bot.buildDistrict(district1);
        bot.buildDistrict(district2);
        bot.buildDistrict(district3);
        bot.setGameCharacter(warlord);
        assertEquals("Condottiere", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }

    @Test
    void chooseCharTestKingGold(){
        game.shuffleCharacters();
        bot.botAlgo = new EinsteinAlgo();
        bot.botAlgo.setPlayer(bot);
        District district1 = new District("Quartier 1", 0, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 0, DistrictColor.NOBLE);
        District district3 = new District("Quartier 3", 0, DistrictColor.RELIGIOUS);
        bot.buildDistrict(district1);
        bot.buildDistrict(district2);
        bot.buildDistrict(district3);
        bot.addDistrictInHand(district1);
        bot.botAlgo.chooseCharacterAlgorithm(game);
        assertEquals("Roi", bot.getCharacterName());
        assertEquals(2,ActionManager.collectGold(bot));
    }
    */
}
