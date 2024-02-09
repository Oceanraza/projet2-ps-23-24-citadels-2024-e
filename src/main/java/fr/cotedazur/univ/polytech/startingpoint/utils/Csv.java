package fr.cotedazur.univ.polytech.startingpoint.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import fr.cotedazur.univ.polytech.startingpoint.exception.CSVFileProcessingException;
import fr.cotedazur.univ.polytech.startingpoint.exception.CSVWriteException;
import fr.cotedazur.univ.polytech.startingpoint.exception.StatsResetException;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

public class Csv {
    private Csv() {
        throw new IllegalStateException("Csv is a utility class");
    }


    public static void writeStats(List<String[]> args)  {
        // Define the data for the CSV file
        // Specify the file path
        String csvFilePath = "src/main/resources/stats/gamestats.csv";
        // Create a File object to check if the file exists
        File file = new File(csvFilePath);
        if (!file.getParentFile().mkdirs()){
            try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    for (String[] argList : args){
                        // Check if the first column is "0"
                        if (nextLine.length > 0 && nextLine[0].equals(argList[0])) {
                            // Replace the second and third columns with "7" and "8"
                            for (int i = 2; i < 8; i++){
                                int newVal = Integer.parseInt(argList[i]) + Integer.parseInt(nextLine[i]);
                                if (i == 2){
                                    newVal = newVal / 2;
                                }
                                argList[i] = Integer.toString(newVal);
                            }
                        }
                    }

                }
            } catch (IOException | CsvValidationException e){
                //throw new CSVFileProcessingException("An error occurred while processing the file: " + e.getMessage());
            }
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath,true))) {
            resetStats();
            // Write the first line
            for (String[] tempList : args){
                writer.writeNext(tempList);
            }
            LOGGER.log(CSV_OR_THOUSAND, COLOR_GREEN + "\nValeurs ajoutees au fichier CSV avec succes !" + COLOR_RESET);
        } catch (IOException e) {
            //throw new CSVWriteException("Error while writing csv file : " + e.getMessage());
        }
    }
    public static void resetStats(){
        String csvFilePath = "src/main/resources/stats/gamestats.csv";
        // Create a File object to check if the file exists
        File file = new File(csvFilePath);
        if(file.getParentFile().mkdirs()) {
            LOGGER.log(CSV_OR_THOUSAND, COLOR_GREEN + "Le fichier n'existait pas, il a ete cree avec succes !" + COLOR_RESET);
        }
        String[] data = {"Nom du joueur ","Algorithme du joueur" ,"Score Moyen", "Nombre de parties jouées", "Nombre de 1ere places", "Nombre de 2e places","Nombre de 3e places","Nombre de 4e places"};
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            writer.writeNext(data);
        } catch (IOException e) {
            //throw new StatsResetException("Error resetting statistics : " + e.getMessage());
        }
    }

    public static void printPlayerInfo(Map<String, Integer> totalScores, Map<String, List<Integer>> totalPlacements, Player wantedPlayer, int numberOfGames) {
        double scoreJoueur = totalPlacements.get(wantedPlayer.getName()).get(0);
        String winrate = wantedPlayer.getName() + " a gagne un total de " + (int) scoreJoueur + " parties.\nIl gagne donc " + (scoreJoueur / (numberOfGames/100.0)) + "% du temps.";
        LOGGER.log(CSV_OR_THOUSAND, winrate);
        String average = "Il a en moyenne " + totalScores.get(wantedPlayer.getName())/numberOfGames + " points.";
        LOGGER.log(CSV_OR_THOUSAND, average);
    }
}