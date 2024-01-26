package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;

public abstract class BaseAlgo {
    protected Bot bot;
    public BaseAlgo(){}
    public void setPlayer(Bot player){
        this.bot = player;
    }
    public abstract void startOfTurn(Game game); //Always draws if needed
    public abstract void chooseCharacterAlgorithm(Game game);
    public abstract void charAlgorithmsManager(Game game);
    public abstract void warlordAlgorithm(Game game);
    public abstract void buildOrNot(Game game);
}
