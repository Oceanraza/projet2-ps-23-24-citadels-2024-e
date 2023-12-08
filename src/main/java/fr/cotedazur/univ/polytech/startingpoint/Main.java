package fr.cotedazur.univ.polytech.startingpoint;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static final Random random = new Random();
    public static final int START_CARDS_NUMBER = 4;

    // If a player has 8 districts built, he wins
    public static boolean isFinished(Player player) {
        return player.getDistrictsBuilt().size() == 8;
    }

    public static List<Player> calculateScores(List<Player> players, Player firstBuilder) {
        for(Player player : players) {
            int score = player.gold;
            ArrayList<DistrictColor> districtColors = new ArrayList<>();
            for (District district : player.getDistrictsBuilt()) {
                score += district.getPrice();
                districtColors.add(district.getColor());
            }
            if (districtColors.size() == DistrictColor.values().length) { // If the player has built all the district colors
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
        System.out.println(winner.getName() + " gagne la partie avec "+ winner.score + " points !");
    }
    public static void main(String... args) {
        Game newGame = new Game();
        System.out.println(newGame);

        Bot firstBot = new Bot("Bot 1");
        Bot secondBot = new Bot("Bot 2");
        List<Player> players = new ArrayList<>();
        players.add(firstBot);
        players.add(secondBot);

        for(int i = 0; i < START_CARDS_NUMBER; i++) {
            District firstBotDistrict = newGame.drawCard();
            firstBot.districtsInHand.add(firstBotDistrict);
            newGame.gameDeck.remove(firstBotDistrict);

            District secondBotDistrict = newGame.drawCard();
            secondBot.districtsInHand.add(secondBotDistrict);
            newGame.gameDeck.remove(secondBotDistrict);
        }
        int turn = 1;
        Player firstBuilder = null;
        while(!(isFinished(firstBot) || isFinished(secondBot))) {
            System.out.println("Tour numÃ©ro " + turn);
            System.out.println("Bot 1:");
            firstBot.play(newGame);
            if(isFinished(firstBot)) {
                firstBuilder = firstBot;
            }
            System.out.println("Bot 2:");
            secondBot.play(newGame);
            if(isFinished(secondBot) && !isFinished(firstBot)) {
                firstBuilder = secondBot;
            }
            turn++;
        }
        announceWinner(players, firstBuilder);
    }
}