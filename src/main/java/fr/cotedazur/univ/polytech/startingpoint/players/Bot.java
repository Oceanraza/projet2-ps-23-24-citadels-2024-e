package fr.cotedazur.univ.polytech.startingpoint.players;

import java.util.*;

import fr.cotedazur.univ.polytech.startingpoint.ActionManager;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.King;
import fr.cotedazur.univ.polytech.startingpoint.players.BotAlgorithms.baseAlgo;


public class Bot extends Player {
    public baseAlgo botAlgo;

    public Bot(String name, baseAlgo algo) {
        super(name);
        algo.setPlayer(this);
        this.botAlgo = algo;
    }
    public Bot(String name){ //for tests
        super(name);
    }

    public boolean canBuildDistrictThisTurn() { // Checks If a district in Hand can be built with +2 gold
        for (District dist : getDistrictsInHand()) {
            if (dist.getPrice() <= this.getGold() + 2) {
                return true;
            }
        }
        return false;
    }

    public boolean isCharInList(List<GameCharacter> cha, String askedChar) {
        for (GameCharacter temp : cha) {
            if (temp.getName().equals(askedChar)) {
                return true;
            }
        }
        return false;
    }

    public GameCharacter getCharInList(List<GameCharacter> cha, String askedChar) {
        for (GameCharacter temp : cha) {
            if (temp.getName().equals(askedChar)) {
                return temp;
            }
        }
        return new King(); // Cette ligne ne peut pas être atteinte puisque getCharInList doit être appelée
        // après isCharInList **A CHANGER ABSOLUMENT AVANT RENDU FINAL, VERSION
        // TEMPORAIRE POUR RESPECT DES DATES DE RENDU**
    }
    public void chooseChar(Game game,String askedChar){
        GameCharacter chosenCharacter = getCharInList(game.getAvailableChars(), askedChar);
        setGameCharacter(chosenCharacter);
        game.removeAvailableChar(chosenCharacter);
        System.out.println(this.getName() + " a choisi le " + chosenCharacter.getName());
    }

    @Override
    public void play(Game game) {
        // Apply special effect, no need for now
       // ActionManager.applySpecialEffect(this, game);

        // Collect gold
        addGold(ActionManager.collectGold(this));
        // The bot draws a card if it has no district in its hand.
        botAlgo.startOfTurn(game);
        botAlgo.charAlgorithmsManager(game);
        // The bot builds one district if it has enough money
        botAlgo.buildOrNot(game);
    }


}
