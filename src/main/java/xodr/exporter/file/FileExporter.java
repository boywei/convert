package xodr.exporter.file;

import xodr.exporter.ExportData;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * This Class handles the exporting of {@link ExportData} into the corresponding Files
 */
public class FileExporter {

    /**
     * This method exports the given {@link ExportData} into the corresponding files
     * @param exportData the data to export
     * @return whether the export was successful
     */
    public static boolean exportToFiles(ExportData exportData) {

        System.out.println("Exporting to '" + exportData.getExportPath() + "'");

        boolean successful = true;

        if(!exportToFile(exportData.getRoadsExportPath(), exportData.getRoadsExportString())){
            System.out.println("Exporting roads failed");
            successful = false;
        }
        if(!exportToFile(exportData.getSidewalkExportPath(), exportData.getSidewalkExportString())){
            System.out.println("Exporting sidewalk failed");
            successful = false;
        }
        if(!exportToFile(exportData.getRoadMarkExportPath(), exportData.getRoadMarkExportString())){
            System.out.println("Exporting road markings failed");
            successful = false;
        }

        if(successful){
            System.out.println("Export successful");
        } else {
            System.out.println("Export failed");
        }

        return false;
    }

    /**
     * This method writes content into a file
     * @param fileName the name of the file to write into
     * @param content the content to write into the file
     * @return whether the content could be written into the file
     */
    public static boolean exportToFile(String fileName, String content) {

        try {
            PrintWriter pw = new PrintWriter(fileName);
            pw.println(content);
            pw.flush();
            pw.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

}
