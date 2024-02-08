package fr.cotedazur.univ.polytech.startingpoint.utils;

import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.List;
import java.util.Map;

import static fr.cotedazur.univ.polytech.startingpoint.Main.calculateScores;
import static fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger.LOGGER;

public class ConsoleLogFunctions {
    private ConsoleLogFunctions() {
        throw new IllegalStateException("Csv is a utility class");
    }

    public static void announceWinner(List<Player> players, Player firstBuilder, GameState gameState) {
        List<Player> playersScores = calculateScores(players, firstBuilder, gameState);
        String playerScoreMessage;
        for (Player player : playersScores) {
            playerScoreMessage = player.getName() + " : " + player.getScore() + " points";
            LOGGER.info(playerScoreMessage);
        }
        Player winner = playersScores.get(0);
        String winnerMessage = "\n" + winner.getName() + " gagne la partie avec " + winner.getScore() + " points !\n";
        LOGGER.info(winnerMessage);
    }

    public static void printPlayerInfo(Map<String, Integer> totalScores, Map<String, List<Integer>> totalPlacements, Player wantedPlayer, int numberOfGames) {
        double scoreJoueur = totalPlacements.get(wantedPlayer.getName()).get(0);
        LOGGER.warning(wantedPlayer.getName() + " a gagné un total de " + scoreJoueur + " parties\nIl gagne donc " + (scoreJoueur / (numberOfGames / 100.0)) + "% du temps.");
        LOGGER.warning("Il a en moyenne " + totalScores.get(wantedPlayer.getName()) / numberOfGames + " points");
    }

}
