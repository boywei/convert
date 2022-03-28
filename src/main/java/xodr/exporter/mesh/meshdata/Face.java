package xodr.exporter.mesh.meshdata;

import java.util.List;

public class Face {

    private List<Vertex> vertices;

    /**
     * A Face of Vertices
     * @param vertices the given edges of the face
     */
    public Face(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
}
