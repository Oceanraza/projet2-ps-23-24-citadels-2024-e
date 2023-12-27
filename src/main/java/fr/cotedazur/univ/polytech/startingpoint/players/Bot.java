package fr.cotedazur.univ.polytech.startingpoint.players;

import java.util.ArrayList;
import java.util.List;

import fr.cotedazur.univ.polytech.startingpoint.ActionManager;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.Game;
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

    public void chooseCharacterAlgorithm(Game game) {
        ArrayList<GameCharacter> availableChars = game.getAvailableChars();
        // If the bot can build its 8th quarter, it will choose the king (if possible)
        if (!(this.getDistrictsInHand().isEmpty()) && (this.getDistrictsBuilt().size() >= 7) && (canBuildDistrictThisTurn())
                && (isCharInList(availableChars, "Roi"))) {
            GameCharacter chosenCharacter = getCharInList(availableChars, "Roi");
            setGameCharacter(chosenCharacter);
            game.removeAvailableChar(chosenCharacter);
            System.out.println(this.getName() + " a choisi le " + chosenCharacter.getName());
        } else {
            GameCharacter chosenCharacter = getCharInList(availableChars, "Personnage 1");
            setGameCharacter(chosenCharacter);
            game.removeAvailableChar(chosenCharacter);
            System.out.println(this.getName() + " a choisi le " + chosenCharacter.getName());
        }
    }

    @Override
    public void play(Game game) {
        // Apply special effect
        if (getGameCharacter() != null) {
            ActionManager.applySpecialEffect(this, game);
        }
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
