package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading the CSV file using Java File I/O (BufferedReader).
 */
public class DataReader {

    // The main method for loading data from disk into memory
    public List<PatientRecord> loadFromCSV(String csvFilePath) throws IOException {
        List<PatientRecord> records = new ArrayList<>();

        // Use try-with-resources to ensure the file stream is automatically closed
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            String line = br.readLine(); // Skip header row
            if (line == null) {
                return records; // Handle empty file
            }

            while ((line = br.readLine()) != null) {
                // Split the line by comma
                String[] values = line.split(",");

                if (values.length == 4) {
                    try {
                        // Parse values and create the PatientRecord object

                        // CORRECTED: Was 'values.trim()' which is invalid.
                        // Should be values[0].trim()
                        String id = values[0].trim();

                        // CORRECTED: Was 'double[] age = Integer.parseInt(values.[1]trim());'
                        // 1. Changed type from double[] to double
                        // 2. Fixed syntax to values[1].trim()
                        double age = Integer.parseInt(values[1].trim());

                        // CORRECTED: Was 'values.[2]trim()'
                        double bmi = Double.parseDouble(values[2].trim());

                        // CORRECTED: Was 'values.[3]trim()'
                        String diagnosis = values[3].trim();

                        PatientRecord p = new PatientRecord(id, age, bmi, diagnosis);
                        records.add(p);

                    } catch (NumberFormatException e) {
                        // Log errors for data that can't be parsed
                        System.err.println("Skipping malformed row: " + line);
                    }
                }
            }
        }
        return records;
    }
}