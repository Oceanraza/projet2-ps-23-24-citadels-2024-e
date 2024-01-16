package fr.cotedazur.univ.polytech.startingpoint;

import static org.junit.jupiter.api.Assertions.*;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.character.*;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Optional;

class WarlordTest {
    King king;
    Bishop bishop;
    Warlord warlord;
    Bot bot;
    Game game;

    @BeforeEach
    void setUp() {
        king = new King();
        bishop = new Bishop();
        warlord = new Warlord();
        bot = new Bot("Bot") {
        };
        game = new Game();
        game.init();
    }
    @Test
    void getLowestDistrictTest(){
        District district1 = new District("Quartier 1", 3, DistrictColor.NOBLE);
        District district2 = new District("Quartier 2", 1, DistrictColor.NOBLE);
        District district3 = new District("Quartier 3", 4, DistrictColor.RELIGIEUX);
        bot.buildDistrict(district1);
        bot.buildDistrict(district2);
        bot.buildDistrict(district3);
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
        thirdPlayer.setGameCharacter(bishop);
        firstBuilder.setGameCharacter(king);
        secondPlayer.setGameCharacter(king);
        fourthPlayer.setGameCharacter(king);

        for (int i = 0; i < 8; i++) {
            firstBuilder.getCity().getDistrictsBuilt().add(new District("test", 4, DistrictColor.MARCHAND));
            secondPlayer.getCity().getDistrictsBuilt().add(new District("test", 5, DistrictColor.MARCHAND));
            thirdPlayer.getCity().getDistrictsBuilt().add(new District("test", 2, DistrictColor.MARCHAND));
            fourthPlayer.getCity().getDistrictsBuilt().add(new District("test", 1, DistrictColor.MARCHAND));
        }
        ArrayList<Player> expectedOutput = new ArrayList<>();
        expectedOutput.add(secondPlayer);
        expectedOutput.add(firstBuilder);
        expectedOutput.add(fourthPlayer);
        //third player is missing because he is the bishop, therefore he can't be an option
        assertEquals(expectedOutput, game.getSortedPlayersByScoreForWarlord());
    }
    @Test
    void WarlordGameCanDestroyFirstTest(){
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Game game = new Game();
        game.setPlayers(firstBuilder, secondPlayer);
        firstBuilder.setGold(5);
        firstBuilder.setGameCharacter(warlord);
        secondPlayer.setGameCharacter(king);

        District distToDestroy = new District("test", 5, DistrictColor.MARCHAND);
        secondPlayer.getCity().getDistrictsBuilt().add(distToDestroy);

        assertEquals(1,secondPlayer.getCity().getDistrictsBuilt().size());
        firstBuilder.getGameCharacter().specialEffect(firstBuilder,game,secondPlayer,distToDestroy);
        assertEquals(1,firstBuilder.getGold());
        assertEquals(0,secondPlayer.getCity().getDistrictsBuilt().size());
    }
}
