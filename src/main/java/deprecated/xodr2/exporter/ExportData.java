package deprecated.xodr2.exporter;

public class ExportData {

    private String exportPath;
    private String fileEnding;
    private String roadsExportString;
    private String sidewalkExportString;
    private String roadMarkExportString;

    public ExportData(String exportPath, String fileEnding, String roadsExportString, String sidewalkExportString, String roadMarkExportString) {
        this.exportPath = exportPath;
        this.fileEnding = fileEnding;
        this.roadsExportString = roadsExportString;
        this.sidewalkExportString = sidewalkExportString;
        this.roadMarkExportString = roadMarkExportString;
    }

    public String getExportPath() {
        return exportPath;
    }

    public String getFileEnding() {
        return fileEnding;
    }

    public String getRoadsExportString() {
        return roadsExportString;
    }

    public String getRoadsExportPath() {
        return exportPath + "_roads" + fileEnding;
    }

    public String getSidewalkExportString() {
        return sidewalkExportString;
    }

    public String getSidewalkExportPath() {
        return exportPath + "_sidewalk" + fileEnding;
    }

    public String getRoadMarkExportString() {
        return roadMarkExportString;
    }

    public String getRoadMarkExportPath() {
        return exportPath + "_road_markings" + fileEnding;
    }

}
