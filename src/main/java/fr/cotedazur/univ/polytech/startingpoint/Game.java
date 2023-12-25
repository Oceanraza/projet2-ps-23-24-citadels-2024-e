package fr.cotedazur.univ.polytech.startingpoint;

import static fr.cotedazur.univ.polytech.startingpoint.Main.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.Character1;
import fr.cotedazur.univ.polytech.startingpoint.gameCharacter.King;

public class Game {

    public ArrayList<District> gameDeck;
    private Crown crown;
    public List<Player> bots;
    public Map<String, GameCharacter> allCharacters = new HashMap<>();
    public ArrayList<GameCharacter> availableChars = new ArrayList<>();

    public void addCardNumber(District d, int n) {
        for (int i = 0; i < n; i++) {
            gameDeck.add(d);
        }
    }

    public Game() {
        init();
    }

    public void init(){
        gameDeck = new ArrayList<>();

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

        // Create the list of players
        bots = new ArrayList<Player>();

        // Creates the characters
        allCharacters.put("Roi", new King());
        allCharacters.put("Personnage 1", new Character1());
    }

    public Crown getCrown() {
        return crown;
    }

    // Add players to the list of players
    public void setPlayers(Player firstBot, Player secondBot) {
        bots.add(firstBot);
        bots.add(secondBot);
    }

    public List<Player> getPlayers() {
        return bots;
    }

    // Set running order depending on the running order of the characters
    public List<Player> setRunningOrder() {
        return this.getPlayers().stream()
                .sorted(Comparator.comparingInt(player -> player.getCharacter().getRunningOrder()))
                .collect(Collectors.toList());
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
        availableChars.add(allCharacters.get("Personnage 1"));
    }

    public ArrayList<GameCharacter> availableCharacters() {
        System.out.println("Les personnages disponibles sont : ");
        for (GameCharacter temp : availableChars) {
            System.out.print(temp.getName() + " ");
        }
        System.out.println("");
        return availableChars;
    }

    public void removeChar(GameCharacter cha) {
        availableChars.remove(cha);
    }

    public String toString() {
        String str = "Les cartes dans le deck sont : \n";
        for (int i = 0; i < gameDeck.size(); i++) {
            str = str + gameDeck.get(i).toString() + '\n';
        }
        return str;
    }
}
