package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.List;

public class GameState {
    private int turn;

    public GameState() {
        this.turn = 1;
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
        return (player.getCity().getDistrictsBuilt().size() >= 8);
    }
}
