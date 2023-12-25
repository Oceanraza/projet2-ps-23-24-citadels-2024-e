package fr.cotedazur.univ.polytech.startingpoint;

public record District(String name, int price, DistrictColor color) {

    public String toString() {
        return name + "-" + price + "-" + color;
    }
}
