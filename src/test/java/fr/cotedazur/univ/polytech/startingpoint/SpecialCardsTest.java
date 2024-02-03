package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.character.Merchant;
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

import static fr.cotedazur.univ.polytech.startingpoint.Main.finalChoice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpecialCardsTest {
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

    @Test
    void huntedQuarterTest() {
        Bot firstPlayer = new Bot("Player 1");
        Bot secondPlayer = new Bot("Player 2");
        King king = new King();
        Merchant merchant = new Merchant();
        Game newGame = new Game();

        firstPlayer.botAlgo = new EinsteinAlgo();
        firstPlayer.botAlgo.setPlayer(firstPlayer);
        secondPlayer.botAlgo = new EinsteinAlgo();
        secondPlayer.botAlgo.setPlayer(secondPlayer);

        // Create list of players
        newGame.setPlayers(firstPlayer, secondPlayer);
        firstPlayer.setGold(0);
        secondPlayer.setGold(0);

        assertEquals(1, gameState.getTurn());
        for (Player player : newGame.getPlayers()) {
            player.getCity().addDistrict(new District("Marchand", 0, DistrictColor.marchand), gameState);
            player.getCity().addDistrict(new District("Militaire", 0, DistrictColor.militaire), gameState);
            player.getCity().addDistrict(new District("Religieux", 0, DistrictColor.religieux), gameState);
            player.getCity().addDistrict(new District("Special", 0, DistrictColor.special), gameState);
        }
        firstPlayer.getCity().addDistrict(new District("Cour des miracles", 0, DistrictColor.special), gameState);
        assertEquals(5, firstPlayer.getCity().getDistrictsBuilt().size());

        gameState.nextTurn();
        assertEquals(2, gameState.getTurn());
        secondPlayer.getCity().addDistrict(new District("Cour des miracles", 0, DistrictColor.special), gameState);
        District huntedQuarter = secondPlayer.getCity().getDistrictsBuilt().get(4);
        assertTrue(huntedQuarter.getTurnBuilt().isPresent());
        assertEquals(2, huntedQuarter.getTurnBuilt().get());

        finalChoice(newGame.getPlayers(), gameState);
        assertEquals(3, firstPlayer.calculateScore()); // +3 HuntedQuarter goes from special to noble
        assertEquals(0, secondPlayer.calculateScore()); // HuntedQuarter's effect can't be used (built at the last turn)
    }
}
