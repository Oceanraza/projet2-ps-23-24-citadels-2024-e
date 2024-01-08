package fr.cotedazur.univ.polytech.startingpoint;

import static fr.cotedazur.univ.polytech.startingpoint.Main.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.*;


import java.util.*;
import java.util.stream.Collectors;

import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.players.*;


public class Game {
    private ArrayList<District> gameDeck;
    private Crown crown;
    private List<Player> players;
    private Map<String, GameCharacter> allCharacters;
    private ArrayList<GameCharacter> availableChars;
    // Getter
    public ArrayList<District> getGameDeck() {
        return gameDeck;
    }
    public Crown getCrown() {
        return crown;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public ArrayList<GameCharacter> getAvailableChars() {
        availableCharacters();
        return availableChars;
    }

    // Setter
    public void setPlayers(Player... bots) { // Add players to the list of players
        players.addAll(Arrays.asList(bots));
    }

    public ArrayList<District> gameDeck = new ArrayList<>();
    private Crown crown;
    public Map<String, Characters> allCharacters = new HashMap<>();
    public ArrayList<Characters> availableChars = new ArrayList<>();

    public void addCardNumber(District d, int n) {
        for (int i = 0; i < n; i++) {
            gameDeck.add(d);
        }
    }

    public void removeAvailableChar(GameCharacter cha) {
        availableChars.remove(cha);
    }

    public Game() {
        init();
    }

    public void init() {
        // Adding religieux districts
        addCardNumber(new District("Temple", 1, DistrictColor.religieux), 3);
        addCardNumber(new District("Eglise", 2, DistrictColor.religieux), 4);
        addCardNumber(new District("Monastere", 3, DistrictColor.religieux), 3);
        addCardNumber(new District("Cathedrale", 5, DistrictColor.religieux), 2);

        addCardNumber(new District("Manoir", 3, DistrictColor.noble), 5);
        addCardNumber(new District("Chateau", 4, DistrictColor.noble), 4);
        addCardNumber(new District("Palais", 5, DistrictColor.noble), 2);

        addCardNumber(new District("Taverne", 1, DistrictColor.marchand), 5);
        addCardNumber(new District("Echoppe", 2, DistrictColor.marchand), 3);
        addCardNumber(new District("Marche", 2, DistrictColor.marchand), 4);
        addCardNumber(new District("Comptoir", 3, DistrictColor.marchand), 3);
        addCardNumber(new District("Port", 4, DistrictColor.marchand), 3);
        addCardNumber(new District("Hotel de ville", 5, DistrictColor.marchand), 2);

        addCardNumber(new District("Tour de guet", 1, DistrictColor.militaire), 3);
        addCardNumber(new District("Prison", 2, DistrictColor.militaire), 3);
        addCardNumber(new District("Caserne", 3, DistrictColor.militaire), 3);
        addCardNumber(new District("Forteresse", 5, DistrictColor.militaire), 2);

        addCardNumber(new District("Donjon", 3, DistrictColor.special), 1);
        addCardNumber(new District("Laboratoire", 5, DistrictColor.special), 1);
        addCardNumber(new District("Observatoire", 5, DistrictColor.special), 1);
        addCardNumber(new District("Cour des miracles", 2, DistrictColor.special), 1);
        addCardNumber(new District("Cimetiere", 5, DistrictColor.special), 1);

        // Create a crown
        crown = new Crown();

        // Creates the characters
        allCharacters.put("Roi", new King());
        allCharacters.put("Marchand", new Marchand());
        allCharacters.put("Eveque", new Eveque());
        allCharacters.put("Condottiere", new Condottiere());
    }

    public District drawCard() {
        District cardDrawn = gameDeck.get(random.nextInt(gameDeck.size() - 1));
        gameDeck.remove(cardDrawn);
        return cardDrawn;
    }

    public void shuffleChars(int numberOfPlayers) { // numberOfPlayers needs to be used for automatic code but due to
                                                    // time reasons, it's postponed to a later day.
        while (!availableChars.isEmpty()) {
            availableChars.remove(0);
        }

        availableChars.add(allCharacters.get("Roi"));
        availableChars.add(allCharacters.get("Marchand"));
        availableChars.add(allCharacters.get("Eveque"));
        availableChars.add(allCharacters.get("Condottiere"));
    }

    public ArrayList<Characters> availableCharacters() {
        System.out.println("Les personnages disponibles sont : ");
        for (Characters temp : availableChars) {
            System.out.print(temp.getName() + " ");
        }
        System.out.println("");
        return availableChars;
    }

    public void removeChar(Characters cha) {
        availableChars.remove(cha);
    }
    public void charSelectionFiller(){
        for (Player p: players){
            if (p.getGameCharacter() == null){
                Bot p2 = (Bot) p;
                System.out.println(p2);
                //We create a new variable p2 to cast p to Bot each time
                //Good to note that you can't just cast the whole List
                p2.chooseCharacterAlgorithm(this);}
        }
    }

    public String toString() {
        String str = "Les cartes dans le deck sont : \n";
        for (int i = 0; i < gameDeck.size(); i++) {
            str = str + gameDeck.get(i).toString() + '\n';
        }
        return str;
    }

    public void setAllCharsToNull() {
        for (Player  p: players){
            p.setGameCharacter(null);
        }
    }
}
