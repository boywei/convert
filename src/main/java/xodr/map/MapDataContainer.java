package xodr.map;

import xodr.map.entity.Junction;
import xodr.map.entity.Road;

public class MapDataContainer {

    private Road[] roads;
    private Junction[] junctions;

    public MapDataContainer(Road[] roads, Junction[] junctions) {
        this.roads = roads;
        this.junctions = junctions;
    }

    public Road[] getRoads() {
        return roads;
    }

    public void setRoads(Road[] roads) {
        this.roads = roads;
    }

    public Junction[] getJunctions() {
        return junctions;
    }

    public void setJunctions(Junction[] junctions) {
        this.junctions = junctions;
    }
}
