package xodr.importer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import util.Uppaal;
import xodr.exporter.BufferWriter;
import xodr.map.ElementType;
import xodr.map.MapDataContainer;
import xodr.map.entity.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XODRParser {

    // 映射：id -> index
    private static Map<Integer, Integer> roadMap;
    private static Map<Integer, Integer> laneSectionMap;
    private static Map<Integer, Integer> laneMap;
    private static Map<Integer, Integer> junctionMap;

    // id: 分配的；roadId和junctionId本身自带
    private static int laneSectionId;
    private static int laneId;

    // index: 分配的
    private static int roadIndex;
    private static int laneSectionIndex;
    private static int laneIndex;
    private static int junctionIndex;
    private static int connectionIndex;
    private static int laneLinkIndex;

    // laneChange: type -> uppaal number
    private static final Map<String, Integer> laneChangeType = new HashMap<String, Integer>() {{
        put("increase", 1);
        put("decrease", 2);
        put("both", 3);
        put("none", 4);
    }};

    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer();
        String map = "src/main/resources/maps/Town02.xodr";
        // 1. 读取
        String input = XODRInputReader.readFromFile(map);
        // 2. 解析
        MapDataContainer container = XODRParser.parse(input);
        // 3. 写入buffer
        BufferWriter.write(container, buffer);
        System.out.println(buffer.toString());
    }

    public static MapDataContainer parse(String input) {
        System.out.println("Parsing input...");
        List<Road> roads = new ArrayList<>();
        List<Junction> junctions = new ArrayList<>();
        List<LaneSection> laneSections = new ArrayList<>();
        List<Lane> lanes = new ArrayList<>();
        List<Connection> connections = new ArrayList<>();
        List<LaneLink> laneLinks = new ArrayList<>();

        roadMap = new HashMap<>();
        laneSectionMap = new HashMap<>();
        laneMap = new HashMap<>();
        junctionMap = new HashMap<>();

        laneSectionId = 0;
        laneId = 0;

        roadIndex = 0;
        laneSectionIndex = 0;
        laneIndex = 0;
        junctionIndex = 0;
        connectionIndex = 0;
        laneLinkIndex = 0;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(input)));
            Element root = document.getDocumentElement();

            // 1. 解析road及子元素
            NodeList roadList = root.getElementsByTagName("road");
            parseRoad(roadList, roads, laneSections, lanes);

            // 2. 解析junction及子元素
            NodeList junctionList = root.getElementsByTagName("junction");
            parseJunction(junctionList, junctions, connections, laneLinks);
            
            // 3. 初始化index; 初始化connection的direction
            initIndex(roads, laneSections, lanes, junctions, connections, laneLinks);

            System.out.println("Finishing parsing...");
            return new MapDataContainer(roads, junctions, laneSections, lanes, connections, laneLinks);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            System.out.println("Error parsing OpenDRIVE file");
            return null;
        }

    }

    // 1 road
    private static void parseRoad(NodeList roadList, List<Road> roads, List<LaneSection> laneSections, List<Lane> lanes) {
        try {
            for(int i = 0; i < roadList.getLength(); i++){
                Element roadElement = (Element) roadList.item(i);
                Road road = new Road();

                //    int elementType;
                road.setElementType(ElementType.ROAD.getValue());
                //    int roadId;
                int roadId = Integer.parseInt(roadElement.getAttribute("id"));
                road.setRoadId(roadId);

                road.setIndex(roadIndex); // 初始化index
                roadMap.put(roadId, roadIndex++);

                //    int junctionId;
                int junctionId = Integer.parseInt(roadElement.getAttribute("junction"));
                road.setJunctionId(junctionId);
                //    int length;
                double length = Double.parseDouble(roadElement.getAttribute("length"));
                road.setLength(length);
                //    int predecessorElementType;
                //    int successorElementType;
                int predecessorId = -1;
                int successorId = -1;
                ElementType predecessorElementType;
                ElementType successorElementType;

                NodeList linkList = roadElement.getElementsByTagName("link");
                Element myLinkList =  (Element) linkList.item(0);

                NodeList predecessorList = myLinkList.getElementsByTagName("predecessor");
                if(predecessorList != null) {
                    Element predecessor = (Element) predecessorList.item(0);
                    if(predecessor != null) {
                        predecessorId = Integer.parseInt(predecessor.getAttribute("elementId"));
                        predecessorElementType = predecessor.getAttribute("elementType")
                                .equalsIgnoreCase("junction") ? ElementType.JUNCTION : ElementType.ROAD;
                        road.setPredecessorElementType(predecessorElementType.getValue());
                    }
                } else {
                    road.setPredecessorElementType(-1);
                }

                NodeList successorList = myLinkList.getElementsByTagName("successor");
                if(successorList != null) {
                    Element successor = (Element) successorList.item(0);
                    if(successor != null) {
                        successorId = Integer.parseInt(successor.getAttribute("elementId"));
                        successorElementType = successor.getAttribute("elementType")
                                .equalsIgnoreCase("junction") ? ElementType.JUNCTION : ElementType.ROAD;
                        road.setSuccessorElementType(successorElementType.getValue());
                    }
                } else {
                    road.setSuccessorElementType(-1);
                }

                road.setPredecessorId(predecessorId);
                road.setSuccessorId(successorId);

                //    int maxSpeed;
                NodeList typeNodeList = roadElement.getElementsByTagName("type");
                if(typeNodeList != null) {
                    Element typeElement = (Element) typeNodeList.item(0);
                    if(typeElement != null) {
                        NodeList speedNodeList = typeElement.getElementsByTagName("speed");
                        if(speedNodeList != null) {
                            Element speedElement = (Element) speedNodeList.item(0);
                            double maxSpeed = Double.parseDouble(speedElement.getAttribute("max"));
                            road.setMaxSpeed(maxSpeed);
                        }
                    }
                } else {
                    road.setMaxSpeed(Uppaal.INT16_MAX / Uppaal.K);
                }

                // laneSections
                NodeList lanesNodeList = roadElement.getElementsByTagName("lanes");
                Element lanesElement = (Element) lanesNodeList.item(0);
                NodeList laneSectionList = lanesElement.getElementsByTagName("laneSection");
                parseLaneSection(laneSectionList, road, laneSections, lanes);

                roads.add(road);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseLaneSection(NodeList laneSectionList, Road road, List<LaneSection> laneSections, List<Lane> lanes){
        try{
            List<LaneSection> roadLaneSections = new ArrayList<>();
            List<Integer> laneSectionsIndex = new ArrayList<>();
            for(int i = 0; i < laneSectionList.getLength(); i++) {
                Element laneSectionElement = (Element) laneSectionList.item(i);
                LaneSection laneSection = new LaneSection();

//                private int elementType;
                laneSection.setElementType(ElementType.LANE_SECTION.getValue());
//                private int roadIndex;
//                private int roadId;
                laneSection.setRoadId(road.getRoadId());
                laneSection.setRoadIndex(road.getIndex());

//                private int laneSectionId;
                laneSection.setLaneSectionId(laneSectionId);
                laneSection.setIndex(laneSectionIndex);
                laneSectionsIndex.add(laneSectionIndex);
                laneSectionMap.put(laneSectionId++, laneSectionIndex++);

//                private double startPosition;
                double s = Double.parseDouble(laneSectionElement.getAttribute("s"));
                laneSection.setStartPosition(s);

//                private double length;
                double length = 0; //上一个的长度：本车道段起始位置减去上一个的起始位置。
                laneSection.setLength(length);

                // lanes
                NodeList laneList = laneSectionElement.getElementsByTagName("lane");
                parseLane(laneList, laneSection, lanes);

                roadLaneSections.add(laneSection);
                laneSections.add(laneSection);
            }
            road.setLaneSectionsIndex(laneSectionsIndex);
            road.setLaneSections(roadLaneSections);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseLane(NodeList laneList, LaneSection laneSection, List<Lane> lanes){
        try{
            List<Lane> laneSectionLanes = new ArrayList<>();
            List<Integer> lanesIndex = new ArrayList<>();
            for(int i = 0; i < laneList.getLength(); i++) {
                Element laneElement = (Element) laneList.item(i);
                Lane lane = new Lane();

//                private int elementType;
                lane.setElementType(ElementType.LANE.getValue());
//                private int roadId;
                lane.setRoadId(laneSection.getRoadId());
//                private int roadIndex;
                lane.setRoadIndex(laneSection.getRoadIndex());
//                private int laneSectionIndex;
                lane.setLaneSectionIndex(laneSection.getIndex());
//                private int laneSectionId;
                lane.setLaneSectionId(laneSection.getLaneSectionId());
//                private int laneId;
                lane.setLaneId(Integer.parseInt(laneElement.getAttribute("id")));

                lane.setId(laneId); //上面的setLaneId中是相对位置，这里是标识符
                lane.setIndex(laneIndex);
                lanesIndex.add(laneIndex);
                laneMap.put(laneId++, laneIndex++);

//                private int type;
                String type = laneElement.getAttribute("type");
                if(type.equals("driving")) {
                    lane.setType(1);
                } else {
                    lane.setType(0);
                }
//                private int predecessorIndex;
//                private int predecessorLaneId; // 与laneId同类，表示相对位置
//                private int predecessorId; // 标识符
                NodeList predecessorList = laneElement.getElementsByTagName("predecessor");
                String predecessorLaneId;
                if(predecessorList != null) {
                    Element predecessorElement = (Element) predecessorList.item(0);
                    if(predecessorElement != null) {
                        predecessorLaneId = predecessorElement.getAttribute("id");
                        lane.setPredecessorLaneId(Integer.parseInt(predecessorLaneId));
                    }
                } else {
                    lane.setPredecessorLaneId(0); // 不存在
                }
//                private int successorIndex; 
//                private int successorLaneId;
//                private int successorId;
                NodeList successorList = laneElement.getElementsByTagName("successor");
                String successorLaneId;
                if(successorList != null) {
                    Element successorElement = (Element) successorList.item(0);
                    if(successorElement != null) {
                        successorLaneId = successorElement.getAttribute("id");
                        lane.setSuccessorLaneId(Integer.parseInt(successorLaneId));
                    }
                } else {
                    lane.setSuccessorLaneId(0); // 不存在
                }
//                private int laneChange;
                NodeList roadMarkList = laneElement.getElementsByTagName("roadMark");
                Element roadMarkElement = (Element) roadMarkList.item(0);
                String laneChange = roadMarkElement.getAttribute("laneChange");
                lane.setLaneChange(laneChangeType.getOrDefault(laneChange, 0));

                laneSectionLanes.add(lane);
                lanes.add(lane);
            }
            laneSection.setLanesIndex(lanesIndex);
            laneSection.setLanes(laneSectionLanes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2 junction
    private static void parseJunction(NodeList junctionList, List<Junction> junctions, List<Connection> connections, List<LaneLink> laneLinks) {
        try {
            for(int i = 0; i < junctionList.getLength(); i++) {
                Element junctionElement = (Element) junctionList.item(i);

                Junction junction = new Junction();
                // int elementType;
                junction.setElementType(ElementType.JUNCTION.getValue());
                // int junctionId;
                int junctionId = Integer.parseInt(junctionElement.getAttribute("id"));

                junction.setIndex(junctionIndex);
                junctionMap.put(junctionId, junctionIndex++);

                // connections;
                NodeList connectionList = junctionElement.getElementsByTagName("connection");
                parseConnection(connectionList, junction, connections, laneLinks);

                junctions.add(junction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseConnection(NodeList connectionList, Junction junction, List<Connection> connections, List<LaneLink> laneLinks) {
        try{
            List<Connection> junctionConnections = new ArrayList<>();
            List<Integer> connectionsIndex = new ArrayList<>();
            for(int i = 0; i < connectionList.getLength(); i++) {
                Element connectionElement = (Element) connectionList.item(i);

                Connection connection = new Connection();
                int incomingRoadId  = Integer.parseInt(connectionElement.getAttribute("incomingRoad"));
                int connectingRoadId  = Integer.parseInt(connectionElement.getAttribute("connectingRoad"));

                connection.setIncomingRoadId(incomingRoadId);
                connection.setConnectingRoadId(connectingRoadId);

                connection.setIndex(connectionIndex);
                connectionsIndex.add(connectionIndex++); // 上级junction中connection索引

                // laneLinks
                NodeList laneLinkList = connectionElement.getElementsByTagName("laneLink");
                parseLaneLink(laneLinkList, connection, laneLinks);

                connections.add(connection);
                junctionConnections.add(connection);
            }
            junction.setConnectionsIndex(connectionsIndex);
            junction.setConnections(junctionConnections);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseLaneLink(NodeList laneLinkList, Connection connection, List<LaneLink> laneLinks) {
        try{
            List<LaneLink> connectionLaneLinks = new ArrayList<>();
            List<Integer> laneLinksIndex = new ArrayList<>();
            for(int i = 0; i < laneLinkList.getLength(); i++) {
                Element laneLinkElement = (Element) laneLinkList.item(i);
                LaneLink laneLink = new LaneLink();

                laneLink.setFrom(Integer.parseInt(laneLinkElement.getAttribute("from")));
                laneLink.setTo(Integer.parseInt(laneLinkElement.getAttribute("to")));

                laneLink.setIndex(laneLinkIndex);
                laneLinksIndex.add(laneLinkIndex++); // 上级laneLinks索引

                connectionLaneLinks.add(laneLink);
                laneLinks.add(laneLink);
            }
            connection.setLaneLinksIndex(laneLinksIndex);
            connection.setLaneLinks(connectionLaneLinks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 初始化各结构的索引；初始化connection的direction
    private static void initIndex(List<Road> roads, List<LaneSection> laneSections, List<Lane> lanes,
                                  List<Junction> junctions, List<Connection> connections, List<LaneLink> laneLinks) {
        // road: junctionIndex, predecessorIndex, successorIndex
        initRoad(roads);
        // lane: predecessorIndex, successorIndex
        initLane(lanes);
        // connection: incomingRoadIndex, connectionRoadIndex; direction
        initConnection(connections, junctions, roads);
    }

    private static void initRoad(List<Road> roads) {
        // road: junctionIndex, predecessorIndex, successorIndex
        for(Road road : roads) {
            // junctionIndex
            road.setJunctionIndex(junctionMap.getOrDefault(road.getJunctionId(), -1));
            // predecessorIndex
            if(road.getPredecessorElementType() == 1) { // road
                road.setPredecessorIndex(roadMap.getOrDefault(road.getPredecessorId(), -1));
            } else if(road.getPredecessorElementType() == 4) { // junction
                road.setPredecessorIndex(junctionMap.getOrDefault(road.getPredecessorId(), -1));
            } else {
                road.setPredecessorIndex(-1);
            }
            // successorIndex
            if(road.getSuccessorElementType() == 1) { // road
                road.setSuccessorIndex(roadMap.getOrDefault(road.getSuccessorId(), -1));
            } else if(road.getSuccessorElementType() == 4) { // junction
                road.setSuccessorIndex(junctionMap.getOrDefault(road.getSuccessorId(), -1));
            } else {
                road.setSuccessorIndex(-1);
            }
        }
    }

    private static void initLane(List<Lane> lanes) {
        // TODO: lane: predecessorIndex, successorIndex

    }

    private static void initConnection(List<Connection> connections, List<Junction> junctions, List<Road> roads) {
        // connection: incomingRoadIndex, connectionRoadIndex
        for(Connection connection : connections) {
            connection.setIncomingRoadIndex(roadMap.getOrDefault(connection.getIncomingRoadId(), -1));
            connection.setConnectingRoadIndex(roadMap.getOrDefault(connection.getConnectingRoadId(), -1));
        }

        // 设置direction（这里可以优化）
        for(Road road : roads) {
            if(road.getJunctionId() != -1) {
                int direction = 1; // 1左转 2直行 3右转 其他都不正常忽略掉
                for(Junction junction : junctions) {
                    List<Connection> junctionConnections = junction.getConnections();
                    for(Connection connection : junctionConnections) {
                        if(connection.getConnectingRoadId() == road.getRoadId()) { // 作为连接路
                            connection.setDirection(direction++);
                        }
                    }
                    junction.setConnections(junctionConnections);
                }
                direction = 1; // 全局的connections也要初始化一遍
                for(Connection connection : connections) {
                    if(connection.getConnectingRoadId() == road.getRoadId()) {
                        connection.setDirection(direction++);
                    }
                }
            }
        }
    }

    
}
