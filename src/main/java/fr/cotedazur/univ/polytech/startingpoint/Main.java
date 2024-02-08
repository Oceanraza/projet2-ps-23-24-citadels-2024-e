package fr.cotedazur.univ.polytech.startingpoint;

import com.beust.jcommander.JCommander;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.BaseAlgo;
import fr.cotedazur.univ.polytech.startingpoint.utils.*;
import java.util.*;
import java.util.logging.Level;
import static fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger.*;

public class Main {
    private static boolean enableCsv = false;
    private static Args.ArgsEnum currentMode;
    public static int getPlacement(List<Player> players, Player wantedPlayer){
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).equals(wantedPlayer)){
                return i + 1;
            }
        }
        return -1;
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
        int numberOfGames = 1;
        currentMode = commandLineArgs.getCurrentMode();
        // 2 x 1000 games
        if (currentMode.equals(Args.ArgsEnum.TWOTHOUSANDS)) {
            numberOfGames = 1000;
            InGameLogger.setGlobalLogLevel(Level.OFF);

        }
        // One game
        else if (currentMode.equals(Args.ArgsEnum.DEMO)) {
            InGameLogger.setup();
            InGameLogger.setGlobalLogLevel(Level.ALL);
        }
        else if (currentMode.equals(Args.ArgsEnum.CSV)){
            numberOfGames = 20;
            InGameLogger.setGlobalLogLevel(Level.OFF);
            enableCsv = true;
        }
        return numberOfGames;
    }
    public static void resetAll(Game game, GameState gameState){
         game.resetGame();
         gameState.resetGameState();
    }

    public static List<String> getPlayerInfo(Map<String, List<Integer>> totalPlacements, Player wantedPlayer){
        List<String> res = new ArrayList<>();
        for (Integer temp : totalPlacements.get(wantedPlayer.getName())){
            res.add(temp.toString());
        }
        return res;
    }
    public static void main(String... args) {
        Map<String, Integer> totalScores = new HashMap<>();
        Map<String, List<Integer>> totalPlacements = new HashMap<>(); //List of 4 placements
        Map<String, Integer> algoWinrate = new HashMap<>();
        String name1 = "Donald";
        String name2 = "Picsou";
        String name3 = "Riri";
        String name4 = "Fifi";
        String[] names = {name1, name2, name3, name4};
        List<Integer> initialPlacement = Arrays.asList(0, 0, 0, 0);

        // Add the initial list to each key in the map
        for (String key : names) {
            totalPlacements.put(key, new ArrayList<>(initialPlacement));
        }
        Game newGame = new Game();
        GameState gameState = new GameState();
        int numberOfGames = jCommander(args);

        int nbOfEinstein;
        int nbOfRandom;
        for (int numberOfRepetitions = 0; numberOfRepetitions < (currentMode.equals(Args.ArgsEnum.TWOTHOUSANDS) ? 2 : 1); numberOfRepetitions++) {
            if (numberOfRepetitions == 0){
                if (currentMode.equals(Args.ArgsEnum.TWOTHOUSANDS)){
                    System.out.println("\nAlgo le plus intelligent contre le second (2vs2)\n");
                }
                nbOfEinstein = 2;
                nbOfRandom = 2;
            }
            else{
                for (String key : algoWinrate.keySet()){
                    System.out.println('\n'+key + " gagne " + ((double)algoWinrate.get(key))/10 + "% de fois.");
                }
                if (currentMode.equals(Args.ArgsEnum.TWOTHOUSANDS)){
                    System.out.println("\nAlgo le plus intelligent contre lui même (1vs1vs1vs1)\n");
                }
                nbOfEinstein = 4;
                nbOfRandom = 0;
            }
            Utils.resetScoresAndPlacements(totalPlacements,totalScores);
            ArrayList<BaseAlgo> algorithmsInGame = new ArrayList<>();
            Utils.setAlgorithms(algorithmsInGame, nbOfEinstein, nbOfRandom);
            for (int games = 0; games < numberOfGames; games++) {
                resetAll(newGame, gameState);
                // Adding players to the game
                newGame.setPlayers(
                        new Bot(name1, algorithmsInGame.get(0)),
                        new Bot(name2, algorithmsInGame.get(1)),
                        new Bot(name3, algorithmsInGame.get(2)),
                        new Bot(name4, algorithmsInGame.get(3))
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

                    for (Player player : runningOrder) {
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
                ConsoleLogFunctions.announceWinner(players, firstBuilder, gameState);
                for (Player p : players) {
                    totalScores.compute(p.getName(), (k, v) -> (v == null) ? p.getScore() : v + p.getScore());
                    algoWinrate.compute(((Bot)p).getBotAlgo().getAlgoName(), (k, v) -> (v == null) ? 0 : v + ((getPlacement(players,p)==1)?1:0));
                    totalPlacements.get(p.getName()).set(getPlacement(players, p) - 1, totalPlacements.get(p.getName()).get(getPlacement(players, p) - 1) + 1); //Adds one to the pos
                }
            }
            List<String[]> finalArgs = new ArrayList<>();
            for (Player p : newGame.getPlayers()) {
                if (enableCsv) {
                    List<String> specificPlayerPlacement = getPlayerInfo(totalPlacements, p);
                    finalArgs.add(new String[]{p.getName(), ((Bot) p).getBotAlgo().getAlgoName(), ((Integer) (totalScores.get(p.getName()) / numberOfGames)).toString(), ((Integer) numberOfGames).toString(), specificPlayerPlacement.get(0), specificPlayerPlacement.get(1), specificPlayerPlacement.get(2), specificPlayerPlacement.get(3)});
                } else if (currentMode.equals(Args.ArgsEnum.TWOTHOUSANDS)) {
                    ConsoleLogFunctions.printPlayerInfo(totalScores, totalPlacements, p, numberOfGames);
                }
            }
            if (enableCsv) {
                Csv.writeStats(finalArgs);
            }
        }
    }
}
