package fr.cotedazur.univ.polytech.startingpoint;

import static org.junit.jupiter.api.Assertions.*;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.*;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
;
import java.util.ArrayList;
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
        assertEquals(district2,Condottiere.getLowestDistrict(bot).get());
    }
    @Test
    void getWrongDistrictTest(){
        assertEquals(Optional.empty(),Condottiere.getLowestDistrict(bot));
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
        expectedOutput.add(thirdPlayer);
        expectedOutput.add(fourthPlayer);

        assertEquals(expectedOutput, Condottiere.getSortedPlayersByScore(game,firstBuilder));
    }
    @Test
    void WarlordGameCanDestroyFirstTest(){
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Player thirdPlayer = new Bot("Player 3");
        Player fourthPlayer = new Bot("Player 4");
        Game game = new Game();
        game.setPlayers(firstBuilder, secondPlayer, thirdPlayer, fourthPlayer);
        firstBuilder.setGold(5);
        firstBuilder.setGameCharacter(condottiere);
        for (int i = 0; i < 8; i++) { //builds 8
            firstBuilder.getDistrictsBuilt().add(new District("test", 4, DistrictColor.marchand));
            secondPlayer.getDistrictsBuilt().add(new District("test", 5, DistrictColor.marchand));
            thirdPlayer.getDistrictsBuilt().add(new District("test", 2, DistrictColor.marchand));
            fourthPlayer.getDistrictsBuilt().add(new District("test", 1, DistrictColor.marchand));
        }

        assertEquals(8,secondPlayer.getDistrictsBuilt().size());
        firstBuilder.getGameCharacter().specialEffect(firstBuilder,game);
        assertEquals(1,firstBuilder.getGold());
        assertEquals(7,secondPlayer.getDistrictsBuilt().size());
    }




}
