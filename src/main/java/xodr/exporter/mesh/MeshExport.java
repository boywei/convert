package xodr.exporter.mesh;

import xodr.exporter.mesh.meshdata.Mesh;

public class MeshExport {

    private Mesh roadMesh;
    private Mesh sidewalkMesh;
    private Mesh roadMarkMesh;

    public MeshExport(Mesh roadMesh, Mesh sidewalkMesh, Mesh roadMarkMesh) {
        this.roadMesh = roadMesh;
        this.sidewalkMesh = sidewalkMesh;
        this.roadMarkMesh = roadMarkMesh;
    }

    public Mesh getRoadMesh() {
        return roadMesh;
    }

    public Mesh getSidewalkMesh() {
        return sidewalkMesh;
    }

    public Mesh getRoadMarkMesh() {
        return roadMarkMesh;
    }
    
}
