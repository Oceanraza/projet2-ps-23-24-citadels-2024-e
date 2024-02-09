package fr.cotedazur.univ.polytech.startingpoint.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.BaseAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.RandomAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart.EinsteinAlgo;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.smart.RichardAlgo;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Cette classe est utilisée pour des méthodes de base qui ont des utilisations spécifiques dans le jeu.
 */
public class Utils {
    private static final SecureRandom random = new SecureRandom();

    private Utils() {
        throw new IllegalStateException("Utils est une classe utilitaire");
    }

    /**
     * Génère un nombre aléatoire.
     *
     * @param bound la limite supérieure pour le nombre aléatoire.
     * @return un nombre aléatoire.
     */
    public static int generateRandomNumber(int bound) {
        if(bound == 0){
            return 0;
        }
        return random.nextInt(bound);
    }

    /**
     * Obtient le nombre le plus élevé de cartes en main parmi les joueurs.
     * @param players la liste des joueurs.
     * @param currentPlayer le joueur actuel.
     * @return le nombre le plus élevé de cartes en main.
     */
    public static int getHighestNumberOfCardsInHand(List<Player> players, Player currentPlayer) {
        return players.stream()
                .filter(player -> !player.equals(currentPlayer))
                .mapToInt(player -> player.getDistrictsInHand().size())
                .max()
                .orElse(0);
    }

    /**
     * Vérifie si un quartier peut être détruit.
     * @param d le quartier à vérifier.
     * @param p le joueur qui tente de détruire le quartier.
     * @return vrai si le quartier peut être détruit, faux sinon.
     */
    public static boolean canDestroyDistrict(District d, Player p){
        return ((d.getPrice() -1 < p.getGold())&&(!d.getName().equals("Donjon")));
    }

    /**
     * Analyse un fichier JSON en un JsonNode.
     * @param filePath le chemin du fichier JSON.
     * @return un JsonNode représentant le fichier JSON.
     * @throws IOException si une erreur d'entrée/sortie se produit.
     */
    public static JsonNode parseJsonFromFile(String filePath) throws IOException {
        // Create an ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Read JSON from file into a JsonNode
        return objectMapper.readTree(new File(filePath));
    }

    /**
     * Convertit un JsonNode en une liste de quartiers.
     * @param districtsNode le JsonNode à convertir.
     * @return une liste de quartiers.
     */
    public static List<District> convertJsonNodeToDistrictList(JsonNode districtsNode) {
        ArrayList<District> districtList = new ArrayList<>();

        // Iterate through each district in the array
        for (JsonNode districtNode : districtsNode) {
            String name = districtNode.path("name").asText();
            int price = districtNode.path("price").asInt();
            String color = districtNode.path("color").asText();
            int number = districtNode.path("number").asInt();
            int bonusPoints = districtNode.path("bonusPoints").asInt();

            // Create a District object and add it to the list
            for (int i = 0; i < number; i++) {
                District district = new District(name, price, DistrictColor.valueOfByString(color), bonusPoints);
                districtList.add(district);
            }
        }
        return districtList;
    }

    /**
     * Définit les algorithmes pour le jeu.
     * @param algorithmsInGame la liste des algorithmes dans le jeu.
     * @param nbOfEinstein le nombre d'Einstein.
     * @param nbOfRichard le nombre de Richard.
     * @param nbOfRandom le nombre de Random.
     */
    public static void setAlgorithms(List<BaseAlgo> algorithmsInGame, int nbOfEinstein, int nbOfRichard, int nbOfRandom) {
        while (nbOfEinstein > 0) {
            algorithmsInGame.add(new EinsteinAlgo());
            nbOfEinstein--;
        }
        while (nbOfRichard > 0) {
            algorithmsInGame.add(new RichardAlgo());
            nbOfRichard--;
        }
        while (nbOfRandom > 0) {
            algorithmsInGame.add(new RandomAlgo());
            nbOfRandom--;
        }
    }

    /**
     * Réinitialise les scores et les placements.
     * @param totalPlacements les placements totaux.
     * @param totalScores les scores totaux.
     */
    public static void resetScoresAndPlacements(Map<String, List<Integer>> totalPlacements, Map<String, Integer> totalScores) {
        List<Integer> initialPlacement = Arrays.asList(0, 0, 0, 0);
        for (String key : totalPlacements.keySet()) {
            totalPlacements.put(key, new ArrayList<>(initialPlacement));
        }
        for (String key : totalScores.keySet()) {
            totalScores.put(key, 0);
        }
    }
}