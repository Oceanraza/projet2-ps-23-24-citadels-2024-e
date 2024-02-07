package fr.cotedazur.univ.polytech.startingpoint;

import com.beust.jcommander.JCommander;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.RandomAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart.EinsteinAlgo;
import fr.cotedazur.univ.polytech.startingpoint.utils.Args;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;

import java.util.*;
import java.util.logging.Level;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

public class Main {

    public static void sortPlayers(List<Player> players) {
        // Use a custom Comparator to compare Players based on their score and running order
        Comparator<Player> playerComparator = Comparator
                .comparingInt(Player::getScore)
                .thenComparingInt(player -> player.getGameCharacter().getRunningOrder())
                .reversed();

        // Sort the players list using the custom comparator
        players.sort(playerComparator);
    }

    public static List<Player> calculateScores(List<Player> players, Player firstBuilder, GameState gameState) {
        for (Player player : players) {
            player.calculateAndSetScore();
            if (player == firstBuilder) { // If the player was the first to build his 8 districts
                player.setScore(player.getScore() + 4);
            } else if (gameState.isFinished(player)) { // If the others players have finished building his 8 districts too
                player.setScore(player.getScore() + 2);
            }
        }
        sortPlayers(players);
        return players;
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

    public static void finalChoice(List<Player> players, GameState gameState) {
        for (Player player : players) {
            for (District district : player.getCity().getDistrictsBuilt()) {
                if (district.getName().equals("Cour des miracles") && district.getTurnBuilt().isPresent()) {
                    Optional<Integer> turnBuilt = district.getTurnBuilt();
                    if (turnBuilt.isPresent() && gameState.getTurn() > turnBuilt.get()) {
                        Bot bot = (Bot) player;
                        bot.getBotAlgo().huntedQuarterAlgorithm(district);
                        LOGGER.info(COLOR_BLUE + "\n[ Choix de fin de partie ]" + COLOR_RESET);
                        String useHuntedQuarterMessage = COLOR_PURPLE + player.getName() + " utilise la Cour des miracles en tant que quartier " + district.getColor() + "." + COLOR_RESET;
                        LOGGER.info(useHuntedQuarterMessage);
                    }
                }
            }
        }

    }

    public static int jCommander(String... args) {
        Args commandLineArgs = new Args();
        JCommander.newBuilder()
                .addObject(commandLineArgs)
                .build()
                .parse(args);

        // Determining the value of numberOfTurns according to the options
        int numberOfGames;
        // 2 x 1000 games
        if (commandLineArgs.is2Thousands()) {
            numberOfGames = 1000;
            CitadelsLogger.setGlobalLogLevel(Level.OFF);
        }
        // One game
        else {
            numberOfGames = 1;
            CitadelsLogger.setGlobalLogLevel(Level.ALL);
        }
        return numberOfGames;
    }
    public static void resetAll(Game game, GameState gameState){
         game.resetGame();
         gameState.resetGameState();
    }
    public static int getPlacement(List<Player> players, Player wantedPlayer){
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).equals(wantedPlayer)){
                return i + 1;
            }
        }
        return -1;
    }
    public static void printPlayerInfo( Map<String, Integer> totalScores, Map<String, List<Integer>> totalPlacements,Player wantedPlayer, int numberOfGames) {
        int place = 1;
        for (Integer temp : totalPlacements.get(wantedPlayer.getName())) {
            System.out.println(wantedPlayer.getName() + " a décroché la " + place + "e place " + temp / 10 + "% de fois en moyenne");
            place++;
        }
        System.out.println("et a également eu " + totalScores.get(wantedPlayer.getName())/1000 + " de score en moyenne.");
    }
    public static void main(String... args) {
        Map<String, Integer> totalScores = new HashMap<>();
        Map<String, List<Integer>> totalPlacements = new HashMap<>(); //List of 4 placements
        String name1 = "Donald";
        String name2 = "Picsou";
        String name3 = "Riri";
        String name4 = "Fifi";
        String[] names = {name1,name2,name3,name4};
        List<Integer> initialPlacement = Arrays.asList(0, 0, 0, 0);

        // Add the initial list to each key in the map
        for (String key : names) {
            totalPlacements.put(key, new ArrayList<>(initialPlacement));
        }
        CitadelsLogger.setup();

        Game newGame = new Game();
        GameState gameState = new GameState();
        int numberOfGames = jCommander(args);
        for (int games = 0 ; games < numberOfGames; games++){
            resetAll(newGame,gameState);
            // Adding players to the game
            newGame.setPlayers(
                    new Bot(name1, new EinsteinAlgo()),
                    new Bot(name2, new EinsteinAlgo()),
                    new Bot(name3, new RandomAlgo()),
                    new Bot(name4, new RandomAlgo())
            );

            List<Player> players = newGame.getPlayers();

            // Gives the startingCards to all the players.
            newGame.startCardGame();

            Player firstBuilder = null;
            while (!gameState.isGameFinished(players)) {
                gameState.nextTurn();

                String turnNumberMessage = COLOR_BLUE + "\n\n----- Tour " + gameState.getTurn() + " -----" + COLOR_RESET;
                LOGGER.info(turnNumberMessage);

                Bot crownOwner = newGame.getCrownOwner();

                // Reset characters, their states and shuffle cards
                newGame.resetChars();
                newGame.resetCharsState();
                newGame.shuffleCharacters();

                // Character selection phase
                LOGGER.info("\n" + COLOR_BLUE + "[ Phase 1 ] Choix des personnages" + COLOR_RESET);

                newGame.characterSelection(crownOwner);

                // Character reveal phase
                LOGGER.info("\n" + COLOR_BLUE + "[ Phase 2 ] Tour des joueurs" + COLOR_RESET);
                List<Player> runningOrder = newGame.setRunningOrder();

                for (Player player: runningOrder) {
                    GameCharacter cha = player.getGameCharacter();
                    // If the character is alive
                    if (cha.getIsAlive()) {
                        String playerInfos = player.toString();
                        LOGGER.info(playerInfos);
                        player.play(newGame, gameState);
                        if (gameState.isFinished(player)) {
                            firstBuilder = player;
                        }
                    }
                    // If the player has been killed, he cannot play
                    else {
                        newGame.playerKilled(cha, player);
                    }
                }
            }
            finalChoice(players, gameState);
            LOGGER.info("\n" + COLOR_BLUE + "[ Decompte des points ]" + COLOR_RESET);
            announceWinner(players, firstBuilder, gameState);
            for (Player p: players){
                totalScores.compute(p.getName(), (k, v) -> (v == null) ? p.getScore() : v + p.getScore());
                //totalPlacements.
                totalPlacements.get(p.getName()).set(getPlacement(players, p) - 1, totalPlacements.get(p.getName()).get(getPlacement(players, p) - 1) + 1); //Adds one to the pos
            }
        }
            System.out.println(totalScores);
            System.out.println(totalPlacements);
            for (Player p : newGame.getPlayers()){
                printPlayerInfo(totalScores,totalPlacements,p,numberOfGames);
            }
        }
    }