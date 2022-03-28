package xodr.importer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileInputReader {

    public static String readFromFile(String filePath) {

        System.out.println("Reading input file: " + filePath);

        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            String line = "";
            while((line = br.readLine()) != null){
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading input file");
            return null;
        }

        String content = stringBuilder.toString();
        return content;

    }

}
