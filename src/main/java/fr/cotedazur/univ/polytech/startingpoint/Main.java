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

    public static Player isWinner(List<Player> players, Player firstBuilder) {
        int maxScore = 0;
        Player winner = players.get(0);
        for(Player player : players) {
            int score = player.gold;
            ArrayList<DistrictColor> districtColors = new ArrayList<>();
            for (District district : player.getDistrictsBuilt()) {
                score += district.getPrice();
                districtColors.add(district.getColor());
            }
            if (districtColors.size() == DistrictColor.values().length) {
                score += 3;
            }
            if (player == firstBuilder) {
                score += 4;
            } else if (isFinished(player)) {
                score += 2;
            }
            if (score > maxScore) {
                maxScore = score;
                winner = player;
            }
        }
        return winner;
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
            System.out.println("Tour numéro " + turn);
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
        System.out.println(isWinner(players, firstBuilder).getName() + " gagne la partie !");
    }
}
