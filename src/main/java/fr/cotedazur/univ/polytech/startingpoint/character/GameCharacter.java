package fr.cotedazur.univ.polytech.startingpoint.character;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

/**
 * Classe abstraite représentant un personnage de jeu.
 */
public abstract class GameCharacter {
    protected GameCharacterRole role;
    protected int runningOrder;
    protected DistrictColor color;

    private boolean isAlive = true;
    private Player attacker = null;

    /**
     * Constructeur de la classe GameCharacter.
     *
     * @param role         le rôle du personnage.
     * @param runningOrder l'ordre de jeu du personnage.
     */
    protected GameCharacter(GameCharacterRole role, int runningOrder) {
        this.role = role;
        this.runningOrder = runningOrder;
        this.color = null;
    }

    /**
     * Constructeur de la classe GameCharacter.
     *
     * @param role         le rôle du personnage.
     * @param runningOrder l'ordre de jeu du personnage.
     * @param color        la couleur du personnage.
     */
    protected GameCharacter(GameCharacterRole role, int runningOrder, DistrictColor color) {
        this.role = role;
        this.runningOrder = runningOrder;
        this.color = color;
    }

    // Getters

    /**
     * Retourne le rôle du personnage.
     *
     * @return le rôle du personnage.
     */
    public GameCharacterRole getRole() {
        return role;
    }

    /**
     * Retourne l'ordre de jeu du personnage.
     *
     * @return l'ordre de jeu du personnage.
     */
    public int getRunningOrder() {
        return runningOrder;
    }

    /**
     * Retourne la couleur du personnage.
     *
     * @return la couleur du personnage.
     */
    public DistrictColor getColor() {
        return color;
    }

    /**
     * Vérifie si le personnage est vivant.
     *
     * @return vrai si le personnage est vivant, faux sinon.
     */
    public boolean getIsAlive() {
        return isAlive;
    }

    /**
     * Retourne l'attaquant du personnage.
     *
     * @return l'attaquant du personnage.
     */
    public Player getAttacker() {
        return attacker;
    }

    // Setter

    /**
     * Définit si le personnage est vivant.
     *
     * @param isAlive vrai si le personnage est vivant, faux sinon.
     */
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * Définit l'attaquant du personnage.
     *
     * @param attacker l'attaquant du personnage.
     */
    public void setAttacker(Player attacker) {
        this.attacker = attacker;
    }

    /**
     * Méthode abstraite pour l'effet spécial du personnage.
     *
     * @param player       le joueur qui joue le personnage.
     * @param game         l'état actuel du jeu.
     * @param optionalArgs arguments optionnels.
     */
    public abstract void specialEffect(Player player, Game game, Object... optionalArgs);

    /**
     * Retourne une représentation sous forme de chaîne de caractères du rôle du personnage.
     *
     * @return une représentation sous forme de chaîne de caractères du rôle du personnage.
     */
    @Override
    public String toString() {
        return role.toString();
    }
}