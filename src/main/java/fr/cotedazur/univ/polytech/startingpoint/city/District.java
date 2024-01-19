package fr.cotedazur.univ.polytech.startingpoint.city;

import java.util.Objects;
import java.util.Optional;

public class District {
    private final DistrictColor color;
    private final int price;
    private final String name;
    private final int bonusPoints;

    public District(String name, int price, DistrictColor color){
        this.color = color;
        this.price = price;
        this.name = name;
        this.bonusPoints = 0;
    }

    public District(String name, int price, DistrictColor color, int bonusPoints){
        this.color = color;
        this.price = price;
        this.name = name;
        this.bonusPoints = bonusPoints;
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
    public int getBonusPoints() {
        return bonusPoints;
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
