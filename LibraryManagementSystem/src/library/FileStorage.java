package library;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileStorage.java
 * Provides basic, reusable file handling used by all the manager
 * classes. Uses FileReader/BufferedReader for reading and
 * FileWriter/BufferedWriter for writing, as permitted by the
 * assignment's file handling guidelines. Data is stored as
 * plain text files inside the "data" folder.
 */
public class FileStorage {

    private static final String DATA_FOLDER = "data";

    /** Makes sure the data folder and the given file both exist before use. */
    public void ensureFileExists(String fileName) {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create data file: " + fileName);
            }
        }
    }

    /** Reads every line of the given data file into a list of strings. */
    public List<String> readAllLines(String fileName) {
        List<String> lines = new ArrayList<>();
        ensureFileExists(fileName);
        File file = new File(DATA_FOLDER, fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file " + fileName + ": " + e.getMessage());
        }
        return lines;
    }

    /** Overwrites the given data file with the supplied list of lines. */
    public void writeAllLines(String fileName, List<String> lines) {
        ensureFileExists(fileName);
        File file = new File(DATA_FOLDER, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file " + fileName + ": " + e.getMessage());
        }
    }

    /** Appends a single line to the end of the given data file. */
    public void appendLine(String fileName, String line) {
        ensureFileExists(fileName);
        File file = new File(DATA_FOLDER, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error appending to file " + fileName + ": " + e.getMessage());
        }
    }
}
