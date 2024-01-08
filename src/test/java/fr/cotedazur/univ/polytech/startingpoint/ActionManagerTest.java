package fr.cotedazur.univ.polytech.startingpoint;

import static org.junit.jupiter.api.Assertions.*;

<<<<<<< HEAD
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.*;
import fr.cotedazur.univ.polytech.startingpoint.players.Bot;
=======
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cotedazur.univ.polytech.startingpoint.characters.Character1;
import fr.cotedazur.univ.polytech.startingpoint.characters.King;

public class ActionManagerTest {
    King king;
<<<<<<< HEAD
    Eveque eveque;
    Bot bot;
=======
    Character1 character1;
    Bot player;
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c
    Game game;

    @BeforeEach
    void setUp() {
        king = new King();
<<<<<<< HEAD
        eveque = new Eveque();
        bot = new Bot("Bot") {
=======
        character1 = new Character1();
        player = new Bot("Bot") {
>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c
        };
        game = new Game();
        game.init();
    }

    @Test
    void updateGoldTestWithoutDistrict() {
        player.chooseCharacter(king);
        assertEquals(0, ActionManager.updateGold(player));
    }

    @Test
    void updateGoldTestWithDistrict() {
        player.chooseCharacter(king);
        District district1 = new District("Quartier 1", 0, DistrictColor.noble);
        District district2 = new District("Quartier 2", 0, DistrictColor.noble);
        District district3 = new District("Quartier 3", 0, DistrictColor.religieux);
        player.build(district1);
        player.build(district2);
        player.build(district3);
        assertEquals(2, ActionManager.updateGold(player));
    }

    @Test
    void getCrownTest() {
        player.chooseCharacter(king);
        ActionManager.applySpecialEffect(player, game);
        assertEquals("Bot", game.getCrown().getOwner().getName());
    }

    @Test
    void getGoldTest() {
<<<<<<< HEAD
        bot.setGameCharacter(eveque);
        ActionManager.applySpecialEffect(bot, game);
        assertEquals(2, bot.getGold());
    }


    @Test
    void chooseCharTestKingPower() {
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
     void chooseCharTestEvequeGold(){
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

     @Test
     void chooseCharTestMarchandGold(){
         game.shuffleChars(4);
         District district1 = new District("Quartier 1", 0, DistrictColor.noble);
         District district2 = new District("Quartier 2", 0, DistrictColor.marchand);
         District district3 = new District("Quartier 3", 0, DistrictColor.marchand);
         bot.build(district1);
         bot.build(district2);
         bot.build(district3);
         bot.chooseCharacterAlgorithm(game);
         assertEquals("Marchand", bot.getCharacterName());
         assertEquals(2,ActionManager.collectGold(bot));
     }

     @Test
     void chooseCharTestCondottiereGold(){
         game.shuffleChars(4);
         District district1 = new District("Quartier 1", 0, DistrictColor.noble);
         District district2 = new District("Quartier 2", 0, DistrictColor.militaire);
         District district3 = new District("Quartier 3", 0, DistrictColor.militaire);
         bot.build(district1);
         bot.build(district2);
         bot.build(district3);
         bot.chooseCharacterAlgorithm(game);
         assertEquals("Condottiere", bot.getCharacterName());
         assertEquals(2,ActionManager.collectGold(bot));
     }

     @Test
     void chooseCharTestKingGold(){
         game.shuffleChars(4);
         District district1 = new District("Quartier 1", 0, DistrictColor.noble);
         District district2 = new District("Quartier 2", 0, DistrictColor.noble);
         District district3 = new District("Quartier 3", 0, DistrictColor.religieux);
         bot.build(district1);
         bot.build(district2);
         bot.build(district3);
         bot.chooseCharacterAlgorithm(game);
         assertEquals("Roi", bot.getCharacterName());
         assertEquals(2,ActionManager.collectGold(bot));
     }




=======
        player.chooseCharacter(character1);
        ActionManager.applySpecialEffect(player, game);
        assertEquals(4, player.getGold());
    }

    @Test
    void chooseCharTestChar1() {
        game.shuffleChars(2);
        player.chooseCharacterAlgorithm(game);
        assertEquals(player.getCharacter().name, "Character1");
    }

    @Test
    void chooseCharTestKing() {
        game.shuffleChars(2);
        player.setGold(8);
        player.districtsInHand.add(game.drawCard());
        player.districtsBuilt.add(game.drawCard());
        player.districtsBuilt.add(game.drawCard());
        player.districtsBuilt.add(game.drawCard());
        player.districtsBuilt.add(game.drawCard());
        player.districtsBuilt.add(game.drawCard());
        player.districtsBuilt.add(game.drawCard());
        player.districtsBuilt.add(game.drawCard());
        player.chooseCharacterAlgorithm(game);
        assertEquals(player.getCharacter().name, "Roi");
    }

>>>>>>> e2d073b1f1402278b8dda95bd247d95f2ca4354c
}
