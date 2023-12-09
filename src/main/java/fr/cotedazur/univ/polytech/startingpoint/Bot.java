package fr.cotedazur.univ.polytech.startingpoint;

public class Bot extends Player{

    Bot(String name){
        super(name);
    }

    void play(Game game) {
        // The bot draws a card if it has no district in its hand.
        if(districtsInHand.isEmpty() || districtsAlreadyBuilt()) {
            System.out.println(getName() + " pioche.");
            districtsInHand.add(game.drawCard());
        } else { // Otherwise it gets 2 gold coins
            System.out.println(getName() + " prend deux pièces d'or.");
            gold += 2;
        }
        // The bot builds one district if it has enough money
        for(District district: districtsInHand) {
            if(build(district)){
                break;
            }
        }
    }
}
