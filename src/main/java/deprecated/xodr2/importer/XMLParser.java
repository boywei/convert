package deprecated.xodr2.importer;

import deprecated.xodr2.data.road.Road;
import deprecated.xodr2.data.OpenDriveDataContainer;
import deprecated.xodr2.data.RoadType;
import deprecated.xodr2.data.road.lane.CenterLane;
import deprecated.xodr2.data.road.lane.Lane;
import deprecated.xodr2.data.road.lane.LaneSection;
import deprecated.xodr2.data.road.lane.OuterLane;
import deprecated.xodr2.data.road.referenceline.*;
import deprecated.xodr2.util.Position;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    public static OpenDriveDataContainer parse(String input){

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();

            Document document = documentBuilder.parse(new InputSource(new StringReader(input)));
            Element root = document.getDocumentElement();

            List<Road> roads = new ArrayList<>();
            NodeList roadsList = root.getElementsByTagName("road");
            for(int i = 0; i < roadsList.getLength(); i++){
                Element roadElement = (Element) roadsList.item(i);

                int previous = -1;
                int next = -1;
                RoadType previousType = RoadType.ROAD;
                RoadType nextType = RoadType.ROAD;

                NodeList linkList = roadElement.getElementsByTagName("link");
                Element myLinkList =  (Element) linkList.item(0);

                Element predecessor = null;
                Element successor = null;


                NodeList predecessorList = myLinkList.getElementsByTagName("predecessor");
                if(predecessorList != null) {
                    predecessor = (Element) predecessorList.item(0);
                    if(predecessor != null) {
                        previous = Integer.parseInt(predecessor.getAttribute("elementId"));
                        previousType = predecessor.getAttribute("elementType")
                                .equalsIgnoreCase("junction") ? RoadType.JUNCTION : RoadType.ROAD;
                    }
                }

                NodeList successorList = myLinkList.getElementsByTagName("successor");
                if(successorList != null) {
                    successor = (Element) successorList.item(0);
                    if(successor != null) {
                        next = Integer.parseInt(successor.getAttribute("elementId"));
                        nextType = successor.getAttribute("elementType")
                                .equalsIgnoreCase("junction") ? RoadType.JUNCTION : RoadType.ROAD;
                    }
                }
                List<Track> tracks = new ArrayList<>();

                int roadId = Integer.parseInt(roadElement.getAttribute("id"));
                int junctionId = Integer.parseInt(roadElement.getAttribute("junction"));
                NodeList planViewList = roadElement.getElementsByTagName("planView");
                for(int j = 0; j < planViewList.getLength(); j++){
                    Element planViewElement = (Element) planViewList.item(j);

                    NodeList geometryList = planViewElement.getElementsByTagName("geometry");
                    for(int k = 0; k < geometryList.getLength(); k++){
                        Element geometryElement = (Element) geometryList.item(k);

                        Position position = new Position(
                                Double.parseDouble(geometryElement.getAttribute("x")),
                                Double.parseDouble(geometryElement.getAttribute("y")),
                                0,
                                Double.parseDouble(geometryElement.getAttribute("hdg")));
                        double length = Double.parseDouble(geometryElement.getAttribute("length"));

                        NodeList geometryContentList = geometryElement.getChildNodes();
                        for(int l = 0; l < geometryContentList.getLength(); l++){
                            Node geometryContentNode = geometryContentList.item(l);

                            if(geometryContentNode instanceof Element){
                                Element geometryContentElement = (Element) geometryContentNode;

                                Track track = null;
                                String trackType = geometryContentElement.getNodeName();
                                if(trackType.equals("line")){
                                    track = new StraightLineTrack(position, length);
                                } else if(trackType.equals("arc")){
                                    double curvature = Double.parseDouble(geometryContentElement.getAttribute("curvature"));
                                    track = new StraightCurveTrack(position, length, curvature);
                                } else if(trackType.equals("spiral")){
                                    double curvStart = Double.parseDouble(geometryContentElement.getAttribute("curvStart"));
                                    double curvEnd = Double.parseDouble(geometryContentElement.getAttribute("curvEnd"));
                                    track = new SpiralTrack(position, length, curvStart, curvEnd);
                                }
                                if(track != null){
                                    tracks.add(track);
                                }
                            }
                        }
                    }
                }

                List<LaneSection> laneSections = new ArrayList<>();

                NodeList lanesNodeList = roadElement.getElementsByTagName("lanes");
                for(int j = 0; j < lanesNodeList.getLength(); j++){
                    Element laneSectionElement = (Element) lanesNodeList.item(j);

                    List<OuterLane> leftLanes = parseLanes(laneSectionElement.getElementsByTagName("left"), false);
                    List<OuterLane> rightLanes = parseLanes(laneSectionElement.getElementsByTagName("right"), true);
                    List<CenterLane> centerLanes = parseLanes(laneSectionElement.getElementsByTagName("center"), false);

                    String s = laneSectionElement.getAttribute("s");
                    if(s.isEmpty()){
                        s = "0.0";
                    }
                    LaneSection laneSection = new LaneSection(leftLanes, rightLanes, centerLanes.get(0), Double.parseDouble(s));
                    laneSections.add(laneSection);
                }

                ReferenceLine rl = new ReferenceLine(tracks);
                System.out.println("Creating road id: " + roadId + " previous: " + previousType.toString() + " next: " + nextType.toString() + " junction: " + junctionId);
                Road road = new Road(rl ,roadId, previous, next, previousType, nextType, laneSections, junctionId, null);
                roads.add(road);
            }

            System.out.println("--------------------------------------------");
            OpenDriveDataContainer con = new OpenDriveDataContainer(roads);
            return con;

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            System.out.println("Error parsing XML file");
            return null;
        }

    }

    private static List parseLanes(NodeList laneNodeList, boolean idsNegative){

        List<Lane> lanes = new ArrayList<>();
        for(int i = 0; i < laneNodeList.getLength(); i++){
            //Element laneElement = (Element) laneNodeList.item(i);

            Element element = (Element) laneNodeList.item(i);
            NodeList laneElementList = (NodeList) element.getElementsByTagName("lane");
            for(int x=0; x<laneElementList.getLength(); x++) {
                Element laneElement = (Element) laneElementList.item(x);

                String laneType = laneElement.getAttribute("type");
                System.out.println("Type of element " + x + ": " + laneType);
                if (laneType.equalsIgnoreCase("driving")) {
                    //if(true){

                    double widthOffset = 0;
                    double widthA = 0;
                    double widthB = 0;
                    double widthC = 0;
                    double widthD = 0;
                    boolean widthExisting = false;
                    NodeList widthNodeList = laneElement.getElementsByTagName("width");
                    for (int j = 0; j < widthNodeList.getLength(); j++) {
                        widthExisting = true;
                        Element widthElement = (Element) widthNodeList.item(j);
                        widthOffset = Double.parseDouble(widthElement.getAttribute("sOffset"));
                        widthA = Double.parseDouble(widthElement.getAttribute("a"));
                        widthB = Double.parseDouble(widthElement.getAttribute("b"));
                        widthC = Double.parseDouble(widthElement.getAttribute("c"));
                        widthD = Double.parseDouble(widthElement.getAttribute("d"));
                    }

                    double roadMarkOffset = 0;
                    Lane.RoadMarkType roadMarkType = Lane.RoadMarkType.SOLID;
                    double roadMarkWidth = 0;
                    NodeList roadMarkNodeList = laneElement.getElementsByTagName("roadMark");
                    for (int j = 0; j < roadMarkNodeList.getLength(); j++) {
                        Element roadMarkElement = (Element) roadMarkNodeList.item(j);
                        roadMarkOffset = Double.parseDouble(roadMarkElement.getAttribute("sOffset"));
                        try {
                            roadMarkType = Lane.RoadMarkType.valueOf(roadMarkElement.getAttribute("type").toUpperCase());
                        } catch (IllegalArgumentException e) {
                            roadMarkType = Lane.RoadMarkType.NONE;
                        }
                        roadMarkWidth = Double.parseDouble(roadMarkElement.getAttribute("width"));
                    }
                    Lane lane;
                    if (widthExisting) {
                        int id = i;
                        if (idsNegative) {
                            id = -id;
                        }
                        lane = new OuterLane(id, roadMarkOffset, roadMarkType, roadMarkWidth, widthOffset, widthA, widthB, widthC, widthD);
                    } else {
                        lane = new CenterLane(roadMarkOffset, roadMarkType, roadMarkWidth);
                    }
                    lanes.add(lane);
                }
            }
        }

        return lanes;
    }

}
