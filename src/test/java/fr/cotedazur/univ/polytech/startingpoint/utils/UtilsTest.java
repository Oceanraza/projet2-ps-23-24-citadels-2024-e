package fr.cotedazur.univ.polytech.startingpoint.utils;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.BaseAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.RandomAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart.EinsteinAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart.RichardAlgo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {
    @Test
    void getHighestNumberOfCardsInHandTest() {
        Player p1 = new Bot("Picsou");
        Player p2 = new Bot("Donald");
        Player p3 = new Bot("Riri");
        p1.addDistrictInHand(new District("Menuiserie", 5, DistrictColor.TRADE));
        p1.addDistrictInHand(new District("Menuiserie", 5, DistrictColor.TRADE));
        p2.addDistrictInHand(new District("Menuiserie", 5, DistrictColor.TRADE));
        p3.addDistrictInHand(new District("Menuiserie", 5, DistrictColor.TRADE));
        p3.addDistrictInHand(new District("Menuiserie", 5, DistrictColor.TRADE));
        p2.addDistrictInHand(new District("Menuiserie", 5, DistrictColor.TRADE));
        p2.addDistrictInHand(new District("Menuiserie", 5, DistrictColor.TRADE));
        p2.addDistrictInHand(new District("Menuiserie", 5, DistrictColor.TRADE));
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        assertEquals(4, Utils.getHighestNumberOfCardsInHand(players, p1));
    }

    @Test
    void setAlgorithmsTest() {
        ArrayList<BaseAlgo> algorithmsInGame = new ArrayList<>();
        int nbOfEinstein = 2;
        int nbOfRandom = 3;
        int nbOfRichard = 4; // Ajout du nombre de RichardAlgo
        Utils.setAlgorithms(algorithmsInGame, nbOfEinstein, nbOfRichard, nbOfRandom); // Ajout du paramètre nbOfRichard
        assertEquals(nbOfEinstein + nbOfRichard + nbOfRandom, algorithmsInGame.size()); // Ajout de nbOfRichard à la somme
        int einsteinCount = 0;
        int randomCount = 0;
        int richardCount = 0; // Ajout du compteur pour RichardAlgo
        for (BaseAlgo algo : algorithmsInGame) {
            if (algo instanceof EinsteinAlgo) {
                einsteinCount++;
            } else if (algo instanceof RandomAlgo) {
                randomCount++;
            } else if (algo instanceof RichardAlgo) { // Ajout de la condition pour RichardAlgo
                richardCount++;
            }
        }
        assertEquals(nbOfEinstein, einsteinCount);
        assertEquals(nbOfRandom, randomCount);
        assertEquals(nbOfRichard, richardCount); // Vérification du nombre correct de RichardAlgo
    }
}
