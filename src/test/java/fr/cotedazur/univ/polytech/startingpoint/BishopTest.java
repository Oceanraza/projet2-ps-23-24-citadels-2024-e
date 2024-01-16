package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.Condottiere;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.Eveque;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.King;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BishopTest {
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
    void ImmuneToWarlordTest(){
        Player firstBuilder = new Bot("Player 1");
        Player secondPlayer = new Bot("Player 2");
        Game game = new Game();
        game.setPlayers(firstBuilder, secondPlayer);
        firstBuilder.setGold(5);
        firstBuilder.setGameCharacter(condottiere);
        secondPlayer.setGameCharacter(eveque);

        District distToDestroy = new District("test", 5, DistrictColor.marchand);
        secondPlayer.getCity().getDistrictsBuilt().add(distToDestroy);

        assertEquals(1,secondPlayer.getCity().getDistrictsBuilt().size());
        firstBuilder.getGameCharacter().specialEffect(firstBuilder,game,secondPlayer,distToDestroy);
        assertEquals(5,firstBuilder.getGold());
        assertEquals(1,secondPlayer.getCity().getDistrictsBuilt().size());
    }
}
