package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;


// This class is called CharacterS because we can't name it Character
public abstract class GameCharacter {
    protected String name;
    protected int runningOrder;
    protected DistrictColor color;

    private boolean isAlive;
    private Player attacker;

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
    public DistrictColor getColor() { return color;}

    public boolean getIsAlive() { return isAlive; }
    public Player getAttacker() { return attacker; }

    public void setIsAlive(boolean isAlive) { this.isAlive = isAlive; }
    public void setAttacker(Player attacker) { this.attacker = attacker; }

    public abstract void specialEffect(Player player, Game game, Object... optionalArgs );
}
