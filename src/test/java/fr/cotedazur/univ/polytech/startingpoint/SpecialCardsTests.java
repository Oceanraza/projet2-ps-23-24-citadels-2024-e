package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.Condottiere;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.Eveque;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.King;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.BotAlgorithms.EinsteinAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpecialCardsTests {
    King king;
    Eveque eveque;
    Condottiere condottiere;
    Bot bot;
    Game game;

    @BeforeEach
    void setUp() {
        king = new King();
        eveque = new Eveque();
        condottiere = new Condottiere();
        Bot bot = new Bot("Bot");
        game = new Game();
        game.init();
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

        firstBuilder.buildDistrict(new District("Donjon", 3, DistrictColor.special));

        assertEquals(firstBuilder.getCity().getDistrictsBuilt().size(),1);
        secondPlayer.botAlgo.warlordAlgorithm(game); //La seule carte construite est le dongeon, qui ne peut être détruit, cet algo ne détruit alors rien
        assertEquals(firstBuilder.getCity().getDistrictsBuilt().size(),1);
    }
}
