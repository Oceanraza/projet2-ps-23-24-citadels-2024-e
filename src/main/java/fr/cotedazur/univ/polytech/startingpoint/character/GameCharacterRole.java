package fr.cotedazur.univ.polytech.startingpoint.character;

/**
 * Enumération représentant les rôles des personnages du jeu.
 */
public enum GameCharacterRole {
    ASSASSIN("Assassin"),
    THIEF("Voleur"),
    MAGICIAN("Magicien"),
    BISHOP("Eveque"),
    KING("Roi"),
    MERCHANT("Marchand"),
    WARLORD("Condottiere"),
    ARCHITECT("Architecte");

    private final String roleName;

    /**
     * Constructeur de l'énumération GameCharacterRole.
     *
     * @param roleName le nom du rôle du personnage.
     */
    GameCharacterRole(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Retourne le nom du rôle du personnage.
     * @return le nom du rôle du personnage.
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Pour imprimer le nom du rôle du personnage lorsque nous devons écrire :
     * Du ... ou
     * De l' ...
     * @return une chaîne de caractères formatée.
     */
    public String toStringDuOrDeL() {
        if (this.equals(GameCharacterRole.BISHOP) || this.equals(GameCharacterRole.ASSASSIN) || (this.equals(GameCharacterRole.ARCHITECT))) {
            return "de l'" + this;
        }
        return "du " + this;
    }

    /**
     * Pour imprimer le nom du rôle du personnage lorsque nous devons écrire :
     * Le... ou
     * L' ...
     * @return une chaîne de caractères formatée.
     */
    public String toStringLeOrLUpperCase() {
        if (this.equals(GameCharacterRole.BISHOP) || this.equals(GameCharacterRole.ASSASSIN) || (this.equals(GameCharacterRole.ARCHITECT))) {
            return "L'" + this;
        }
        return "Le " + this;
    }

    /**
     * Pour imprimer le nom du rôle du personnage lorsque nous devons écrire :
     * le... ou
     * l' ...
     * @return une chaîne de caractères formatée.
     */
    public String toStringLeOrLLowerCase() {
        if (this.equals(GameCharacterRole.BISHOP) || this.equals(GameCharacterRole.ASSASSIN) || (this.equals(GameCharacterRole.ARCHITECT))) {
            return "l'" + this;
        }
        return "le " + this;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du rôle du personnage.
     * @return une représentation sous forme de chaîne de caractères du rôle du personnage.
     */
    @Override
    public String toString() {
        return roleName;
    }
}