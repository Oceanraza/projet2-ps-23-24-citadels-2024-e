package fr.cotedazur.univ.polytech.startingpoint;

import com.beust.jcommander.JCommander;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.EinsteinAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.RandomAlgo;
import fr.cotedazur.univ.polytech.startingpoint.utils.Args;
import fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
                        bot.botAlgo.huntedQuarterAlgorithm(district);
                        LOGGER.info(COLOR_BLUE + "\n[ Choix de fin de partie ]" + COLOR_RESET);
                        String useHuntedQuarterMessage = player.getName() + " utilise la Cour des miracles en tant que quartier " + district.getColor() + ".";
                        LOGGER.info(useHuntedQuarterMessage);
                    }
                }
            }
        }

    }

    public static void main(String... args){
        CitadelsLogger.setup();
        CitadelsLogger.setGlobalLogLevel(Level.ALL); // Change level to OFF to disable logs

        Args commandLineArgs = new Args();
        JCommander.newBuilder()
                .addObject(commandLineArgs)
                .build()
                .parse(args);

        // Determining the value of numberOfTurns according to the options
        int numberOfTurns;
        if (commandLineArgs.is2Thousands()) {
            numberOfTurns = 2 * 1000; // 2 x 1000 games
        } else {
            numberOfTurns = 1; // One game
        }

        Game newGame = new Game();
        GameState gameState = new GameState();

        // Adding players to the game
        newGame.setPlayers(
                new Bot("Donald", new EinsteinAlgo()),
                new Bot("Picsou", new EinsteinAlgo()),
                new Bot("Riri", new RandomAlgo()),
                new Bot("Fifi", new RandomAlgo())
        );

        List<Player> players = newGame.getPlayers();

        // Gives the startingCards to all the players.
        newGame.startCardGame();

        Player firstBuilder = null;
        while (!gameState.isGameFinished(players)) {
            gameState.nextTurn();
            Bot crownOwner = (Bot) newGame.getCrown().getOwner();
            String turnNumberMessage = COLOR_BLUE + "\n\n----- Tour " + gameState.getTurn() + " -----" + COLOR_RESET;
            LOGGER.info(turnNumberMessage);
            String crownOwnerMessage = "La couronne appartient a " + (crownOwner != null ? crownOwner.getName() : "personne");
            LOGGER.info(crownOwnerMessage);

            // Reset characters, their states and shuffle cards
            newGame.resetChars();
            newGame.resetCharsState();
            newGame.shuffleCharacters();

            // Character selection phase
            LOGGER.info("\n" + COLOR_BLUE + "[ Phase 1 ] Choix des personnages" + COLOR_RESET);

            if (crownOwner != null){
                String crownOwnerInfos = crownOwner.toString();
                LOGGER.info(crownOwnerInfos);
                crownOwner.botAlgo.chooseCharacterAlgorithm(newGame);
            }
            newGame.charSelectionFiller();

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
                    String isKilledMessage = "\n" + cha.getRole().toStringLeOrLUpperCase() + " a ete tue par " + cha.getAttacker().getName();
                    String cannotPlayMessage = player.getName() + " ne pourra pas jouer ce tour !";
                    LOGGER.info(isKilledMessage);
                    LOGGER.info(cannotPlayMessage);
                    // If the king is killed, he gets the crown at the end of this turn
                    if (cha.getRole() == GameCharacterRole.KING) {
                        newGame.getCrown().setOwner(player);
                        LOGGER.info("Il recuperera la couronne a la fin de ce tour");
                    }
                }
            }
        }
        finalChoice(players, gameState);
        LOGGER.info("\n" + COLOR_BLUE + "[ Decompte des points ]" + COLOR_RESET);
        announceWinner(players, firstBuilder, gameState);
    }
}