package xodr.exporter;

import xodr.map.MapDataContainer;
import xodr.map.entity.*;

public class BufferWriter {

    private static Road[] roads;
    private static Junction[] junctions;

    private static LaneSection[] laneSections;
    private static Lane[] lanes;
    private static Connection[] connections;
    private static LaneLink[] laneLinks;

    private static int countOfRoad;
    private static int countOfLaneSection;
    private static int countOfLane;
    private static int countOfJunction;
    private static int countOfConnection;
    private static int countOfLaneLink;

    private static final int ROAD_LANESECTION = 10; // 一个road含有laneSection的数量
    private static final int LANESECTION_LANE = 10; // 一个laneSection含有lane的数量
    private static final int JUNCTION_CONNECTION = 10; // 一个junction含有connection的数量
    private static final int CONNECTION_LANELINK = 10; // 一个connection含有laneLink的数量

    public static void write(MapDataContainer container, StringBuffer buffer) {
        System.out.println("Writing map's declaration to file...");

        init(container);

        addRoad(buffer);
        addLaneSection(buffer);
        addLane(buffer);
        addJunction(buffer);
        addConnection(buffer);
        addLaneLink(buffer);

        System.out.println("Finishing Writing...");
    }

    // TODO: 初始化道路信息
    private static void init(MapDataContainer container) {
        roads = container.getRoads();
        junctions = container.getJunctions();



    }

    private static void addRoad(StringBuffer buffer) {
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

    private static void addLaneSection(StringBuffer buffer) {
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

    private static void addLane(StringBuffer buffer) {
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

    private static void addJunction(StringBuffer buffer) {
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

    private static void addConnection(StringBuffer buffer) {
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

    private static void addLaneLink(StringBuffer buffer) {
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

}
