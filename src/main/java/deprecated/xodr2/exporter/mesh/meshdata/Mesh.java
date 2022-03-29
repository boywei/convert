package deprecated.xodr2.exporter.mesh.meshdata;

import java.util.ArrayList;
import java.util.List;

public class Mesh {

    private List<Vertex> vertices;
    private List<Face> faces;

    /**
     * A Mesh is the combination of vertices with faces
     */
    public Mesh() {
        this.vertices = new ArrayList<>();
        this.faces = new ArrayList<>();
    }

    /**
     * A Mesh is the combination of vertices with faces
     */
    public Mesh(List<Vertex> vertices, List<Face> faces) {
        this.vertices = vertices;
        this.faces = faces;
    }

    public void add(Mesh mesh){
        vertices.addAll(mesh.getVertices());
        faces.addAll(mesh.getFaces());
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Face> getFaces() {
        return faces;
    }
}
