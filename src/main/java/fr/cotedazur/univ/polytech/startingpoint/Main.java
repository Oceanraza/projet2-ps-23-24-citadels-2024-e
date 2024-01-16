package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.EinsteinAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.RandomAlgo;

import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;


import java.util.List;
import java.util.Comparator;
public class Main {
    private static final int START_CARDS_NUMBER = 4;

    // If a player has 8 districts built, he wins
    public static boolean isGameFinished(List<Player> players) {
        for (Player p: players){
            if (isFinished(p)){return true;}
        }
        return false;
    }

    public static boolean isFinished(Player player) {
        return (player.getCity().getDistrictsBuilt().size() >= 8);
    }
    public static void sortPlayers(List<Player> players) {
        // Use a custom Comparator to compare Players based on their score and running order
        Comparator<Player> playerComparator = Comparator
                .comparingInt(Player::getScore)
                .thenComparingInt(player -> player.getGameCharacter().getRunningOrder())
                .reversed();

        // Sort the players list using the custom comparator
        players.sort(playerComparator);
    }
    public static List<Player> calculateScores(List<Player> players, Player firstBuilder) {
        for (Player player : players) {
            player.calculateScore();
            if (player == firstBuilder) { // If the player was the first to build his 8 districts
                player.setScore(player.getScore()+4);
            } else if (isFinished(player)) { // If the others players have finished building his 8 districts too
                player.setScore(player.getScore()+2);
            }
        }
        sortPlayers(players);
        return players;
    }

    public static void announceWinner(List<Player> players, Player firstBuilder) {
        List<Player> playersScores = calculateScores(players, firstBuilder);
        for (Player player : playersScores) {
            System.out.println(player.getName() + " : " + player.getScore() + " points");
        }
        Player winner = playersScores.get(0);
        System.out.println(winner.getName() + " gagne la partie avec " + winner.getScore() + " points !");
    }

    public static void main(String... args) {
        Game newGame = new Game();
        // System.out.println(newGame);

        // Adding players to the game
        newGame.setPlayers(new Bot("Donald",new EinsteinAlgo()), new Bot("Picsou",new RandomAlgo()), new Bot("Riri",new RandomAlgo()), new Bot("Fifi",new RandomAlgo()));


        List<Player> players = newGame.getPlayers();

        //Gives the startingCards to all the players.
        for (Player p : players){
            for (int i = 0; i < START_CARDS_NUMBER; i++) {
                p.addDistrictInHand(newGame.drawCard());
            }
        }

        int turn = 1;
        Player firstBuilder = null;
        while (!isGameFinished(players)) {
            newGame.setAllCharsToNull();
            newGame.shuffleChars(4);
            Bot crownOwner = (Bot) newGame.getCrown().getOwner();
            // "\033[0;94m" : Shinning blue
            // "\033[0;34m" : Blue
            // "\033[0m" : Reset
            System.out.println("\033[0;94m" + "\n\n----- Tour numero " + turn + " -----" + "\033[0m" + "\nLa couronne appartient à "
                    + (crownOwner != null ? crownOwner.getName() : "personne"));

            // Character selection phase
            System.out.println("\033[0;34m" + "\n[ Phase 1 ] Choix des personnages" + "\033[0m");

            if (crownOwner != null){
                System.out.println(crownOwner);
                crownOwner.botAlgo.chooseCharacterAlgorithm(newGame);
            }
            newGame.charSelectionFiller();

            // Character reveal phase
            System.out.println("\033[0;34m" + "\n[ Phase 2 ] Tour des joueurs" + "\033[0m");
            List<Player> runningOrder = newGame.setRunningOrder();

            for (Player player: runningOrder) {
                System.out.println(player);
                player.play(newGame);
                if (isFinished(player)) {
                    firstBuilder = player;
                }
            }

            turn++;
        }
        announceWinner(players, firstBuilder);
    }
}