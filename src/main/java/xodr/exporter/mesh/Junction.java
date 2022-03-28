package xodr.exporter.mesh;

import xodr.data.RoadType;
import xodr.data.road.Road;
import xodr.data.road.referenceline.StraightCurveTrack;
import xodr.data.road.referenceline.Track;
import xodr.exporter.mesh.meshdata.Mesh;
import xodr.util.Position;

import java.util.ArrayList;
import java.util.List;

public class Junction {
    private List<Road> entrances;
    private List<Road> connectors;
    private int id;

    public Junction(int id){
        this.id = id;
        entrances = new ArrayList<>();
        connectors = new ArrayList<>();
    }

    public void addEntrance(Road entrance){
        entrances.add(entrance);
    }

    public List<Road> getConnectors(){
        return connectors;
    }

    public void setConnectors(List<Road> connectors){
        this.connectors = connectors;
    }

    public void setEntrances(List<Road> entrances){
        this.entrances = entrances;
    }

    public List<Road> getEntrances(){
        return entrances;
    }

    public void addConnector(Road connector){
        connectors.add(connector);
    }

    public Mesh mesh(MeshExporter.Mode mode){
        System.out.println("Meshing junction " + id + " in mode " + mode.toString());
        Mesh result = new Mesh();
        List<Road> shortest = getShortestRoads();

        if(mode == MeshExporter.Mode.ROADS) {
            //Entrance roads
            for (Road r : entrances)
                for (Track t : r.getReferenceLine().getTracks())
                    result.add(MeshExporter.generateCurveMesh(r, t, MeshExporter.Mode.ROADS));

            //Connector roads
            for (Road r : connectors)
                for (Track t : r.getReferenceLine().getTracks())
                    result.add(MeshExporter.generateCurveMesh(r, t, MeshExporter.Mode.ROADS));

        }else {
            System.out.println("Meshing entrance sidewalks");
            //Entrance sidewalks
            for (Road r : entrances) {
                System.out.println("road id: " + r.getId());
                List<Track> tracks = r.getReferenceLine().getTracks();
                Position start = tracks.get(tracks.size()-1).getStartingPosition();
 //               tracks.remove(tracks.size()-1);

                //Track curv = new StraightCurveTrack(start, 5, 1.2);
//                tracks.add(curv);

                for (Track t : tracks)
                    result.add(MeshExporter.generateCurveMesh(r, t, MeshExporter.Mode.SIDEWALKS));
            }

            System.out.println("Meshing connector sidewalks");
            //Connector roads
            for (Road r : shortest) {
                System.out.println("road id: " + r.getId());
                List<Track> tracks = r.getReferenceLine().getTracks();
                Position start = tracks.get(tracks.size()-1).getStartingPosition();
//                tracks.remove(tracks.size()-1);
                //Track curv = new StraightCurveTrack(r.getReferenceLine().getTracks().get(r.getReferenceLine().getTracks().size()-1).getEndPosition(), 5, 2.9);
//                tracks.add(curv);
                for (Track t : tracks) {
//                    if(t instanceof StraightCurveTrack) {
 //                       StraightCurveTrack curved = (StraightCurveTrack) t;
 //                       curved.reverseCurvature();
 //                   }
                    result.add(MeshExporter.generateCurveMesh(r, t, MeshExporter.Mode.SIDEWALK_RIGHT));
                }
            }
        }
        return result;
    }

    private List<Road> getShortestRoads(){
        List<Road> result = new ArrayList<>();
        for(int x = 0; x < entrances.size(); x++){
            Road shortest = getShortestRoad(connectors);
            result.add(shortest);
            connectors.remove(shortest);
        }
        for(Road r : result)
            connectors.add(r);

        return result;
    }

    private Road getShortestRoad(List<Road> roads){
        Road result = roads.get(0);
        for(Road r : roads) {
            boolean b = false;
            for (Track t : r.getReferenceLine().getTracks()) {
                if (t instanceof StraightCurveTrack) {
                    StraightCurveTrack s = (StraightCurveTrack) t;
                    b = s.getCurvature() < 0;
                    break;
                }
            }
            if (b && r.getLenght() < result.getLenght()) {
                result = r;
            }
        }
        return result;
    }

    /*
    public static Junction extractJunction(Road entry, List<Road> roads){
        Junction junction = new Junction();
        List<Road> roads2 = new ArrayList<>();
        roads2.addAll(roads);
        for(Road r : roads) {
            if (r.getPreviousRoadType() == RoadType.JUNCTION)
                for (Road r2 : roads2) {
                    if (r2.getPreviousRoad() == entry.getId() && r2.getNextRoad() == r.getId()) {
                        if (!junction.getConnectors().contains(r2)) {
                            junction.addConnector(r2);
                            roads.remove(r2);
                        }
                        if (!junction.getEntrances().contains(r)) {
                            junction.addEntrance(r);
                        }
                    }
                }
        }
        for(Road r : junction.getEntrances())
            roads.remove(r);

        System.out.println("Extracted junction: " + junction.getEntrances().size() + " entrances, "
                + junction.getConnectors().size() + " connectors");
        return junction;
    }*/

    /**
     * Extracts a junction with the road as an entry and removes all its components from the roads lit
     *
     * @param id
     * @param roads
     * @return
     */
    public static Junction extractJunction(int id, List<Road> roads) {
        List<Road> entries = getEntries(id, roads);
        List<Road> connectors = getConnectors(id, roads);

        Junction junction = new Junction(id);
        junction.setEntrances(entries);
        junction.setConnectors(connectors);

        for(Road r : entries)
            roads.remove(r);

        for(Road r : connectors)
            roads.remove(r);
        System.out.println("Extracted Junction with id " + id + "; " + entries.size()
                + " entries, " + connectors.size() + " connectors");

        return junction;
    }

    private static List<Road> getConnectors(int junctionId, List<Road> roads){
        List<Road> result = new ArrayList<>();
        for(Road r : roads)
            if(r.getJunctionId() == junctionId && r.getPreviousRoadType() == RoadType.ROAD)
                result.add(r);
        return result;
    }

    private static List<Road> getEntries(int junctionId, List<Road> roads){
        List<Road> result = new ArrayList<>();
        for(Road r : roads)
            if(r.getPreviousRoad() == junctionId && r.getPreviousRoadType() == RoadType.JUNCTION)
                result.add(r);
        return result;
    }

    public static Road getById(int id, List<Road> roads){
        for(Road r : roads)
            if(r.getId() == id)
                return r;
        return null;
    }


    public static boolean containsJunction(List<Road> roads){
        for(Road r : roads) {
//            System.out.println("containsJunction road id: " + r.getId() + " junction id: " + r.getJunctionId()
//                    + " prevRoadType: " + r.getPreviousRoadType().toString());
            if (r.getPreviousRoadType() == RoadType.JUNCTION)
                return containsConnectors(roads);
        }
        return false;
    }

    private static boolean containsConnectors(List<Road> roads){
        for(Road r : roads)
            if(r.getJunctionId() >= 0)
                return true;

        return false;
    }

    public static Road getEntry(List<Road> roads){
        for(Road r : roads)
            if(r.getPreviousRoadType() == RoadType.JUNCTION)
                return r;
        return null;
    }
}
