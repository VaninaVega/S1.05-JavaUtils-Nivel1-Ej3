import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a directory path as an argument.");
            return;
        }

        String directoryPath = args[0];
        File directory = new File(directoryPath);
        File outputFile = new File("resultado.txt"); //me lo guarda en txt

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            listContent(directory, " ", writer);
            System.out.println("Directory listing has been saved in resultado.txt");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void listContent(File directory, String tabulador, BufferedWriter writer) throws IOException {
        tabulador += "   ";

        if (!directory.exists() || !directory.isDirectory()) {
            writer.write("Specified directory does not exist or is invalid: " + directory.getAbsolutePath());
            writer.newLine();
            return;
        }

        File[] content = directory.listFiles();

        if (content == null || content.length == 0) {
            writer.write("Directory is empty: " + directory.getAbsolutePath());
            writer.newLine();
        } else {
            // Ordenamos alfabéticamente por nombre
            Arrays.sort(content, Comparator.comparing(File::getName));

            // Formato de fecha
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (File f : content) {
                String lastModified = dateFormat.format(f.lastModified());
                String line;

                if (f.isDirectory()) {
                    line = tabulador + "[D] " + f.getName() + " (Last modified: " + lastModified + ")";
                    writer.write(line);
                    writer.newLine();
                    listContent(f, tabulador, writer); // Llamada recursiva para subdirectorios
                } else {
                    line = tabulador + "[F] " + f.getName() + " (Last modified: " + lastModified + ")";
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }
}