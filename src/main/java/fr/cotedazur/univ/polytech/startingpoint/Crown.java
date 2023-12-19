package fr.cotedazur.univ.polytech.startingpoint;

public class Crown {
    private Player owner;

    public Crown() {}

    public Crown(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
