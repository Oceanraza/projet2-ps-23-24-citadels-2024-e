package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.player.Player;

/**
 * Classe représentant la couronne dans le jeu.
 */
public class Crown {
    private Player owner;

    /**
     * Retourne le propriétaire actuel de la couronne.
     *
     * @return le propriétaire actuel de la couronne.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Définit le propriétaire de la couronne.
     *
     * @param owner le nouveau propriétaire de la couronne.
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }
}