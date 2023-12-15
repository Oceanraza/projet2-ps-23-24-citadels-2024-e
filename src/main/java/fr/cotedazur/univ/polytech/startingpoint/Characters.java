package fr.cotedazur.univ.polytech.startingpoint;

// This class is called CharacterS because we can't name it Character
public abstract class Characters {
    protected String name;
    protected int runningOrder;
    public Characters(String name, int runningOrder) {
        this.name = name;
        this.runningOrder = runningOrder;
    }

    public String getName() { return name; }

    public int getRunningOrder() { return runningOrder; }

    public abstract void specialEffect(Player player, Game game);
}