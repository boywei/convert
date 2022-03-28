package xodr;

import xodr.data.OpenDriveDataContainer;
import xodr.exporter.ExportData;
import xodr.exporter.file.FileExporter;
import xodr.exporter.file.obj.ObjExporter;
import xodr.exporter.mesh.MeshExport;
import xodr.exporter.mesh.MeshExporter;
import xodr.importer.FileInputReader;
import xodr.importer.XMLParser;

public class XodrMain {

    public final static String IMPORT_PATH = "samples/sample.xodr";
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
