package fr.cotedazur.univ.polytech.startingpoint.players;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import fr.cotedazur.univ.polytech.startingpoint.ActionManager;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.King;


public class Bot extends Player {

    public Bot(String name) {
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
    public void chooseCharacterAlgorithm(Game game) {
        ArrayList<GameCharacter> availableChars = game.getAvailableChars();
        // If the bot can build its 8th quarter next turn, it will choose the king (if possible)
        if (!(this.getDistrictsInHand().isEmpty()) && (this.getDistrictsBuilt().size() >= 7) && (canBuildDistrictThisTurn())
                && (isCharInList(availableChars, "Roi"))) {
            chooseChar(game,"Roi");
        }
        // If the bot doesn't have an immediate way to win, it will just pick the character who gives out the most gold for him
        else {
            GameCharacter chosenChar = availableChars.get(0);
            for (GameCharacter cha : availableChars){
                if (getNumberOfDistrictsByColor().get(cha.getColor()) > getNumberOfDistrictsByColor().get(chosenChar.getColor())){
                    chosenChar = cha;
                }
            }
            chooseChar(game,chosenChar.getName());
        }
    }

    @Override
    public void play(Game game) {
        // Apply special effect
        ActionManager.applySpecialEffect(this, game);

        // Collect gold
        addGold(ActionManager.collectGold(this));
        // The bot draws a card if it has no district in its hand.
        if (getDistrictsInHand().isEmpty() || districtsAlreadyBuilt()) {
            District drawnDistrict = game.drawCard();
            System.out.println(getName() + " pioche le " + drawnDistrict);
            getDistrictsInHand().add(drawnDistrict);
        } else { // Otherwise it gets 2 gold coins
            System.out.println(getName() + " prend deux pièces d'or.");
            addGold(2);
        }
        // The bot builds one district if it has enough money
        for (District district : getDistrictsInHand()) {
            if (build(district)) {
                break;
            }
        }
    }
}
