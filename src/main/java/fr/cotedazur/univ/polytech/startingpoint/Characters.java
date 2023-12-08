package fr.cotedazur.univ.polytech.startingpoint;

public abstract class Characters {
    private String name;
    private int runningOrder;
    public Characters(String name, int runningOrder) {
        this.name = name;
        this.runningOrder = runningOrder;
    }

    public String getName() { return name; }
    public int getRunningOrder() { return runningOrder; }

    public abstract int goldOnPlay();

    public abstract void specialEffect();
}
