package fr.cotedazur.univ.polytech.startingpoint.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static fr.cotedazur.univ.polytech.startingpoint.utils.InGameLogger.LOGGER;

public class Csv {
    private Csv() {
        throw new IllegalStateException("Csv is a utility class");
    }


    public static void writeStats(List<String[]> args) {
        // Define the data for the CSV file
        // Specify the file path
        String csvFilePath = "src/main/resources/stats/gamestats.csv";
        // Create a File object to check if the file exists
        File file = new File(csvFilePath);
        if (!file.getParentFile().mkdirs()) {
            try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    for (String[] argList : args) {
                        // Check if the first column is "0"
                        if (nextLine.length > 0 && nextLine[0].equals(argList[0])) {
                            // Replace the second and third columns with "7" and "8"
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
                LOGGER.warning(e.toString());
            }
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath, true))) {
            resetStats();
            // Write the first line
            for (String[] tempList : args) {
                writer.writeNext(tempList);
            }
            LOGGER.warning("Values added to CSV file successfully!");
        } catch (IOException e) {
            LOGGER.warning(e.toString());
        }
    }

    public static void resetStats() {
        String csvFilePath = "src/main/resources/stats/gamestats.csv";
        // Create a File object to check if the file exists
        File file = new File(csvFilePath);
        file.getParentFile().mkdirs();
        String[] data = {"Nom du joueur ", "Algorithme du joueur", "Score Moyen", "Nombre de parties jouées", "Nombre de 1ere places", "Nombre de 2e places", "Nombre de 3e places", "Nombre de 4e places"};
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            writer.writeNext(data);
        } catch (IOException e) {
            LOGGER.warning(e.toString());
        }

    }
}