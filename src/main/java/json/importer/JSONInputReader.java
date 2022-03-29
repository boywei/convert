package json.importer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JSONInputReader {

    public static String readFromFile(String JSONPath) {
        System.out.println("Reading JSON file: " + JSONPath + "...");

        String jsonStr = null;
        try {
            jsonStr = FileUtils.readFileToString(new File(JSONPath), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finish reading JSON... ");
        return jsonStr;
    }

}
