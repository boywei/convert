package json.importer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
