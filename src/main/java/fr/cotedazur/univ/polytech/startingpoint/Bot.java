package fr.cotedazur.univ.polytech.startingpoint;

public class Bot extends Player{
    Bot(){
        super();
    }

    void play(Game game) {
        // The bot draws a card if it has no district in its hand.
        if(districtsInHand.isEmpty()) {
            System.out.println("Le bot pioche.");
            District districtPicked = game.drawCard();
            districtsInHand.add(districtPicked);
            game.gameDeck.remove(districtPicked);
        } else { // Otherwise it gets 2 gold coins
            System.out.println("Le bot prend deux pièces d'or.");
            gold += 2;
        }
        // The bot builds one district if it has enough money
        for(District district: districtsInHand) {
            if(build(district)) {
                break;
            }
        }
    }
}
