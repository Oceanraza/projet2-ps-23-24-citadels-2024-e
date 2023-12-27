package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.players.Bot;
import fr.cotedazur.univ.polytech.startingpoint.players.Player;

import java.util.ArrayList;
import java.util.List;

public class Main {
    // If a player has 8 districts built, he wins
    public static boolean isFinished(Player player) {
        return player.getDistrictsBuilt().size() >= 8;
    }

    public static List<Player> calculateScores(List<Player> players, Player firstBuilder) {
        for (Player player : players) {
            int score = player.getGold();
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
            player.setScore(score); // Initialize the player's score
        }
        if (players.get(0).getScore() == players.get(1).getScore()) {
            players.sort((player1, player2) -> player2.getGameCharacter().runningOrder - player1.getGameCharacter().runningOrder); // Sort the players by their character running order
        } else {
            players.sort((player1, player2) -> player2.getScore() - player1.getScore()); // Sort the players by their score
        }
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
        int START_CARDS_NUMBER = 4;

        // Players join the game
        Bot firstBot = new Bot("Donald");
        Bot secondBot = new Bot("Picsou");
        newGame.setPlayers(firstBot, secondBot);

        List<Player> players = newGame.getPlayers();

        for (int i = 0; i < START_CARDS_NUMBER; i++) {
            District firstBotDistrict = newGame.drawCard();
            firstBot.addDistrictInHand(firstBotDistrict);
            newGame.removeDistrictInGameDeck(firstBotDistrict);

            District secondBotDistrict = newGame.drawCard();
            secondBot.addDistrictInHand(secondBotDistrict);
            newGame.removeDistrictInGameDeck(secondBotDistrict);
        }

        int turn = 1;
        Player firstBuilder = null;
        while (!(isFinished(firstBot) || isFinished(secondBot))) {
            newGame.shuffleChars(2);
            // "\033[0;94m" : Shinning blue
            // "\033[0;34m" : Blue
            // "\033[0m" : Reset
            System.out.println("\033[0;94m" + "\n\n----- Tour numero " + turn + " -----" + "\033[0m" + "\nLa couronne appartient à "
                    + (newGame.getCrown().getOwner() != null ? newGame.getCrown().getOwner().getName() : "personne"));

            // Character selection phase
            System.out.println("\033[0;34m" + "\n[ Phase 1 ] Choix des personnages" + "\033[0m");
            System.out.println(firstBot);
            firstBot.chooseCharacterAlgorithm(newGame);
            System.out.println(secondBot);
            secondBot.chooseCharacterAlgorithm(newGame);

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