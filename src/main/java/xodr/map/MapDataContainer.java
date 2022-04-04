package xodr.map;

import xodr.map.entity.*;

import java.util.List;

public class MapDataContainer {

    private List<Road> roads;
    private List<Junction> junctions;
    private List<LaneSection> laneSections;
    private List<Lane> lanes;
    private List<Connection> connections;
    private List<LaneLink> laneLinks;

    public MapDataContainer(List<Road> roads, List<Junction> junctions, List<LaneSection> laneSections, List<Lane> lanes, List<Connection> connections, List<LaneLink> laneLinks) {
        this.roads = roads;
        this.junctions = junctions;
        this.laneSections = laneSections;
        this.lanes = lanes;
        this.connections = connections;
        this.laneLinks = laneLinks;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public void setRoads(List<Road> roads) {
        this.roads = roads;
    }

    public List<Junction> getJunctions() {
        return junctions;
    }

    public void setJunctions(List<Junction> junctions) {
        this.junctions = junctions;
    }

    public List<LaneSection> getLaneSections() {
        return laneSections;
    }

    public void setLaneSections(List<LaneSection> laneSections) {
        this.laneSections = laneSections;
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public List<LaneLink> getLaneLinks() {
        return laneLinks;
    }

    public void setLaneLinks(List<LaneLink> laneLinks) {
        this.laneLinks = laneLinks;
    }
}
