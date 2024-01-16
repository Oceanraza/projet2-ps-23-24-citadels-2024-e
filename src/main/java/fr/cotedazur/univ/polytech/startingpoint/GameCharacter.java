package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.city.District;

import fr.cotedazur.univ.polytech.startingpoint.player.Player;


// This class is called CharacterS because we can't name it Character
public abstract class GameCharacter {
    protected String name;
    protected int runningOrder;
    protected DistrictColor color;
    protected GameCharacter(String name, int runningOrder) {
        this.name = name;
        this.runningOrder = runningOrder;
        this.color = null;
    }
    protected GameCharacter(String name, int runningOrder,DistrictColor color) {
        this.name = name;
        this.runningOrder = runningOrder;
        this.color = color;
    }
    public String getName() { return name; }

    public int getRunningOrder() { return runningOrder; }

    public abstract void specialEffect(Player player,Game game,Object... optionalArgs );

    public DistrictColor getColor() {return color;}
}
