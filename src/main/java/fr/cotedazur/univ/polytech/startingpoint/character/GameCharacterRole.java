package fr.cotedazur.univ.polytech.startingpoint.character;

public enum GameCharacterRole {
    ASSASSIN("Assassin"),
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

    public String toString() {
        return roleName;
    }
}
