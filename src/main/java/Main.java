import json.exporter.XMLWriter;
import json.importer.JSONInputReader;
import json.importer.JSONParser;
import json.tree.TreeDataContainer;

public class Main {

    private final static String ADSML_PATH = "src/main/resources/ADSML/overtake/";
    private final static String JSON_PATH = ADSML_PATH + "overtake.json";
    private final static String XML_PATH = "src/main/resources/models/overtake1.xml";

    public static void main(String[] args) {

        try{
            // 1. 读取
            String input = JSONInputReader.readFromFile(JSON_PATH);
            // 2. 解析
            TreeDataContainer container = JSONParser.parse(input, ADSML_PATH);
            // 3. 写入
            XMLWriter.write(container, XML_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
