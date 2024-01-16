package fr.cotedazur.univ.polytech.startingpoint.city;

import fr.cotedazur.univ.polytech.startingpoint.DistrictColor;

import java.util.Objects;

public class District {
    private final DistrictColor color;
    private final int price;
    private final String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        District d = (District) o;
        return getName().equals(d.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, color);
    }
}
