import json.exporter.XMLWriter;
import json.importer.JSONInputReader;
import json.importer.JSONParser;
import json.tree.TreeDataContainer;

public class Main {

    // "src/main/resources/ADSML/Example/" "example.model" "src/main/resources/models/Example.xml"
//    private final static String ADSML_PATH = "src/main/resources/ADSML/Example/";
//    private final static String JSON_PATH = ADSML_PATH + "example.model";
//    private final static String XML_PATH = "src/main/resources/models/Example.xml";

    public static void main(String[] args) {
        final String ADSML_PATH = args[0];
        final String JSON_PATH = ADSML_PATH + args[1];
        final String XML_PATH = args[2];

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
