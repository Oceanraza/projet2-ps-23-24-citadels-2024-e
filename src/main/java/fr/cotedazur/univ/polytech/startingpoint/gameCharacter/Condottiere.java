package fr.cotedazur.univ.polytech.startingpoint.gameCharacter;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.players.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Condottiere extends GameCharacter {
    boolean lowestDistrictFound = false;
    public Condottiere() {
        super("Condottiere", 8, DistrictColor.militaire);
    }

    @Override
    public void specialEffect(Player player, Game game) { //l'algorithme actuel (12/01) est de détruire le batiment le moins cher du meilleur joueur
        ArrayList<Player> playerList = getSortedPlayersByScore(game,player);
        for (Player targetedPlayer : playerList){
            getLowestDistrict(targetedPlayer).ifPresent(value -> {
                District dist = value;
                if (canDestroyDistrict(dist,player)){
                    targetedPlayer.destroyDistrict(dist);
                    player.removeGold(dist.getPrice()-1);
                    System.out.println("Le Condottiere à détruit le quartier " + dist.getName() + " qui appartient au joueur " + targetedPlayer.getName() + " au prix de " + (dist.getPrice()-1) + " or");
                    LowestDistrictHasBeenFound();
                }
            });
            if (lowestDistrictFound){break;}
        }
    }

    public static ArrayList<Player> getSortedPlayersByScore(Game game, Player playerToRemove){
        ArrayList<Player> sortedPlayersByScore = new ArrayList<>();
        for (Player p : game.getPlayers()){
            p.calculateScore();
            sortedPlayersByScore.add(p);
        }
        Comparator<Player> playerComparator = Comparator
                .comparingInt(Player::getScore)
                .reversed();
        sortedPlayersByScore.sort(playerComparator);
        sortedPlayersByScore.remove(playerToRemove);
        return sortedPlayersByScore;
    }
    public boolean canDestroyDistrict(District d, Player p){
        return (d.getPrice() -1 < p.getGold());
    }
    public static Optional<District> getLowestDistrict(Player player){
        List<District> sortedDistrictByScore = player.getDistrictsBuilt();
        if (sortedDistrictByScore.size() == 0){return Optional.empty();}
        District minPriceDistrict = sortedDistrictByScore.stream()
                .min(Comparator.comparingDouble(District::getPrice))
                .orElse(null);
        return Optional.of(minPriceDistrict);
    }
    public void LowestDistrictHasBeenFound(){lowestDistrictFound = true;}
}