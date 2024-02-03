package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.character.Warlord;
import fr.cotedazur.univ.polytech.startingpoint.character.Bishop;
import fr.cotedazur.univ.polytech.startingpoint.character.King;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.EinsteinAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpecialCardsTests {
    King king;
    Bishop bishop;
    Warlord warlord;
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
    }

    @Test
    void donjonTest(){
        Player firstBuilder = new Bot("Player 1",new EinsteinAlgo());
        Bot secondPlayer = new Bot("Player 2",new EinsteinAlgo());
        Player thirdPlayer = new Bot("Player 3",new EinsteinAlgo());
        Player fourthPlayer = new Bot("Player 4",new EinsteinAlgo());
        Game game = new Game();
        game.setPlayers(firstBuilder, secondPlayer, thirdPlayer, fourthPlayer);
        firstBuilder.setGold(4);
        secondPlayer.setGold(4);
        thirdPlayer.setGameCharacter(king);
        firstBuilder.setGameCharacter(king);
        secondPlayer.setGameCharacter(king);
        fourthPlayer.setGameCharacter(king);

        firstBuilder.buildDistrict(new District("Donjon", 3, DistrictColor.special), gameState);

        assertEquals(1, firstBuilder.getCity().getDistrictsBuilt().size());
        secondPlayer.botAlgo.warlordAlgorithm(game); //La seule carte construite est le donjon, qui ne peut être détruit, cet algo ne détruit alors rien
        assertEquals(1, firstBuilder.getCity().getDistrictsBuilt().size());
    }
}
