package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;

import fr.cotedazur.univ.polytech.startingpoint.characters.King;

public class Bot extends Player {

    Bot(String name) {
        super(name);
    }

    public boolean canBuildDistrictThisTurn() { // Checks If a district in Hand can be built with +2 gold
        for (District dist : districtsInHand) {
            System.out.println(dist.getPrice() + "," + this.getGold() + 2);
            if (dist.getPrice() <= this.getGold() + 2) {
                return true;
            }
        }
        return false;
    }

    public boolean isCharInList(ArrayList<Characters> cha, String askedChar) {
        for (Characters temp : cha) {
            if (temp.getName().equals(askedChar)) {
                return true;
            }
        }
        return false;
    }

    public Characters getCharInList(ArrayList<Characters> cha, String askedChar) {
        for (Characters temp : cha) {
            if (temp.getName().equals(askedChar)) {
                return temp;
            }
        }
        return new King(); // Cette ligne ne peut pas être atteinte puisque getCharInList doit être appelée
                           // après isCharInList **A CHANGER ABSOLUMENT AVANT RENDU FINAL, VERSION
                           // TEMPORAIRE POUR RESPECT DES DATES DE RENDU**
    }

    public void chooseCharacterAlgorithm(Game game) {
        ArrayList<Characters> availableChars = game.availableCharacters();
        if ((this.districtsInHand.size() > 0) && (this.districtsBuilt.size() >= 7) && (canBuildDistrictThisTurn())
                && (isCharInList(availableChars, "Roi"))) {
            Characters chosenCharacter = getCharInList(availableChars, "Roi");
            chooseCharacter(chosenCharacter);
            game.removeChar(chosenCharacter);
            System.out.println(this.name + " a choisi le " + chosenCharacter.getName());
            return;
        } else {
            Characters chosenCharacter = getCharInList(availableChars, "Character1");
            chooseCharacter(chosenCharacter);
            game.removeChar(chosenCharacter);
            System.out.println(this.name + " a choisi le " + chosenCharacter.getName());
            return;
        }
    }

    void play(Game game) {
        // Apply special effect
        ActionManager.applySpecialEffect(this, game);
        // Collect gold
        gold += ActionManager.updateGold(this);
        // The bot draws a card if it has no district in its hand.
        if (districtsInHand.isEmpty() || districtsAlreadyBuilt()) {
            District drawnDistrict = game.drawCard();
            System.out.println(getName() + " pioche le " + drawnDistrict);
            districtsInHand.add(drawnDistrict);
        } else { // Otherwise it gets 2 gold coins
            System.out.println(getName() + " prend deux pièces d'or.");
            gold += 2;
        }
        // The bot builds one district if it has enough money
        for (District district : districtsInHand) {
            if (build(district)) {
                break;
            }
        }
    }
}
