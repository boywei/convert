package xodr.importer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class XODRInputReader {

    public static String readFromFile(String xodrPath) {
        System.out.println("Reading OpenDRIVE file: " + xodrPath + "...");

        String xodrStr = null;
        try {
            xodrStr = FileUtils.readFileToString(new File(xodrPath), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finish reading OpenDRIVE... ");
        return xodrStr;
    }


}
