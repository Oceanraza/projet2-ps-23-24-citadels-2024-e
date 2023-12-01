package fr.cotedazur.univ.polytech.startingpoint;

public class Quartier {
    private QuartierColor color;
    private int price;
    private String name;
    public Quartier(String name, int price, QuartierColor color){
        this.color = color; this.price = price; this.name = name;
    }
    public String getName(){
        return name;
    }
    public int getPrice(){
        return price;
    }
    public QuartierColor getColor() {
        return color;
    }
}
