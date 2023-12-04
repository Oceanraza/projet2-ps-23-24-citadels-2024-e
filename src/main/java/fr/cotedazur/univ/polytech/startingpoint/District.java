package fr.cotedazur.univ.polytech.startingpoint;

public class District {
    private DistrictColor color;
    private int price;
    private String name;
    public District(String name, int price, DistrictColor color){
        this.color = color; this.price = price; this.name = name;
    }
    public String getName(){
        return name;
    }
    public int getPrice(){
        return price;
    }
    public DistrictColor getColor() {
        return color;
    }
}
