package xodr.exporter;

import xodr.map.MapDataContainer;
import xodr.map.entity.*;

import java.util.List;

public class BufferWriter {

    private static List<Road> roads;
    private static List<Junction> junctions;

    private static List<LaneSection> laneSections;
    private static List<Lane> lanes;
    private static List<Connection> connections;
    private static List<LaneLink> laneLinks;
    
    private static final int ROAD_LANESECTION = 10; // 一个road含有laneSection的数量
    private static final int LANESECTION_LANE = 10; // 一个laneSection含有lane的数量
    private static final int JUNCTION_CONNECTION = 10; // 一个junction含有connection的数量
    private static final int CONNECTION_LANELINK = 10; // 一个connection含有laneLink的数量
    private static final int K = 10; // 放大倍数

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
    
    private static void init(MapDataContainer container) {
        roads = container.getRoads();
        junctions = container.getJunctions();
        laneSections = container.getLaneSections();
        lanes = container.getLanes();
        connections = container.getConnections();
        laneLinks = container.getLaneLinks();
/*
        初始化index
        int countOfConnection = 0, countOfLane = 0, countOfLaneLink = 0, countOfLaneSection = 0;

        for(Road road : roads) {
            List<Integer> laneSectionsIndex = new ArrayList<>();
            for(LaneSection laneSection : road.getLaneSections()) {
                // 1. laneSections
                laneSections.add(laneSection);
                // 2. laneSectionsIndex
                laneSectionsIndex.add(countOfLaneSection++);
                // 3. lanes
                List<Integer> lanesIndex = new ArrayList<>();
                for(Lane lane : laneSection.getLanes()) {
                    // 1. lanes
                    lanes.add(lane);
                    // 2. lanesIndex
                    lanesIndex.add(countOfLane++);
                }
                laneSection.setLanesIndex(lanesIndex);
            }
            road.setLaneSectionsIndex(laneSectionsIndex);
        }

        for(Junction junction : junctions) {
            List<Integer> connectionsIndex = new ArrayList<>();
            for(Connection connection : junction.getConnections()) {
                // 1. connections
                connections.add(connection);
                // 2. connectionsIndex
                connectionsIndex.add(countOfConnection++);
                // 3. laneLinks
                List<Integer> laneLinksIndex = new ArrayList<>();
                for(LaneLink laneLink : connection.getLaneLinks()) {
                    // 1. laneLinks
                    laneLinks.add(laneLink);
                    // 2. laneLinksIndex
                    laneLinksIndex.add(countOfLaneLink++);
                }
                connection.setLaneLinksIndex(laneLinksIndex);
            }
            junction.setConnectionsIndex(connectionsIndex);
        }
*/

    }

    private static void addRoad(StringBuffer buffer) {
        buffer.append("Road roads[" + roads.size() + "] = {");
        for (Road road : roads) {
            // 开始road
            System.out.println(road.toString());
            buffer.append("{");

            buffer.append(road.getElementType() + ",");
            buffer.append(road.getRoadId() + ",");
            buffer.append(road.getJunctionIndex() + ",");
            buffer.append(road.getJunctionId() + ",");
            buffer.append(f(road.getLength()) + ",");
            buffer.append(road.getPredecessorElementType() + ",");
            buffer.append(road.getPredecessorIndex() + ",");
            buffer.append(road.getSuccessorElementType() + ",");
            buffer.append(road.getSuccessorIndex() + ",");
            buffer.append(f(road.getMaxSpeed()) + ",");

            // +laneSections索引
            buffer.append("{");
            // 存放索引
            List<Integer> laneSectionsIndex = road.getLaneSectionsIndex();
            int countOfLaneSection = Math.min(laneSectionsIndex.size(), ROAD_LANESECTION);
            buffer.append(laneSectionsIndex.get(0));
            for (int i = 1; i < countOfLaneSection; i++) {
                buffer.append("," + i);
            }
            // 空位填-1
            for (int i = countOfLaneSection; i < ROAD_LANESECTION; i++) {
                buffer.append("," + (-1));
            }

            buffer.append("}");
            // +laneSections

            buffer.append("}" + (road.getIndex()==roads.size()-1?"":",") + "\n");
            // 结束road
        }

        // roads结束
        buffer.append("};\n");

    }

    private static void addLaneSection(StringBuffer buffer) {
        buffer.append("LaneSection laneSections[" + laneSections.size() + "] = {");
        for (LaneSection laneSection : laneSections) {
            // +laneSection开始
            System.out.println(laneSection.toString());
            buffer.append("{");

            buffer.append(laneSection.getElementType() + ",");
            buffer.append(laneSection.getRoadIndex() + ",");
            buffer.append(laneSection.getRoadId() + ",");
            buffer.append(laneSection.getLaneSectionId() + ",");
            buffer.append(f(laneSection.getStartPosition()) + ",");

            // +lanes索引
            buffer.append("{");
            // 存放索引
            List<Integer> lanesIndex = laneSection.getLanesIndex();
            int countOfLane = Math.min(lanesIndex.size(), LANESECTION_LANE);
            buffer.append(lanesIndex.get(0));
            for (int i = 1; i < countOfLane; i++) {
                buffer.append("," + i);
            }
            // 空位填-1
            for (int i = countOfLane; i < LANESECTION_LANE; i++) {
                buffer.append("," + (-1));
            }

            buffer.append("}");
            // +lanes

            buffer.append("," + f(laneSection.getLength()));

            buffer.append("}" + (laneSection.getIndex()==laneSections.size()-1?"":",") + "\n");
            // +laneSection结束
        }

        buffer.append("};\n");
        // LaneSections结束
    }

    private static void addLane(StringBuffer buffer) {
        buffer.append("Lane lanes[" + lanes.size() + "] = {");
        for (Lane lane : lanes) {
            // +lane开始
            System.out.println(lane.toString());
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

            buffer.append("}" + (lane.getIndex()==lanes.size()-1?"":",") + "\n");
            // +lane结束
        }

        buffer.append("};\n");
        // LaneSections结束
    }

    private static void addJunction(StringBuffer buffer) {
        if(junctions.size() == 0) {
            buffer.append("Junction junctions[2];\n");
            return;
        }
        buffer.append("Junction junctions[" + junctions.size() + "] = {");
        for (Junction junction : junctions) {
            // 开始junction
            System.out.println(junction.toString());
            buffer.append("{");

            buffer.append(junction.getElementType() + ",");
            buffer.append(junction.getJunctionId() + ",");

            // +connections索引
            buffer.append("{");
            // 存放索引
            List<Integer> connectionsIndex = junction.getConnectionsIndex();
            int countOfConnection = Math.min(connectionsIndex.size(), JUNCTION_CONNECTION);
            buffer.append(connectionsIndex.get(0));
            for (int i = 1; i < countOfConnection; i++) {
                buffer.append("," + i);
            }
            // 空位填-1
            for (int i = countOfConnection; i < JUNCTION_CONNECTION; i++) {
                buffer.append("," + (-1));
            }

            buffer.append("}");
            // +connections

            buffer.append("}" + (junction.getIndex()==junctions.size()-1?"":",") + "\n");
            // 结束junction
        }

        // junctions结束
        buffer.append("};\n");
    }

    private static void addConnection(StringBuffer buffer) {
        if(connections.size() == 0) {
            buffer.append("Connection connections[2];\n");
            return;
        }
        buffer.append("Connection connections[" + connections.size() + "] = {");
        for (Connection connection : connections) {
            // 开始connection
            System.out.println(connection.toString());
            buffer.append("{");

            buffer.append(connection.getDirection() + ",");
            buffer.append(connection.getIncomingRoadId() + ",");
            buffer.append(connection.getConnectingRoadId() + ",");
            buffer.append(connection.getIncomingRoadIndex() + ",");
            buffer.append(connection.getConnectingRoadIndex() + ",");

            // +laneLinks索引
            buffer.append("{");
            // 存放索引
            List<Integer> laneLinksIndex = connection.getLaneLinksIndex();
            int countOfLaneLink = Math.min(laneLinksIndex.size(), CONNECTION_LANELINK);
            buffer.append(laneLinksIndex.get(0));
            for (int i = 1; i < countOfLaneLink; i++) {
                buffer.append("," + i);
            }
            // 空位填-1
            for (int i = countOfLaneLink; i < CONNECTION_LANELINK; i++) {
                buffer.append("," + (-1));
            }

            buffer.append("}");
            // +laneLinks

            buffer.append("}" + (connection.getIndex()==connections.size()-1?"":",") + "\n");
            // 结束connection
        }

        // connections结束
        buffer.append("};\n");
    }

    private static void addLaneLink(StringBuffer buffer) {
        if(laneLinks.size() == 0) {
            buffer.append("LaneLink laneLinks[2];\n");
            return;
        }
        buffer.append("LaneLink laneLinks[" + laneLinks.size() + "] = {");
        for (LaneLink laneLink : laneLinks) {
            // +laneLink开始
            System.out.println(laneLink.toString());
            buffer.append("{");

            buffer.append(laneLink.getFrom() + ",");
            buffer.append(laneLink.getTo());

            buffer.append("}" + (laneLink.getIndex()==laneLinks.size()-1?"":",") + "\n");
            // +laneLink结束
        }

        buffer.append("};\n");
        // LaneLinks结束
    }

    private static int f(double x) {
        return (int) Math.round(x * K);
    }
}
