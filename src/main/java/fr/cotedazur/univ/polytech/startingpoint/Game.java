package fr.cotedazur.univ.polytech.startingpoint;

import java.util.ArrayList;

import static fr.cotedazur.univ.polytech.startingpoint.Main.random;

public class Game {

    public ArrayList<District> gameDeck = new ArrayList<>();

    public void addCardNumber(District d,int n){
        for (int i = 0; i<n; i++){
            gameDeck.add(d);
        }
    }

    public Game(){
        init();
    }
    public void init(){
        // Adding religieux districts
        addCardNumber( new District("Temple", 1, DistrictColor.religieux), 3);
        addCardNumber( new District("Eglise", 2, DistrictColor.religieux), 4);
        addCardNumber( new District("Monastere", 3, DistrictColor.religieux), 3);
        addCardNumber( new District("Cathedrale", 5, DistrictColor.religieux), 2);

        addCardNumber( new District("Manoir", 3, DistrictColor.noble), 5);
        addCardNumber( new District("Chateau", 4, DistrictColor.noble), 4);
        addCardNumber( new District("Palais", 5, DistrictColor.noble), 2);

        addCardNumber( new District("Taverne", 1, DistrictColor.marchand), 5);
        addCardNumber( new District("Echoppe", 2, DistrictColor.marchand), 3);
        addCardNumber( new District("Marche", 2, DistrictColor.marchand), 4);
        addCardNumber( new District("Comptoir", 3, DistrictColor.marchand), 3);
        addCardNumber( new District("Port", 4, DistrictColor.marchand), 3);
        addCardNumber( new District("Hotel de ville", 5, DistrictColor.marchand), 2);

        addCardNumber( new District("Tour de guet", 1, DistrictColor.militaire), 3);
        addCardNumber( new District("Prison", 2, DistrictColor.militaire), 3);
        addCardNumber( new District("Caserne", 3, DistrictColor.militaire), 3);
        addCardNumber( new District("Forteresse", 5, DistrictColor.militaire), 2);

        addCardNumber( new District("Donjon", 3, DistrictColor.special), 1);
        addCardNumber( new District("Laboratoire", 5, DistrictColor.special), 1);
        addCardNumber( new District("Observatoire", 5, DistrictColor.special), 1);
        addCardNumber( new District("Cour des miracles", 2, DistrictColor.special), 1);
        addCardNumber( new District("Cimetiere", 5, DistrictColor.special), 1);


    }

    public District drawCard() {
        District cardDrawn = gameDeck.get(random.nextInt(gameDeck.size() - 1));
        gameDeck.remove(cardDrawn);
        return cardDrawn;
    }

    public String toString(){
        String str = "Les cartes dans le deck sont : \n";
        for(int i = 0;i<gameDeck.size();i++){
            str = str + gameDeck.get(i).toString() + '\n';
        }
        return str;
    }
}
