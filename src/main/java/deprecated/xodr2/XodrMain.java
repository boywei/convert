package deprecated.xodr2;

import deprecated.xodr2.data.OpenDriveDataContainer;
import deprecated.xodr2.exporter.ExportData;
import deprecated.xodr2.exporter.file.FileExporter;
import deprecated.xodr2.exporter.mesh.MeshExport;
import deprecated.xodr2.exporter.mesh.MeshExporter;
import deprecated.xodr2.importer.FileInputReader;
import deprecated.xodr2.importer.XMLParser;
import deprecated.xodr2.exporter.file.obj.ObjExporter;

public class XodrMain {

    public final static String IMPORT_PATH = "samples/sample.xodr2";
    public final static String EXPORT_PATH = "Result/sample";

    public static void main(String[] args) {

        String input = FileInputReader.readFromFile(IMPORT_PATH);
        if(input != null){

            OpenDriveDataContainer con = XMLParser.parse(input);
            if(con != null){

                MeshExport meshExport = MeshExporter.export(con);
                ExportData exportData = ObjExporter.export(EXPORT_PATH, meshExport);

                FileExporter.exportToFiles(exportData);

            }

        }

    }

}
