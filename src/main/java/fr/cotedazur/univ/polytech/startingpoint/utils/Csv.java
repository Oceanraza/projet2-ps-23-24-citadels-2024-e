package fr.cotedazur.univ.polytech.startingpoint.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

/**
 * Classe utilitaire pour la gestion des fichiers CSV.
 */
public class Csv {
    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
     */
    private Csv() {
        throw new IllegalStateException("Csv is a utility class");
    }

    /**
     * Écrit les statistiques dans un fichier CSV.
     *
     * @param args Les données à écrire dans le fichier CSV.
     */
    public static void writeStats(List<String[]> args) {
        // Définit le chemin du fichier CSV
        String csvFilePath = "src/main/resources/stats/gamestats.csv";
        // Crée un objet File pour vérifier si le fichier existe
        File file = new File(csvFilePath);
        if (!file.getParentFile().mkdirs()) {
            try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    for (String[] argList : args) {
                        // Vérifie si la première colonne est "0"
                        if (nextLine.length > 0 && nextLine[0].equals(argList[0])) {
                            // Remplace les deuxième et troisième colonnes par "7" et "8"
                            for (int i = 2; i < 8; i++) {
                                int newVal = Integer.parseInt(argList[i]) + Integer.parseInt(nextLine[i]);
                                if (i == 2) {
                                    newVal = newVal / 2;
                                }
                                argList[i] = Integer.toString(newVal);
                            }
                        }
                    }

                }
            } catch (IOException | CsvValidationException e) {
                // Gérer l'exception
            }
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath, true))) {
            resetStats();
            // Écrit la première ligne
            for (String[] tempList : args) {
                writer.writeNext(tempList);
            }
            LOGGER.log(CSV_OR_THOUSAND, COLOR_GREEN + "\nValeurs ajoutees au fichier CSV avec succes !" + COLOR_RESET);
        } catch (IOException e) {
            // Gérer l'exception
        }
    }

    /**
     * Réinitialise les statistiques.
     */
    public static void resetStats() {
        String csvFilePath = "src/main/resources/stats/gamestats.csv";
        // Crée un objet File pour vérifier si le fichier existe
        File file = new File(csvFilePath);
        if (file.getParentFile().mkdirs()) {
            LOGGER.log(CSV_OR_THOUSAND, COLOR_GREEN + "Le fichier n'existait pas, il a ete cree avec succes !" + COLOR_RESET);
        }
        String[] data = {"Nom du joueur ", "Algorithme du joueur", "Score Moyen", "Nombre de parties jouées", "Nombre de 1ere places", "Nombre de 2e places", "Nombre de 3e places", "Nombre de 4e places"};
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            writer.writeNext(data);
        } catch (IOException e) {
            // Gérer l'exception
        }
    }

    /**
     * Affiche les informations du joueur.
     * @param totalScores Les scores totaux.
     * @param totalPlacements Les placements totaux.
     * @param wantedPlayer Le joueur recherché.
     * @param numberOfGames Le nombre de parties.
     */
    public static void printPlayerInfo(Map<String, Integer> totalScores, Map<String, List<Integer>> totalPlacements, Player wantedPlayer, int numberOfGames) {
        double scoreJoueur = totalPlacements.get(wantedPlayer.getName()).get(0);
        String winrate = wantedPlayer.getName() + " a gagne un total de " + (int) scoreJoueur + " parties.\nIl gagne donc " + (scoreJoueur / (numberOfGames / 100.0)) + "% du temps.";
        LOGGER.log(CSV_OR_THOUSAND, winrate);
        String average = "Il a en moyenne " + totalScores.get(wantedPlayer.getName()) / numberOfGames + " points.";
        LOGGER.log(CSV_OR_THOUSAND, average);
    }
}