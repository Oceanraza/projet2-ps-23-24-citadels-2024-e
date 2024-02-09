package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.List;

import static fr.cotedazur.univ.polytech.startingpoint.Game.CITY_SIZE_TO_WIN;

/**
 * This class represents the state of the game
 * It contains the current turn
 */
public class GameState {
    private int turn;

    public GameState() {
        this.turn = 0;
    }

    public int getTurn() {
        return turn;
    }

    public void nextTurn() {
        turn++;
    }

    // If a player has 8 districts built, he wins
    public boolean isGameFinished(List<Player> players) {
        for (Player p: players){
            if (isFinished(p)){return true;}
        }
        return false;
    }

    public boolean isFinished(Player player) {
        return (player.getCity().getDistrictsBuilt().size() >= CITY_SIZE_TO_WIN);
    }

    public void resetGameState() {
        turn = 0;
    }
}
