package xodr;

import xodr.map.entity.*;

public class XodrConverter {

    private static final int ROAD_LANESECTION = 10; // 一个road含有laneSection的数量
    private static final int LANESECTION_LANE = 10; // 一个laneSection含有lane的数量
    private static final int JUNCTION_CONNECTION = 10; // 一个junction含有connection的数量
    private static final int CONNECTION_LANELINK = 10; // 一个connection含有laneLink的数量

    private Road[] roads;
    private LaneSection[] laneSections;
    private Lane[] lanes;
    private Junction[] junctions;
    private Connection[] connections;
    private LaneLink[] laneLinks;

    private int countOfRoad;
    private int countOfLaneSection;
    private int countOfLane;
    private int countOfJunction;
    private int countOfConnection;
    private int countOfLaneLink;

    public String convertMap(String mapPath) {

        StringBuffer buffer = new StringBuffer();

        init(mapPath);

        addRoad(buffer);
        addLaneSection(buffer);
        addLane(buffer);
        addJunction(buffer);
        addConnection(buffer);
        addLaneLink(buffer);

        return buffer.toString();
    }

    private void init(String mapPath) {

        // TODO: 根据地图文件位置解析地图
        countOfRoad = 1;
        countOfJunction = 1;

        roads = new Road[countOfRoad];
        laneSections = new LaneSection[countOfRoad * ROAD_LANESECTION];
        lanes = new Lane[countOfRoad * ROAD_LANESECTION * LANESECTION_LANE];
        junctions = new Junction[countOfJunction];
        connections = new Connection[countOfJunction * JUNCTION_CONNECTION];
        laneLinks = new LaneLink[countOfJunction * JUNCTION_CONNECTION * CONNECTION_LANELINK];

        // 对map初始化
    }

    private void addRoad(StringBuffer buffer) {
        buffer.append("Road roads[" + roads.length + "] = {");
        for (Road road : roads) {
            // 开始road
            buffer.append("{");

            buffer.append(road.getElementType() + ",");
            buffer.append(road.getRoadId() + ",");
            buffer.append(road.getJunctionIndex() + ",");
            buffer.append(road.getJunctionId() + ",");
            buffer.append(road.getRoadLengthIndex() + ",");
            buffer.append(road.getPredecessorElementType() + ",");
            buffer.append(road.getPredecessorIndex() + ",");
            buffer.append(road.getSuccessorElementType() + ",");
            buffer.append(road.getSuccessorIndex() + ",");
            buffer.append(road.getMaxSpeedIndex() + ",");

            // +laneSections索引
            buffer.append("{");
            int len = road.getLaneSections().length;
            // 存放索引
            buffer.append(countOfLaneSection);
            for (int j = 1; j < len; j++) {
                buffer.append("," + (countOfLaneSection + j));
            }
            countOfLaneSection += len; // 计算所有LaneSection的数量
            // 空位填-1
            for (int j = len; j < ROAD_LANESECTION; j++) {
                buffer.append("," + (-1));
            }

            buffer.append("}");
            // +laneSections

            buffer.append("}\n");
            // 结束road
        }

        // roads结束
        buffer.append("};\n");

    }

    private void addLaneSection(StringBuffer buffer) {
        buffer.append("LaneSection laneSections[" + countOfLaneSection + "] = {");
        for (LaneSection laneSection : laneSections) {
            // +laneSection开始
            buffer.append("{");

            buffer.append(laneSection.getElementType() + ",");
            buffer.append(laneSection.getRoadIndex() + ",");
            buffer.append(laneSection.getRoadId() + ",");
            buffer.append(laneSection.getLaneSectionId() + ",");
            buffer.append(laneSection.getStartPositionIndex() + ",");

            // +lanes索引
            buffer.append("{");
            int len = laneSection.getLanes().length;
            // 存放索引
            buffer.append(countOfLane);
            for (int j = 1; j < len; j++) {
                buffer.append("," + (countOfLane + j));
            }
            countOfLane += len; // 计算所有Lane的数量
            // 空位填-1
            for (int j = len; j < LANESECTION_LANE; j++) {
                buffer.append("," + (-1));
            }

            buffer.append("}");
            // +lanes

            buffer.append("," + laneSection.getLaneSectionLengthIndex());

            buffer.append("}\n");
            // +laneSection结束
        }

        buffer.append("};\n");
        // LaneSections结束
    }

    private void addLane(StringBuffer buffer) {
        buffer.append("Lane lanes[" + countOfLane + "] = {");
        for (Lane lane : lanes) {
            // +lane开始
            buffer.append("{");

            buffer.append(lane.getElementType() + ",");
            buffer.append(lane.getRoadId() + ",");
            buffer.append(lane.getRoadIndex() + ",");
            buffer.append(lane.getLaneSectionIndex() + ",");
            buffer.append(lane.getLaneId() + ",");
            buffer.append(lane.getType() + ",");
            buffer.append(lane.getPredecessorIndex() + ",");
            buffer.append(lane.getSuccessorIndex() + ",");
            buffer.append(lane.getLaneChange());

            buffer.append("}\n");
            // +lane结束
        }

        buffer.append("};\n");
        // LaneSections结束
    }

    private void addJunction(StringBuffer buffer) {
        buffer.append("Junction junctions[" + junctions.length + "] = {");
        for (Junction junction : junctions) {
            // 开始junction
            buffer.append("{");

            buffer.append(junction.getElementType() + ",");
            buffer.append(junction.getJunctionId() + ",");

            // +connections索引
            buffer.append("{");
            int len = junction.getConnections().length;
            // 存放索引
            buffer.append(countOfConnection);
            for (int j = 1; j < len; j++) {
                buffer.append("," + (countOfConnection + j));
            }
            countOfConnection += len; // 计算所有connection的数量
            // 空位填-1
            for (int j = len; j < JUNCTION_CONNECTION; j++) {
                buffer.append("," + (-1));
            }

            buffer.append("}");
            // +connections

            buffer.append("}\n");
            // 结束junction
        }

        // junctions结束
        buffer.append("};\n");
    }

    private void addConnection(StringBuffer buffer) {
        buffer.append("Connection connections[" + connections.length + "] = {");
        for (Connection connection : connections) {
            // 开始connection
            buffer.append("{");

            buffer.append(connection.getIncomingRoadId() + ",");
            buffer.append(connection.getConnectingRoadId() + ",");
            buffer.append(connection.getIncomingRoadIndex() + ",");
            buffer.append(connection.getConnectingRoadIndex() + ",");

            // +laneLinks索引
            buffer.append("{");
            int len = connection.getLaneLinks().length;
            // 存放索引
            buffer.append(countOfLaneLink);
            for (int j = 1; j < len; j++) {
                buffer.append("," + (countOfLaneLink + j));
            }
            countOfLaneLink += len; // 计算所有laneLink的数量
            // 空位填-1
            for (int j = len; j < CONNECTION_LANELINK; j++) {
                buffer.append("," + (-1));
            }

            buffer.append("}");
            // +laneLinks

            buffer.append("}\n");
            // 结束connection
        }

        // connections结束
        buffer.append("};\n");
    }

    private void addLaneLink(StringBuffer buffer) {
        buffer.append("LaneLink laneLinks[" + countOfLaneLink + "] = {");
        for (LaneLink laneLink : laneLinks) {
            // +laneLink开始
            buffer.append("{");

            buffer.append(laneLink.getFrom() + ",");
            buffer.append(laneLink.getTo());

            buffer.append("}\n");
            // +laneLink结束
        }

        buffer.append("};\n");
        // LaneLinks结束
    }

    public Road[] getRoads() {
        return roads;
    }

    public void setRoads(Road[] roads) {
        this.roads = roads;
    }

    public LaneSection[] getLaneSections() {
        return laneSections;
    }

    public void setLaneSections(LaneSection[] laneSections) {
        this.laneSections = laneSections;
    }

    public Lane[] getLanes() {
        return lanes;
    }

    public void setLanes(Lane[] lanes) {
        this.lanes = lanes;
    }

    public Junction[] getJunctions() {
        return junctions;
    }

    public void setJunctions(Junction[] junctions) {
        this.junctions = junctions;
    }

    public Connection[] getConnections() {
        return connections;
    }

    public void setConnections(Connection[] connections) {
        this.connections = connections;
    }

    public LaneLink[] getLaneLinks() {
        return laneLinks;
    }

    public void setLaneLinks(LaneLink[] laneLinks) {
        this.laneLinks = laneLinks;
    }

}


