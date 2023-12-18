package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static final Random random = new Random();
    public static final int START_CARDS_NUMBER = 4;

    // If a player has 8 districts built, he wins
    public static boolean isFinished(Player player) {
        return player.getDistrictsBuilt().size() >= 8;
    }

    public static List<Player> calculateScores(List<Player> players, Player firstBuilder) {
        for (Player player : players) {
            int score = player.gold;
            ArrayList<DistrictColor> districtColors = new ArrayList<>();
            for (District district : player.getDistrictsBuilt()) {
                score += district.getPrice();
                districtColors.add(district.getColor());
            }
            if (districtColors.size() == DistrictColor.values().length) { // If the player has built all the district
                                                                          // colors
                score += 3;
            }
            if (player == firstBuilder) { // If the player was the first to build his 8 districts
                score += 4;
            } else if (isFinished(player)) { // If the others players have finished building his 8 districts too
                score += 2;
            }
            player.score = score; // Initialize the player's score
        }
        players.sort((player1, player2) -> player2.score - player1.score); // Sort the players by their score
        return players;
    }

    public static void announceWinner(List<Player> players, Player firstBuilder) {
        List<Player> playersScores = calculateScores(players, firstBuilder);
        for (Player player : playersScores) {
            System.out.println(player.getName() + " : " + player.score + " points");
        }
        Player winner = playersScores.get(0);
        System.out.println(winner.getName() + " gagne la partie avec " + winner.score + " points !");
    }

    public static void main(String... args) {
        Game newGame = new Game();
        // System.out.println(newGame);

        // Players join the game
        Bot firstBot = new Bot("Donald");
        Bot secondBot = new Bot("Picsou");
        newGame.setPlayers(firstBot, secondBot);

        List<Player> players = newGame.getPlayers();

        for (int i = 0; i < START_CARDS_NUMBER; i++) {
            District firstBotDistrict = newGame.drawCard();
            firstBot.districtsInHand.add(firstBotDistrict);
            newGame.gameDeck.remove(firstBotDistrict);

            District secondBotDistrict = newGame.drawCard();
            secondBot.districtsInHand.add(secondBotDistrict);
            newGame.gameDeck.remove(secondBotDistrict);
        }

        int turn = 1;
        Player firstBuilder = null;
        while (!(isFinished(firstBot) || isFinished(secondBot))) {
            newGame.shuffleChars(2);
            System.out.println("\nTour numero " + turn + "\nLa couronne appartient à "
                    + (newGame.getCrown().getOwner() != null ? newGame.getCrown().getOwner().name : "personne"));

            // Character selection phase
            System.out.println("Choix des personnages.");
            System.out.println(firstBot);
            firstBot.chooseCharacterAlgorithm(newGame);
            System.out.println(secondBot);
            secondBot.chooseCharacterAlgorithm(newGame);

            // Character reveal phase
            System.out.println("Jouez !");
            List<Player> runningOrder = newGame.setRunningOrder(players);

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