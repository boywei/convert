package xodr.exporter.mesh.meshdata;

import xodr.util.Position;

public class Vertex {

    private Position pos;

    /**
     * A Vertex is a simple Node with a Position
     * @param pos
     */
    public Vertex(Position pos) {
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }
}
