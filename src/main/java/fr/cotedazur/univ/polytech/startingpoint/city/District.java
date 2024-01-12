package fr.cotedazur.univ.polytech.startingpoint.city;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;

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

    public String toString() {
        return name + "-" + price + "-" + color;
    }

    public boolean equals(District obj) {
        return (getName().equals(obj.getName()));
    }
}
