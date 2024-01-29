package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class BaseAlgo {
    protected Bot bot;
    public BaseAlgo(){}
    public void setPlayer(Bot player){
        this.bot = player;
    }
    public abstract int startOfTurnChoice();
    public abstract void chooseCharacterAlgorithm(Game game);
    public abstract void charAlgorithmsManager(Game game);
    public abstract void warlordAlgorithm(Game game);
    public abstract void kingAlgorithm (Game game);
    public abstract void magicianAlgorithm(Game game);
    public abstract void assassinAlgorithm(Game game);

    public abstract void buildOrNot(GameState gamestate);
    public abstract void huntedQuarterAlgorithm(District huntedQuarter);
}
