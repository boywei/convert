package xodr.importer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
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
    private static Map<Integer, Integer> roadMap = new HashMap<>();
//    private static Map<Integer, Integer> laneSectionMap = new HashMap<>();
//    private static Map<Integer, Integer> laneMap = new HashMap<>();
    private static Map<Integer, Integer> junctionMap = new HashMap<>();
//    private static Map<Integer, Integer> connectionMap = new HashMap<>();
//    private static Map<Integer, Integer> laneLinkMap = new HashMap<>();

    private static int laneSectionId;

    public static MapDataContainer parse(String input) {
        System.out.println("Parsing input...");
        List<Road> roads = new ArrayList<>();
        List<Junction> junctions = new ArrayList<>();
        List<LaneSection> laneSections = new ArrayList<>();
        List<Lane> lanes = new ArrayList<>();
        List<Connection> connections = new ArrayList<>();
        List<LaneLink> laneLinks = new ArrayList<>();

        laneSectionId = 0;

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
                ElementType predecessorElementType = ElementType.ROAD;
                ElementType successorElementType = ElementType.ROAD;

                NodeList linkList = roadElement.getElementsByTagName("link");
                Element myLinkList =  (Element) linkList.item(0);

                NodeList predecessorList = myLinkList.getElementsByTagName("predecessor");
                if(predecessorList != null) {
                    Element predecessor = (Element) predecessorList.item(0);
                    if(predecessor != null) {
                        predecessorId = Integer.parseInt(predecessor.getAttribute("elementId"));
                        predecessorElementType = predecessor.getAttribute("elementType")
                                .equalsIgnoreCase("junction") ? ElementType.JUNCTION : ElementType.ROAD;
                    }
                }

                NodeList successorList = myLinkList.getElementsByTagName("successor");
                if(successorList != null) {
                    Element successor = (Element) successorList.item(0);
                    if(successor != null) {
                        successorId = Integer.parseInt(successor.getAttribute("elementId"));
                        successorElementType = successor.getAttribute("elementType")
                                .equalsIgnoreCase("junction") ? ElementType.JUNCTION : ElementType.ROAD;
                    }
                }

                road.setPredecessorId(predecessorId);
                road.setSuccessorId(successorId);
                road.setPredecessorElementType(predecessorElementType.getValue());
                road.setSuccessorElementType(successorElementType.getValue());
                //    int maxSpeed;
                NodeList typeNodeList = roadElement.getElementsByTagName("type");
                Element speedElement = (Element) typeNodeList.item(0);
                double maxSpeed = Double.parseDouble(speedElement.getAttribute("max"));
                road.setMaxSpeed(maxSpeed);

                //  TODO: road本身变量，各种索引

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
            double preS = 0.0;
            List<LaneSection> roadLaneSections = new ArrayList<>();
            for(int i = 0; i < laneSectionList.getLength(); i++) {
                Element laneSectionElement = (Element) laneSectionList.item(i);
                LaneSection laneSection = new LaneSection();

//                private int elementType;
                laneSection.setElementType(ElementType.LANE_SECTION.getValue());
//                private int roadIndex;
//                private int roadId;
                laneSection.setRoadId(road.getRoadId());
//                private int laneSectionId;
                laneSection.setLaneSectionId(laneSectionId++);
//                private double startPosition;
                double s = Double.parseDouble(laneSectionElement.getAttribute("s"));
                laneSection.setStartPosition(s);
//                private List<Integer> lanesIndex;
//                private List<Lane> lanes;
//                private double length;
                double length = s - preS; //上一个的长度：本车道段起始位置减去上一个的起始位置。TODO: 后期应往后移；length可以去掉？
                preS = s;
                laneSection.setLength(length);

                // lanes
                NodeList laneList = laneSectionElement.getElementsByTagName("lane");
                parseLane(laneList, laneSection, lanes);

                roadLaneSections.add(laneSection);
                laneSections.add(laneSection);
            }
            road.setLaneSections(roadLaneSections);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseLane(NodeList laneList, LaneSection laneSection, List<Lane> lanes){
        try{
            List<Lane> laneSectionLanes = new ArrayList<>();
            for(int i = 0; i < laneList.getLength(); i++) {
                Element laneElement = (Element) laneList.item(i);
                Lane lane = new Lane();

//                private int elementType;
                lane.setElementType(ElementType.LANE.getValue());
//                private int roadId;
                lane.setRoadId(laneSection.getRoadId());
//                private int roadIndex;
//                private int laneSectionIndex;
//                private int laneSectionId;
                lane.setLaneSectionId(laneSection.getLaneSectionId());
//                private int laneId;
                lane.setLaneId(Integer.parseInt(laneElement.getAttribute("id")));
//                private int type;
                String type = laneElement.getAttribute("type");
                if(type.equals("driving")) {
                    lane.setType(1);
                } else {
                    lane.setType(0);
                }
//                private int predecessorIndex;
//                private int predecessorLaneId; // 与laneId同类，表示相对位置
                NodeList predecessorList = laneElement.getElementsByTagName("predecessor");
                Element predecessorElement = (Element) predecessorList.item(0);
                String predecessorLaneId = predecessorElement.getAttribute("id");
                lane.setPredecessorLaneId(Integer.parseInt(predecessorLaneId));
//                private int predecessorId; // 标识符
//                private int successorIndex; 
//                private int successorLaneId;
//                private int successorId;
//                private int laneChange;
                laneSectionLanes.add(lane);
                lanes.add(lane);
            }
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
                // TODO: junction本身变量

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
            for(int i = 0; i < connectionList.getLength(); i++) {
                Element connectionElement = (Element) connectionList.item(i);

                Connection connection = new Connection();
                int incomingRoadId  = Integer.parseInt(connectionElement.getAttribute("incomingRoad"));
                int connectingRoadId  = Integer.parseInt(connectionElement.getAttribute("connectingRoad"));
                // TODO: connection本身变量
                
                // laneLinks
                NodeList laneLinkList = connectionElement.getElementsByTagName("laneLink");
                parseLaneLink(laneLinkList, connection, laneLinks);

                connections.add(connection);
                junctionConnections.add(connection);
            }
            junction.setConnections(junctionConnections);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseLaneLink(NodeList laneLinkList, Connection connection, List<LaneLink> laneLinks) {
        try{
            List<LaneLink> connectionLaneLinks = new ArrayList<>();
            for(int i = 0; i < laneLinkList.getLength(); i++) {
                Element laneLinkElement = (Element) laneLinkList.item(i);
                LaneLink laneLink = new LaneLink();
                // TODO: laneLink本身变量

                connectionLaneLinks.add(laneLink);
                laneLinks.add(laneLink);
            }
            connection.setLaneLinks(connectionLaneLinks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
