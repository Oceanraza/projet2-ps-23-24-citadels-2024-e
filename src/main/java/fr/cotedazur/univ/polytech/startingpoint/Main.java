package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.EinsteinAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.RandomAlgo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.CitadelsLogger.LOGGER;

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
        LOGGER.info("");
        for (Player player : playersScores) {
            LOGGER.info(player.getName() + " : " + player.getScore() + " points");
        }
        Player winner = playersScores.get(0);
        LOGGER.info("\n" + winner.getName() + " gagne la partie avec " + winner.getScore() + " points !\n");
    }

    public static void finalChoice(List<Player> players, GameState gameState) {
        for(Player player: players) {
            for(District district: player.getCity().getDistrictsBuilt()) {
                if(district.getName().equals("Cour des miracles") && district.getTurnBuilt().isPresent()) {
                    Optional<Integer> turnBuilt = district.getTurnBuilt();
                    if(turnBuilt.isPresent() && gameState.getTurn() > turnBuilt.get()) {
                        Bot bot = (Bot) player;
                        bot.botAlgo.huntedQuarterAlgorithm(district);
                        LOGGER.info("\n[ Choix de fin de partie ]");
                        LOGGER.info(player.getName() + " utilise la Cour des miracles en tant que quartier " + district.getColor() + ".");
                    }
                }
            }
        }

    }

    public static void main(String... args){
        CitadelsLogger.setup();
        //CitadelsLogger.setGlobalLogLevel(Level.OFF); // Uncomment this line to disable logs

        Game newGame = new Game();
        GameState gameState = new GameState();

        // Adding players to the game
        newGame.setPlayers(new Bot("Donald", new EinsteinAlgo()), new Bot("Picsou", new EinsteinAlgo()), new Bot("Riri", new RandomAlgo()), new Bot("Fifi", new RandomAlgo()));

        List<Player> players = newGame.getPlayers();

        // Gives the startingCards to all the players.
        newGame.startCardGame();

        Player firstBuilder = null;
        while (!gameState.isGameFinished(players)) {
            gameState.nextTurn();
            Bot crownOwner = (Bot) newGame.getCrown().getOwner();
            // "\033[0;94m" : Shinning blue
            // "\033[0;34m" : Blue
            // "\033[0m" : Reset
            LOGGER.info("\n\n----- Tour " + gameState.getTurn() + " -----" +
                    "\nLa couronne n'appartient a " + (crownOwner != null ? crownOwner.getName() : "personne"));

            // Reset characters, their states and shuffle cards
            newGame.resetChars();
            newGame.resetCharsState();
            newGame.shuffleCharacters();

            // Character selection phase
            LOGGER.info("\n[ Phase 1 ] Choix des personnages");

            if (crownOwner != null){
                LOGGER.info(crownOwner.toString());
                crownOwner.botAlgo.chooseCharacterAlgorithm(newGame);
            }
            newGame.charSelectionFiller();

            // Character reveal phase
            LOGGER.info("\n[ Phase 2 ] Tour des joueurs");
            List<Player> runningOrder = newGame.setRunningOrder();

            for (Player player: runningOrder) {
                GameCharacter cha = player.getGameCharacter();
                // If the character is alive
                if (cha.getIsAlive()) {
                    LOGGER.info(player.toString());
                    player.play(newGame, gameState);
                    if (gameState.isFinished(player)) {
                        firstBuilder = player;
                    }
                }
                // If the player has been killed, he cannot play
                else {
                    LOGGER.info("\n" + cha.getRole().toStringLeOrL() + " a ete tue par " + cha.getAttacker().getName());
                    LOGGER.info(player.getName() + " ne pourra pas jouer ce tour !");
                    // If the king is killed, he gets the crown at the end of this turn
                    if (cha.getRole() == GameCharacterRole.KING) {
                        newGame.getCrown().setOwner(player);
                        LOGGER.info("Il recuperera la couronne a la fin de ce tour");
                    }
                }
            }
        }
        finalChoice(players, gameState);
        announceWinner(players, firstBuilder, gameState);
    }
}
