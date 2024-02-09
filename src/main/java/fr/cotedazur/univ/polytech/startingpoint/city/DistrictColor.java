package fr.cotedazur.univ.polytech.startingpoint.city;

/**
 * Enumération des différentes couleurs de quartier.
 */
public enum DistrictColor {
    NOBLE("noble"),
    RELIGIOUS("religieux"),
    TRADE("marchand"),
    MILITARY("militaire"),
    SPECIAL("special");

    private final String colorName;

    /**
     * Constructeur de l'énumération DistrictColor.
     *
     * @param colorName Le nom de la couleur.
     */
    DistrictColor(String colorName) {
        this.colorName = colorName;
    }

    /**
     * Méthode pour obtenir la valeur de l'énumération à partir d'une chaîne de caractères.
     * @param str La chaîne de caractères représentant la couleur.
     * @return La valeur de l'énumération correspondante.
     */
    public static DistrictColor valueOfByString(String str) {
        switch (str) {
            case "noble" -> {
                return DistrictColor.NOBLE;
            }
            case "religieux" -> {
                return DistrictColor.RELIGIOUS;
            }
            case "marchand" -> {
                return DistrictColor.TRADE;
            }
            case "militaire" -> {
                return DistrictColor.MILITARY;
            }
            default -> {
                return DistrictColor.SPECIAL;
            }
        }
    }

    /**
     * Méthode pour obtenir le nom de la couleur.
     * @return Le nom de la couleur.
     */
    @Override
    public String toString() {
        return colorName;
    }
}