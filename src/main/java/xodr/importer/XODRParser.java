package xodr.importer;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import util.UppaalUtil;
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

@Slf4j
public class XODRParser {

    // 映射：id -> index
    private static Map<Integer, Integer> roadMap;
    private static Map<Integer, Integer> laneSectionMap;
    private static Map<Integer, Integer> laneMap;
    private static Map<Integer, Integer> junctionMap;

    // index: 分配的
    private static int roadIndex;
    private static int laneSectionIndex;
    private static int laneIndex;
    private static int junctionIndex;
    private static int connectionIndex;
    private static int laneLinkIndex;

    // id: 分配的标识符，在这里id和index一致了；roadId和junctionId自身就有，不用分配；connection和laneLink用不着
    private static int laneSectionId;
    private static int laneSingleId;

    // laneChange: type -> uppaal number
    private static final Map<String, Integer> laneChangeType = new HashMap<String, Integer>() {{
        put("increase", 1);
        put("decrease", 2);
        put("both", 3);
        put("none", 4);
    }};

    public static MapDataContainer parse(String input) {
        log.info("开始解析地图...");
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
        laneSingleId = 0;

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

            log.info("解析地图完成！");
            return new MapDataContainer(roads, junctions, laneSections, lanes, connections, laneLinks);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            log.error("解析OpenDRIVE文件时发生错误！");
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
                ElementType predecessorElementType = ElementType.NONE;
                ElementType successorElementType = ElementType.NONE;

                NodeList linkList = roadElement.getElementsByTagName("link");
                Element myLinkList =  (Element) linkList.item(0);
                NodeList predecessorList = null, successorList = null;
                if (myLinkList != null) {
                    predecessorList = myLinkList.getElementsByTagName("predecessor");
                    successorList = myLinkList.getElementsByTagName("successor");
                }

                if(predecessorList != null) {
                    Element predecessor = (Element) predecessorList.item(0);
                    if(predecessor != null) {
                        String elementId = predecessor.getAttribute("elementId");
                        String elementType = predecessor.getAttribute("elementType");
                        if(elementId != null && elementType != null && elementId.length() != 0 && elementType.length() != 0) {
                            predecessorId = Integer.parseInt(elementId);
                            predecessorElementType = elementType.equalsIgnoreCase("junction")
                                    ? ElementType.JUNCTION : ElementType.ROAD;
                            road.setPredecessorElementType(predecessorElementType.getValue());
                        }
                    }
                }
                road.setPredecessorElementType(predecessorElementType.getValue());
                road.setPredecessorId(predecessorId);

                if(successorList != null) {
                    Element successor = (Element) successorList.item(0);
                    if(successor != null) {
                        String elementId = successor.getAttribute("elementId");
                        String elementType = successor.getAttribute("elementType");
                        if(elementId != null && elementType != null && elementId.length() != 0 && elementType.length() != 0) {
                            successorId = Integer.parseInt(elementId);
                            successorElementType = elementType.equalsIgnoreCase("junction")
                                    ? ElementType.JUNCTION : ElementType.ROAD;
                        }
                    }
                }
                road.setSuccessorElementType(successorElementType.getValue());
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
                    road.setMaxSpeed(UppaalUtil.INT16_MAX * 1.0 / UppaalUtil.K);
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
                double length = 0; // 本车道段起始位置减去上一个的起始位置
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

                lane.setSingleId(laneSingleId); //上面的setLaneId中是相对位置，这里是标识符
                lane.setIndex(laneIndex);
                lanesIndex.add(laneIndex);
                laneMap.put(laneSingleId++, laneIndex++);

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
                }

//                private int laneChange;
                NodeList roadMarkList = laneElement.getElementsByTagName("roadMark");
                Element roadMarkElement = (Element) roadMarkList.item(0);
                String laneChange = roadMarkElement.getAttribute("laneChange");
                lane.setLaneChange(laneChangeType.getOrDefault(laneChange, 0));

                // width
                NodeList widthList = laneElement.getElementsByTagName("width");
                Element widthElement = (Element) widthList.item(0);
                String width = "0.0";
                if(widthElement != null) { // 不是所有的lane都有width，比如center（中心线）没有
                    width = widthElement.getAttribute("a");
                }
                lane.setWidth(Double.parseDouble(width));

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
        // road: 需要初始化junctionIndex, predecessorIndex, successorIndex
        initRoad(roads);
        // lane: 需要初始化predecessorIndex, successorIndex
        initLane(lanes, laneSections, roads);
        // connection: 需要初始化incomingRoadIndex, connectionRoadIndex; direction
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

    private static void initLane(List<Lane> lanes, List<LaneSection> laneSections, List<Road> roads) {
        log.warn("Lane的前驱后继索引初始化尚未完成：跨road部分");
        for(Lane lane : lanes) {
            Road currentRoad = roads.get(lane.getRoadIndex());
            LaneSection currentLaneSection = laneSections.get(lane.getLaneSectionIndex());

            /*
                前驱：
                1 当前laneSection不是当前road的第一个车道段，那么
                - 当前laneSection的index-1，即前一个laneSection，找到对应的predecessorLaneId即可
                2 当前laneSection是当前road的第一个车道段，那么
                - 找到当前road的前驱road，根据两条road的连接方式判断predecessorLaneId属于前驱road的第一个还是最后一个车道段
            */
            if(lane.getPredecessorLaneId() != 0) {
                LaneSection preLaneSection = null;
                if(currentLaneSection.getStartPosition() != 0.0) { // 不是第一个车道段
                    preLaneSection = laneSections.get(currentLaneSection.getIndex() - 1);
                } else { //是第一个车道段
                    int preRoadId = currentRoad.getPredecessorId();
                    if(!roadMap.containsKey(preRoadId)) { // 地图出错了
                        log.error("找不到road(id={})的前驱road(id={})", currentRoad.getRoadId(), preRoadId);
                    } else {
                        Road preRoad = roads.get(roadMap.get(preRoadId)); // 如果是Junction？
                        int connectType = 0; // TODO: 两个road的连接方式，影响着前一个laneSection是preRoad的第一个还是最后一个
                        if(connectType == 0) { // 第一个
                            preLaneSection = preRoad.getLaneSections().get(0);
                        } else { // 最后一个
                            int length = preRoad.getLaneSections().size();
                            preLaneSection = preRoad.getLaneSections().get(length-1);
                        }
                    }

                }
                if (preLaneSection != null) {
                    updatePreLaneIndex(lane, preLaneSection);
                }
            }

            /*
                后继：
                1 当前laneSection不是当前road的最后一个车道段，那么
                - 当前laneSection的index+1，即后一个laneSection，找到对应的successorLaneId即可
                2 当前laneSection是当前road的最后一个车道段，那么
                - 找到当前road的后继road，根据两条road的连接方式判断successorLaneId属于后继road的第一个还是最后一个车道段
             */
            if(lane.getSuccessorLaneId() != 0) {
                LaneSection sucLaneSection = null;
                // 最后一个车道段
                int lastIndex = currentRoad.getLaneSections().size() - 1;
                LaneSection lastLaneSection = currentRoad.getLaneSections().get(lastIndex);

                if(currentLaneSection.getStartPosition() != lastLaneSection.getStartPosition()) { // 不是最后一个车道段
                    sucLaneSection = laneSections.get(currentLaneSection.getIndex() + 1);
                } else { //是最后一个车道段
                    int sucRoadId = currentRoad.getSuccessorId();
                    if(!roadMap.containsKey(sucRoadId)) { // 地图出错了
                        log.error("找不到road(id={})的后继road(id={})", currentRoad.getRoadId(), sucRoadId);
                    } else {
                        Road sucRoad = roads.get(roadMap.get(sucRoadId));
                        int connectType = 0; // TODO: 两个road的连接方式，影响着前一个laneSection是preRoad的第一个还是最后一个
                        if (connectType == 0) { // 第一个
                            sucLaneSection = sucRoad.getLaneSections().get(0);
                        } else { // 最后一个
                            int length = sucRoad.getLaneSections().size();
                            sucLaneSection = sucRoad.getLaneSections().get(length - 1);
                        }
                    }
                }
                if (sucLaneSection != null) {
                    updateSucLaneIndex(lane, sucLaneSection);
                }
            }

        }
    }

    // 抽取出来的一个方法，通过前驱laneSection更新lane的前驱
    private static void updatePreLaneIndex(Lane lane, LaneSection preLaneSection) {
        // 更新lane
        for(Lane preLane : preLaneSection.getLanes()) {
            if(preLane.getLaneId() == lane.getPredecessorLaneId()) {
                lane.setPredecessorSingleId(preLane.getSingleId());
                lane.setPredecessorIndex(preLane.getIndex());
                break;
            }
        }
    }

    // 抽取出来的一个方法，通过后继laneSection更新lane的后继
    private static void updateSucLaneIndex(Lane lane, LaneSection sucLaneSection) {
        for(Lane preLane : sucLaneSection.getLanes()) {
            if(preLane.getLaneId() == lane.getPredecessorLaneId()) {
                lane.setSuccessorSingleId(preLane.getSingleId());
                lane.setSuccessorIndex(preLane.getIndex());
                break;
            }
        }
    }

    private static void initConnection(List<Connection> connections, List<Junction> junctions, List<Road> roads) {
        // connection: incomingRoadIndex, connectionRoadIndex
        for(Connection connection : connections) {
            connection.setIncomingRoadIndex(roadMap.getOrDefault(connection.getIncomingRoadId(), -1));
            connection.setConnectingRoadIndex(roadMap.getOrDefault(connection.getConnectingRoadId(), -1));
        }

        // 设置direction
        for(Road road : roads) {
            if(road.getJunctionId() != -1) {
                int direction = 1; // 1左转 2直行 3右转 其他都不正常忽略掉
                for(Junction junction : junctions) {
                    for(Connection connection : junction.getConnections()) {
                        if(connection.getConnectingRoadId() == road.getRoadId()) { // 作为连接路
                            connection.setDirection(direction++);
                        }
                    }
                }
            }
        }
    }

}
