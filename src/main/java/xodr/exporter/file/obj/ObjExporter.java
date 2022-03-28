package xodr.exporter.file.obj;

import xodr.exporter.ExportData;
import xodr.exporter.mesh.MeshExport;
import xodr.exporter.mesh.meshdata.Face;
import xodr.exporter.mesh.meshdata.Mesh;
import xodr.exporter.mesh.meshdata.Vertex;
import xodr.util.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * This Class handles exporting Meshes into Objects
 */
public class ObjExporter {

    /**
     * This method creates an {@link ExportData}-Object from an Export-Path and a {@link MeshExport}
     * @param exportPath the path to the core file to export to
     * @param meshExport the {@link MeshExport} to export
     * @return
     */
    public static ExportData export(String exportPath, MeshExport meshExport) {

        String roadMeshExportString = ObjExporter.exportMesh(meshExport.getRoadMesh());
        String sidewalkMeshExportString = ObjExporter.exportMesh(meshExport.getSidewalkMesh());
        String roadMarkMeshExportString = ObjExporter.exportMesh(meshExport.getRoadMarkMesh());

        ExportData exportData = new ExportData(exportPath, ".obj", roadMeshExportString, sidewalkMeshExportString, roadMarkMeshExportString);
        return exportData;
    }

    /**
     * This method encodes a {@link Mesh} into a {@link String}
     * @param mesh the mesh to encode
     * @return the encoded mesh as a {@link String}
     */
    private static String exportMesh(Mesh mesh) {

        Map<Vertex, Integer> vertexIDMap = new HashMap<>();

        String s = "";
        s += "o Object1\n";

        int vertexID = 1;
        for(Vertex v:mesh.getVertices()) {
            Position pos = v.getPos();
            s += "v " + pos.getX() + " " + pos.getZ() + " " + (-pos.getY()) +  "\n";
            vertexIDMap.put(v, vertexID);
            vertexID++;
        }

        for(Face f:mesh.getFaces()) {
            String faceString = "";
            for(Vertex v:f.getVertices()){
                faceString += vertexIDMap.get(v) + " ";
            }
            s += "f " + faceString + "\n";
        }

        return s;
    }

}
