package fr.cotedazur.univ.polytech.startingpoint;

import com.beust.jcommander.JCommander;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.exception.CSVFileProcessingException;
import fr.cotedazur.univ.polytech.startingpoint.exception.CSVWriteException;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.BaseAlgo;
import fr.cotedazur.univ.polytech.startingpoint.utils.Args;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;
import fr.cotedazur.univ.polytech.startingpoint.utils.Csv;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.*;
import java.util.logging.Level;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

public class Main {
    private static boolean enableCsv = false;
    private static Args.ArgsEnum currentMode;

    /**
     * Cette méthode retourne la position du joueur souhaité dans la liste des joueurs.
     *
     * @param players      La liste des joueurs.
     * @param wantedPlayer Le joueur dont on veut connaître la position.
     * @return La position du joueur souhaité dans la liste des joueurs, ou -1 si le joueur n'est pas trouvé.
     */
    public static int getPlacement(List<Player> players, Player wantedPlayer) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(wantedPlayer)) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Cette méthode trie les joueurs en fonction de leur score et de leur ordre de passage.
     * Elle utilise un comparateur personnalisé pour comparer les joueurs en fonction de leur score et de leur ordre de passage.
     * Les joueurs ayant des scores plus élevés sont placés en premier. Si deux joueurs ont le même score,
     * le joueur ayant le plus petit ordre de passage est placé en premier.
     *
     * @param players La liste des joueurs à trier.
     */
    public static void sortPlayers(List<Player> players) {
        // Use a custom Comparator to compare Players based on their score and running order
        Comparator<Player> playerComparator = Comparator
                .comparingInt(Player::getScore)
                .thenComparingInt(player -> player.getGameCharacter().getRunningOrder())
                .reversed();

        // Sort the players list using the custom comparator
        players.sort(playerComparator);
    }

    /**
     * Cette méthode calcule les scores pour chaque joueur et trie la liste des joueurs en fonction de leurs scores.
     * Si un joueur a été le premier à construire ses 8 quartiers, il reçoit un bonus de 4 points.
     * Si les autres joueurs ont également terminé la construction de leurs 8 quartiers, ils reçoivent un bonus de 2 points.
     *
     * @param players La liste des joueurs pour lesquels calculer les scores.
     * @param firstBuilder Le joueur qui a été le premier à construire ses 8 quartiers.
     * @param gameState L'état actuel du jeu.
     * @return La liste des joueurs triée en fonction de leurs scores.
     */

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

    /**
     * Cette méthode permet aux joueurs de faire un choix final à la fin du jeu.
     * Si un joueur a construit le quartier "Cour des miracles" et que le tour actuel est supérieur au tour de construction de ce quartier,
     * le joueur peut utiliser l'algorithme "huntedQuarterAlgorithm" pour choisir un quartier à utiliser comme quartier de couleur différente.
     *
     * @param players La liste des joueurs qui vont faire leur choix final.
     * @param gameState L'état actuel du jeu.
     */

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

    /**
     * Cette méthode annonce le gagnant de la partie et affiche les scores de chaque joueur.
     *
     * @param players La liste des joueurs.
     * @param firstBuilder Le joueur qui a été le premier à construire ses 8 quartiers.
     * @param gameState L'état actuel du jeu.
     */

    public static void announceWinner(List<Player> players, Player firstBuilder, GameState gameState) {
        List<Player> playersScores = calculateScores(players, firstBuilder, gameState);
        String playerScoreMessage;
        for (Player player : playersScores) {
            playerScoreMessage = player.getName() + " : " + player.getScore() + " points";
            LOGGER.info(playerScoreMessage);
        }
        Player winner = playersScores.get(0);
        String winnerMessage = "\n" + winner.getName() + " gagne la partie avec " + winner.getScore() + " points !";
        LOGGER.info(winnerMessage);
    }


    /**
     * Cette méthode permet de choisir le nombre de parties à jouer en fonction des options passées en paramètre.
     *
     * @param args Les options passées en paramètre.
     * @return Le nombre de parties à jouer.
     */

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
            CitadelsLogger.setupCsvOr2Thousand();
            numberOfGames = 1000;
            CitadelsLogger.setGlobalLogLevel(CSV_OR_THOUSAND);
        }
        // One game
        else if (currentMode.equals(Args.ArgsEnum.DEMO)) {
            CitadelsLogger.setupDemo();
            CitadelsLogger.setGlobalLogLevel(Level.INFO);
        }
        // CSV
        else if (currentMode.equals(Args.ArgsEnum.CSV)){
            CitadelsLogger.setupCsvOr2Thousand();
            numberOfGames = 20;
            CitadelsLogger.setGlobalLogLevel(CSV_OR_THOUSAND);
            enableCsv = true;
        }
        return numberOfGames;
    }

    /**
     * Cette méthode réinitialise le jeu et l'état du jeu.
     *
     * @param game      Le jeu à réinitialiser.
     * @param gameState L'état du jeu à réinitialiser.
     */

    public static void resetAll(Game game, GameState gameState) {
        game.resetGame();
        gameState.resetGameState();
    }

    /**
     * Cette méthode retourne les informations d'un joueur sous forme de liste de chaînes de caractères.
     *
     * @param totalPlacements La liste des placements de chaque joueur.
     * @param wantedPlayer    Le joueur dont on veut connaître les informations.
     * @return Les informations du joueur sous forme de liste de chaînes de caractères.
     */

    public static List<String> getPlayerInfo(Map<String, List<Integer>> totalPlacements, Player wantedPlayer) {
        List<String> res = new ArrayList<>();
        for (Integer temp : totalPlacements.get(wantedPlayer.getName())){
            res.add(temp.toString());
        }
        return res;
    }

    /**
     * Cette méthode est la méthode principale du programme.
     * Elle permet de jouer un certain nombre de parties en fonction des options passées en paramètre.
     *
     * @param args Les options passées en paramètre.
     */
    public static void main(String... args) throws CSVWriteException, CSVFileProcessingException {
        Map<String, Integer> totalScores = new HashMap<>();
        Map<String, List<Integer>> totalPlacements = new HashMap<>(); //List of 4 placements
        Map<String, Integer> algoWinrate = new HashMap<>();
        String Donald = "Donald";
        String Picsou = "Picsou";
        String Riri = "Riri";
        String Fifi = "Fifi";
        String[] names = {Donald, Picsou, Riri, Fifi};
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
                    LOGGER.log(CSV_OR_THOUSAND,  COLOR_BLUE + "\n[ Algo le plus intelligent contre le second (2vs2) ]\n" + COLOR_RESET);
                }
                nbOfEinstein = 2;
                nbOfRandom = 2;
            }
            else{
                for (Map.Entry<String,Integer> entry : algoWinrate.entrySet()) {
                    String key = entry.getKey();
                    String winPercentage = COLOR_PURPLE + key + " gagne " + ((double)algoWinrate.get(key))/10 + "% de fois." + COLOR_RESET;
                    LOGGER.log(CSV_OR_THOUSAND, winPercentage);
                }
                if (currentMode.equals(Args.ArgsEnum.TWOTHOUSANDS)){
                    LOGGER.log(CSV_OR_THOUSAND, COLOR_BLUE + "\n[ Algo le plus intelligent contre lui meme (1vs1vs1vs1) ]\n" + COLOR_RESET);
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
                        new Bot(Donald, algorithmsInGame.get(0)),
                        new Bot(Picsou, algorithmsInGame.get(1)),
                        new Bot(Riri, algorithmsInGame.get(2)),
                        new Bot(Fifi, algorithmsInGame.get(3))
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

                    newGame.characterSelection(crownOwner,getPlacement(players,crownOwner) - 1);

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
                announceWinner(players, firstBuilder, gameState);
                for (Player p : players) {
                    int placementScore = getPlacement(players, p) == 1 ? 1 : 0;
                    totalScores.compute(p.getName(), (k, v) -> (v == null) ? p.getScore() : v + p.getScore());
                    algoWinrate.compute(((Bot) p).getBotAlgo().getAlgoName(), (k, v) -> (v == null) ? 0 : v + placementScore);
                    totalPlacements.get(p.getName()).set(getPlacement(players, p) - 1, totalPlacements.get(p.getName()).get(getPlacement(players, p) - 1) + 1); //Adds one to the pos
                }
            }
            List<String[]> finalArgs = new ArrayList<>();
            for (Player p : newGame.getPlayers()) {
                if (enableCsv) {
                    List<String> specificPlayerPlacement = getPlayerInfo(totalPlacements, p);
                    finalArgs.add(new String[]{p.getName(), ((Bot) p).getBotAlgo().getAlgoName(), ((Integer) (totalScores.get(p.getName()) / numberOfGames)).toString(), ((Integer) numberOfGames).toString(), specificPlayerPlacement.get(0), specificPlayerPlacement.get(1), specificPlayerPlacement.get(2), specificPlayerPlacement.get(3)});
                } else if (currentMode.equals(Args.ArgsEnum.TWOTHOUSANDS)) {
                    Csv.printPlayerInfo(totalScores, totalPlacements, p, numberOfGames);
                }
            }
            if (enableCsv) {
                Csv.writeStats(finalArgs);
            }
        }
        LOGGER.log(CSV_OR_THOUSAND, "\n");
    }
}