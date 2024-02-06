package fr.cotedazur.univ.polytech.startingpoint.city;

public enum DistrictColor {
    NOBLE("noble"),
    RELIGIOUS("religieux"),
    TRADE("marchand"),
    MILITARY("militaire"),
    SPECIAL("special");

    private final String colorName;

    DistrictColor(String colorName) {
        this.colorName = colorName;
    }

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

    @Override
    public String toString() {
        return colorName;
    }
}
