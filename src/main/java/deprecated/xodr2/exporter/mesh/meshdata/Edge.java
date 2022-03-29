package deprecated.xodr2.exporter.mesh.meshdata;

public class Edge {

    private Vertex startVertex;
    private Vertex endVertex;

    /**
     * An Edge with a Start-{@link Vertex} and an End-{@link Vertex}
     * @param startVertex the first part of this edge
     * @param endVertex the second part of this edge
     */
    public Edge(Vertex startVertex, Vertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public Vertex getEndVertex() {
        return endVertex;
    }

}
