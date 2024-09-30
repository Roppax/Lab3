package org.translation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileAggregator {

    // Define the output text file path
    private static final String OUTPUT_FILE = "aggregated_output.txt";

    public static void main(String[] args) {
        // Define the directory where your source code resides (e.g., src)
        String srcDir = "src"; // You can change this path to match your src directory
        File directory = new File(srcDir);

        try {
            // Start the process by cleaning up or creating the output file
            new FileWriter(OUTPUT_FILE, false).close();  // Clear the file if it exists
            // Traverse the directory recursively and process each file
            traverseAndCopy(directory);
            System.out.println("All files from src have been copied to " + OUTPUT_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to traverse directories and copy file contents
    private static void traverseAndCopy(File directory) throws IOException {
        // Get all files in the directory
        File[] files = directory.listFiles();

        // If directory is empty or does not exist, return
        if (files == null) return;

        // Iterate through all files and sub-directories
        for (File file : files) {
            if (file.isDirectory()) {
                // If it's a directory, recurse into it
                traverseAndCopy(file);
            } else {
                // If it's a file, copy its content to the output file
                copyFileContent(file);
            }
        }
    }

    // Function to copy the content of a single file
    private static void copyFileContent(File file) throws IOException {
        // Ensure it's a file (not a directory) and has readable content
        if (file.isFile()) {
            System.out.println("Copying content from: " + file.getName());
            List<String> lines = Files.readAllLines(Paths.get(file.getPath()));

            // Write the content of the file to the output file
            try (FileWriter writer = new FileWriter(OUTPUT_FILE, true)) {
                writer.write("---- Content from file: " + file.getName() + " ----\n");
                for (String line : lines) {
                    writer.write(line + "\n");
                }
                writer.write("\n");
            }
        }
    }
}


