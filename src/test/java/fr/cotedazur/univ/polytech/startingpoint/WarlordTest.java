package fr.cotedazur.univ.polytech.startingpoint;

import static fr.cotedazur.univ.polytech.startingpoint.Main.calculateScores;
import static java.lang.Character.LINE_SEPARATOR;
import static org.junit.jupiter.api.Assertions.*;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.*;
import fr.cotedazur.univ.polytech.startingpoint.players.Bot;
import fr.cotedazur.univ.polytech.startingpoint.players.BotAlgorithms.EinsteinAlgo;
import fr.cotedazur.univ.polytech.startingpoint.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
;import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class WarlordTest {
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
        bot = new Bot("Bot") {
        };
        game = new Game();
        game.init();
    }
    @Test
    void getLowestDistrictTest(){
        District district1 = new District("Quartier 1", 3, DistrictColor.noble);
        District district2 = new District("Quartier 2", 1, DistrictColor.noble);
        District district3 = new District("Quartier 3", 4, DistrictColor.religieux);
        bot.build(district1);
        bot.build(district2);
        bot.build(district3);
        assertEquals(district2, bot.getLowestDistrict().get());
    }
    @Test
    void getWrongDistrictTest(){
        assertEquals(Optional.empty(),bot.getLowestDistrict());
    }
    @Test
    void getSortedPlayersByScoreTest(){

        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Player thirdPlayer = new Bot("Player 3");
        Player fourthPlayer = new Bot("Player 4");
        Game game = new Game();
        game.setPlayers(firstBuilder, secondPlayer, thirdPlayer, fourthPlayer);

        for (int i = 0; i < 8; i++) {
            firstBuilder.getDistrictsBuilt().add(new District("test", 4, DistrictColor.marchand));
            secondPlayer.getDistrictsBuilt().add(new District("test", 5, DistrictColor.marchand));
            thirdPlayer.getDistrictsBuilt().add(new District("test", 2, DistrictColor.marchand));
            fourthPlayer.getDistrictsBuilt().add(new District("test", 1, DistrictColor.marchand));
        }
        ArrayList<Player> expectedOutput = new ArrayList<>();
        expectedOutput.add(secondPlayer);
        expectedOutput.add(firstBuilder);
        expectedOutput.add(thirdPlayer);
        expectedOutput.add(fourthPlayer);

        assertEquals(expectedOutput, game.getSortedPlayersByScore());
    }
    @Test
    void WarlordGameCanDestroyFirstTest(){
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Game game = new Game();
        game.setPlayers(firstBuilder, secondPlayer);
        firstBuilder.setGold(5);
        firstBuilder.setGameCharacter(condottiere);

        District distToDestroy = new District("test", 5, DistrictColor.marchand);
        secondPlayer.getDistrictsBuilt().add(distToDestroy);

        assertEquals(1,secondPlayer.getDistrictsBuilt().size());
        firstBuilder.getGameCharacter().specialEffect(firstBuilder,game,secondPlayer,distToDestroy);
        assertEquals(1,firstBuilder.getGold());
        assertEquals(0,secondPlayer.getDistrictsBuilt().size());
    }


}
