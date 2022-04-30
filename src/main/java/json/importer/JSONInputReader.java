package json.importer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
public class JSONInputReader {

    public static String readFromFile(String JSONPath) {
        log.info("开始解析JSON文件：{}...", JSONPath);

        String jsonStr = null;
        try {
            jsonStr = FileUtils.readFileToString(new File(JSONPath), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("完成解析JSON!");
        return jsonStr;
    }

}
