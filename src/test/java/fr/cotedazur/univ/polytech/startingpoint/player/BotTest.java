package fr.cotedazur.univ.polytech.startingpoint.player;

import fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.character.King;
import fr.cotedazur.univ.polytech.startingpoint.character.Warlord;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {
    Game game;
    Bot bot;
    District district1;
    District district2;
    King king;
    Warlord warlord;

    @BeforeEach
    void setUp() {
        InGameLogger.setup();
        InGameLogger.setGlobalLogLevel(Level.OFF);

        game = new Game();
        bot = new Bot("Bot");
        king = new King();
        warlord = new Warlord();

        district1 = new District("Quartier 1", 3, DistrictColor.NOBLE);
        district2 = new District("Quartier 2", 4, DistrictColor.MILITARY);
    }

    @Test
    void canBuildDistrictThisTurnTest() {
        bot.setGold(1);
        bot.addDistrictInHand(district1);
        bot.addDistrictInHand(district2);

        assertTrue(bot.canBuildDistrictThisTurn());
    }

    @Test
    void cannotBuildDistrictThisTurnTest() {
        bot.setGold(0);
        bot.addDistrictInHand(district1);
        bot.addDistrictInHand(district2);

        assertFalse(bot.canBuildDistrictThisTurn());
    }

    @Test
    void listOfCharactersTest() {
        List<GameCharacter> listOfCharacters = new ArrayList<>();
        listOfCharacters.add(king);

        assertTrue(bot.isCharInList(listOfCharacters, GameCharacterRole.KING));
        assertEquals(king, bot.getCharInList(listOfCharacters, GameCharacterRole.KING));

        assertFalse(bot.isCharInList(listOfCharacters, GameCharacterRole.WARLORD));
    }

    @Test
    void chooseCharacterTest() {
        game.init();
        game.shuffleCharacters();
        List<GameCharacter> listOfCharacters = game.getAvailableChars();

        bot.chooseChar(game, GameCharacterRole.KING);
        assertEquals(GameCharacterRole.KING, bot.getGameCharacter().getRole());
        assertFalse(bot.isCharInList(listOfCharacters, GameCharacterRole.KING));
    }
}
