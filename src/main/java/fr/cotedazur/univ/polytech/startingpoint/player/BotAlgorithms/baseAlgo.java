package fr.cotedazur.univ.polytech.startingpoint.player.BotAlgorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.ArrayList;

public abstract class baseAlgo{
    protected Bot player;
    public baseAlgo(){}
    public void setPlayer(Bot player){
        this.player = player;
    }
    public abstract void startOfTurn(Game game); //Always draws if needed
    public abstract void chooseCharacterAlgorithm(Game game);
    public abstract void charAlgorithmsManager(Game game);
    public abstract void warlordAlgorithm(Game game);
    public abstract void buildOrNot(Game game);

}
