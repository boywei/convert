import json.exporter.XMLWriter;
import json.importer.JSONInputReader;
import json.importer.JSONParser;
import json.tree.TreeDataContainer;

public class Main {

    public final static String JSON_PATH = "src/main/resources/ADSML/overtake/overtake.json";
    public final static String XML_PATH = "src/main/resources/models/overtake.xml";
//    public final static String JSON_PATH = "src/main/resources/examples/test.json";
//    public final static String XML_PATH = "src/main/resources/models/test.xml";

    public static void main(String[] args) {

        try{
            // 1. 读取
            String input = JSONInputReader.readFromFile(JSON_PATH);
            // 2. 解析
            TreeDataContainer container = JSONParser.parse(input);
            // 3. 写入
            XMLWriter.write(container, XML_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
