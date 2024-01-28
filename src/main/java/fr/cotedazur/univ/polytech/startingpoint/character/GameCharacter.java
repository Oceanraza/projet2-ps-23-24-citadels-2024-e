package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

public abstract class GameCharacter {
    protected GameCharacterRole role;
    protected int runningOrder;
    protected DistrictColor color;

    private boolean isAlive = true;
    private Player attacker = null;

    protected GameCharacter(GameCharacterRole role, int runningOrder) {
        this.role = role;
        this.runningOrder = runningOrder;
        this.color = null;
    }
    protected GameCharacter(GameCharacterRole role, int runningOrder, DistrictColor color) {
        this.role = role;
        this.runningOrder = runningOrder;
        this.color = color;
    }

    public GameCharacterRole getRole() { return role; }
    public int getRunningOrder() { return runningOrder; }
    public DistrictColor getColor() { return color;}

    public boolean getIsAlive() { return isAlive; }
    public Player getAttacker() { return attacker; }

    public void setIsAlive(boolean isAlive) { this.isAlive = isAlive; }
    public void setAttacker(Player attacker) { this.attacker = attacker; }

    public abstract void specialEffect(Player player, Game game, Object... optionalArgs);

    public String toString() {
        return role.toString();
    }
}
