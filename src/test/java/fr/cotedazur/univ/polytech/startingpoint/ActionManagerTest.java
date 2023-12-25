package fr.cotedazur.univ.polytech.startingpoint;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.Character1;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.King;

public class ActionManagerTest {
    King king;
    Character1 character1;
    Bot player;
    Game game;

    @BeforeEach
    void setUp() {
        king = new King();
        character1 = new Character1();
        player = new Bot("Bot") {
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
        player.chooseCharacter(character1);
        ActionManager.applySpecialEffect(player, game);
        assertEquals(4, player.getGold());
    }

    @Test
    void chooseCharTestChar1() {
        game.shuffleChars(2);
        player.chooseCharacterAlgorithm(game);
        assertEquals(player.getCharacter().name, "Personnage 1");
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

}
