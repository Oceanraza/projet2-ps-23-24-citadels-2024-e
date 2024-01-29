package fr.cotedazur.univ.polytech.startingpoint.character;

public enum GameCharacterRole {
    ASSASSIN("Assassin"),
    MAGICIAN("Magicien"),
    BISHOP("Eveque"),
    KING("Roi"),
    MERCHANT("Marchand"),
    WARLORD("Condottiere");

    private final String roleName;

    GameCharacterRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    /*
    To print the role name of the character when we have to write :
    Du ... or
    De l' ...
    */
    public String toStringDuOrDeL() {
        if (this.equals(GameCharacterRole.BISHOP) || this.equals(GameCharacterRole.ASSASSIN)) {
            return "de l'" + this;
        }
        return "du " + this;
    }

    /*
    To print the role name of the character when we have to write :
    Le... or
    L' ...
     */
    public String toStringLeOrL() {
        if (this.equals(GameCharacterRole.BISHOP) || this.equals(GameCharacterRole.ASSASSIN)) {
            return "L'" + this;
        }
        return "Le " + this;
    }

    public String toString() {
        return roleName;
    }
}
