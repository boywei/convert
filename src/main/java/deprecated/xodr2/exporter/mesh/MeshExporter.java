package deprecated.xodr2.exporter.mesh;

import deprecated.xodr2.data.OpenDriveDataContainer;
import deprecated.xodr2.data.road.Road;
import deprecated.xodr2.data.road.lane.Lane;
import deprecated.xodr2.data.road.lane.LaneSection;
import deprecated.xodr2.data.road.referenceline.Track;
import deprecated.xodr2.exporter.mesh.meshdata.Face;
import deprecated.xodr2.exporter.mesh.meshdata.Mesh;
import deprecated.xodr2.exporter.mesh.meshdata.Vertex;
import deprecated.xodr2.util.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeshExporter {

    public enum Mode {
        ROADS, SIDEWALKS, ROAD_MARK_SOLID, ROAD_MARK_BROKEN, SIDEWALK_RIGHT
    }

    public final static double SIDEWALK_WIDTH = 1;
    public final static double SIDEWALK_HEIGHT = 0.2;
    public final static double ROAD_MARKINGS_WIDTH = 0.2;
    public final static double ROAD_MARKINGS_HEIGHT = 0.001;
    public final static double ROAD_MARKINGS_BROKEN_LENGTH = 1;
    public final static double ROAD_MARKINGS_BROKEN_SPACE = 1;
    public final static int CURVE_SEGMENTS = 10;

    public static MeshExport export(OpenDriveDataContainer con){



        List<Junction> junctions = new ArrayList<>();

        while(Junction.containsJunction(con.getRoads())) {
            System.out.println("Detected junction");
            Road entry = Junction.getEntry(con.getRoads());
            Junction junction = Junction.extractJunction(entry.getPreviousRoad(), con.getRoads());
            junctions.add(junction);
        }

        Mesh roadMesh = exportMesh(con, Mode.ROADS);
        Mesh sidewalkMesh = exportMesh(con, Mode.SIDEWALKS);
        Mesh roadMarkMesh = exportMesh(con, Mode.ROAD_MARK_SOLID);
        roadMarkMesh.add(exportMesh(con, Mode.ROAD_MARK_BROKEN));

        System.out.println("Final junction count: " + junctions.size());

        for(Junction j : junctions){
            roadMesh.add(j.mesh(Mode.ROADS));
            sidewalkMesh.add(j.mesh(Mode.SIDEWALKS));
        }

        MeshExport meshExport = new MeshExport(roadMesh, sidewalkMesh, roadMarkMesh);
        return meshExport;

    }

    private static Mesh exportMesh(OpenDriveDataContainer con, Mode exportMode){

        Mesh mesh = new Mesh();

        for(Road road:con.getRoads()){
            for(Track t:road.getReferenceLine().getTracks()){

                if(exportMode.equals(Mode.ROAD_MARK_BROKEN)){
                    int amountRoadMarks = Math.round((float) (((float) t.getLength()) / (ROAD_MARKINGS_BROKEN_LENGTH + ROAD_MARKINGS_BROKEN_SPACE)));
                    mesh.add(generateCurveMesh(road, t, exportMode, amountRoadMarks*2));
                } else {
                    mesh.add(generateCurveMesh(road, t, exportMode, CURVE_SEGMENTS));
                }

            }
        }

        return mesh;
    }

    public static Mesh generateCurveMesh(Road road, Track track, Mode exportMode){
        return generateCurveMesh(road, track, exportMode, CURVE_SEGMENTS);
    }

    public static Mesh generateCurveMesh(Road road, Track track, Mode exportMode, int amountSegments){

        Mesh mesh = new Mesh();

        for(int i = 0; i < amountSegments; i++){
            if(exportMode.equals(Mode.ROAD_MARK_BROKEN)){
                double l1 = (i * track.getLength()) / amountSegments;
                double l2 = ((i * track.getLength()) / amountSegments) + ROAD_MARKINGS_BROKEN_LENGTH;
                mesh.add(generateMesh(road, track, l1, l2, exportMode));
                i++;
            } else {
                double l1 = (i * track.getLength()) / amountSegments;
                double l2 = ((i+1) * track.getLength()) / amountSegments;
                mesh.add(generateMesh(road, track, l1, l2, exportMode));
            }
        }

        return mesh;
    }

    private static Mesh generateMesh(Road road, Track track, double l1, double l2, Mode exportMode){

        List<Vertex> vertices = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        boolean mirrorVectors = false;
        List<Position> shiftVectors = new ArrayList<>();
        List<Vertex> faceVertices = new ArrayList<>();
        switch(exportMode){
            case ROADS:
                shiftVectors.add(new Position(0, road.getLeftWidthAtPosition(0)));
                shiftVectors.add(new Position(0, -road.getRightWidthAtPosition(0)));
                mirrorVectors = false;

                break;
            case SIDEWALKS:
                shiftVectors.add(new Position(0, road.getLeftWidthAtPosition(0) + SIDEWALK_WIDTH));
                shiftVectors.add(new Position(0, road.getLeftWidthAtPosition(0) + SIDEWALK_WIDTH, SIDEWALK_HEIGHT, 0d));
                shiftVectors.add(new Position(0, road.getLeftWidthAtPosition(0), SIDEWALK_HEIGHT, 0d));
                shiftVectors.add(new Position(0, road.getLeftWidthAtPosition(0)));
                shiftVectors.add(new Position(0, -road.getRightWidthAtPosition(0)));
                shiftVectors.add(new Position(0, -road.getRightWidthAtPosition(0), SIDEWALK_HEIGHT, 0d));
                shiftVectors.add(new Position(0, -road.getRightWidthAtPosition(0) - SIDEWALK_WIDTH, SIDEWALK_HEIGHT, 0d));
                shiftVectors.add(new Position(0, -road.getRightWidthAtPosition(0) - SIDEWALK_WIDTH));
                mirrorVectors = false;

                break;
            case SIDEWALK_RIGHT:
                shiftVectors.add(new Position(0, -road.getRightWidthAtPosition(0)));
                shiftVectors.add(new Position(0, -road.getRightWidthAtPosition(0), SIDEWALK_HEIGHT, 0d));
                shiftVectors.add(new Position(0, -road.getRightWidthAtPosition(0) - SIDEWALK_WIDTH, SIDEWALK_HEIGHT, 0d));
                shiftVectors.add(new Position(0, -road.getRightWidthAtPosition(0) - SIDEWALK_WIDTH));
                mirrorVectors = false;

                break;
            case ROAD_MARK_BROKEN:
            case ROAD_MARK_SOLID:

                for(LaneSection laneSection:road.getLaneSections()){
                    if(laneSection.getMiddleLane() != null && checkForRoadMarkType(exportMode, laneSection.getMiddleLane().getRoadMarkType())){
                        shiftVectors.add(new Position(0, ROAD_MARKINGS_WIDTH/2, ROAD_MARKINGS_HEIGHT, 0));
                        shiftVectors.add(new Position(0, -ROAD_MARKINGS_WIDTH/2, ROAD_MARKINGS_HEIGHT, 0));
                    }

                    // TODO: improve width calculation
                    double currentWidth = 0;
                    for(int i = 0; i < laneSection.getLeftLanes().size(); i++){
                        currentWidth += laneSection.getLeftLanes().get(i).getWidthA();
                        if(checkForRoadMarkType(exportMode, laneSection.getLeftLanes().get(i).getRoadMarkType())){
                            shiftVectors.add(new Position(0, currentWidth + ROAD_MARKINGS_WIDTH/2, ROAD_MARKINGS_HEIGHT, 0));
                            shiftVectors.add(new Position(0, currentWidth - ROAD_MARKINGS_WIDTH/2, ROAD_MARKINGS_HEIGHT, 0));
                        }
                    }
                    currentWidth = 0;
                    for(int i = 0; i < laneSection.getRightLanes().size(); i++){
                        currentWidth -= laneSection.getRightLanes().get(i).getWidthA();
                        if(checkForRoadMarkType(exportMode, laneSection.getRightLanes().get(i).getRoadMarkType())) {
                            shiftVectors.add(new Position(0, currentWidth + ROAD_MARKINGS_WIDTH / 2, ROAD_MARKINGS_HEIGHT, 0));
                            shiftVectors.add(new Position(0, currentWidth - ROAD_MARKINGS_WIDTH / 2, ROAD_MARKINGS_HEIGHT, 0));
                        }
                    }
                }

                mirrorVectors = false;

                break;
        }

        List<Vertex> vs1 = getPerpendicularVertices(track.getPositionAlongTrack(l1), shiftVectors, mirrorVectors);
        List<Vertex> vs2 = getPerpendicularVertices(track.getPositionAlongTrack(l2), shiftVectors, mirrorVectors);
        vertices.addAll(vs1);
        vertices.addAll(vs2);

        switch(exportMode){
            case ROADS:
                faceVertices.addAll(vs1);
                Collections.reverse(vs2);
                faceVertices.addAll(vs2);
                faces.add(new Face(faceVertices));

                break;
            case SIDEWALKS:
                faceVertices = new ArrayList<>();
                faceVertices.add(vs1.get(0));
                faceVertices.add(vs1.get(1));
                faceVertices.add(vs2.get(1));
                faceVertices.add(vs2.get(0));
                faces.add(new Face(faceVertices));
                faceVertices = new ArrayList<>();

                faceVertices.add(vs1.get(1));
                faceVertices.add(vs1.get(2));
                faceVertices.add(vs2.get(2));
                faceVertices.add(vs2.get(1));
                faces.add(new Face(faceVertices));
                faceVertices = new ArrayList<>();

                faceVertices.add(vs1.get(2));
                faceVertices.add(vs1.get(3));
                faceVertices.add(vs2.get(3));
                faceVertices.add(vs2.get(2));
                faces.add(new Face(faceVertices));
                faceVertices = new ArrayList<>();

                faceVertices.add(vs1.get(4));
                faceVertices.add(vs1.get(5));
                faceVertices.add(vs2.get(5));
                faceVertices.add(vs2.get(4));
                faces.add(new Face(faceVertices));
                faceVertices = new ArrayList<>();

                faceVertices.add(vs1.get(5));
                faceVertices.add(vs1.get(6));
                faceVertices.add(vs2.get(6));
                faceVertices.add(vs2.get(5));
                faces.add(new Face(faceVertices));
                faceVertices = new ArrayList<>();

                faceVertices.add(vs1.get(6));
                faceVertices.add(vs1.get(7));
                faceVertices.add(vs2.get(7));
                faceVertices.add(vs2.get(6));
                faces.add(new Face(faceVertices));

                break;
            case SIDEWALK_RIGHT:
                faceVertices = new ArrayList<>();
                faceVertices.add(vs1.get(0));
                faceVertices.add(vs1.get(1));
                faceVertices.add(vs2.get(1));
                faceVertices.add(vs2.get(0));
                faces.add(new Face(faceVertices));
                faceVertices = new ArrayList<>();

                faceVertices.add(vs1.get(1));
                faceVertices.add(vs1.get(2));
                faceVertices.add(vs2.get(2));
                faceVertices.add(vs2.get(1));
                faces.add(new Face(faceVertices));
                faceVertices = new ArrayList<>();

                faceVertices.add(vs1.get(2));
                faceVertices.add(vs1.get(3));
                faceVertices.add(vs2.get(3));
                faceVertices.add(vs2.get(2));
                faces.add(new Face(faceVertices));
                faceVertices = new ArrayList<>();

                break;
            case ROAD_MARK_BROKEN:
            case ROAD_MARK_SOLID:

                for(int i = 0; i < vs1.size(); i = i+2){
                    faceVertices = new ArrayList<>();
                    faceVertices.add(vs1.get(i));
                    faceVertices.add(vs1.get(i+1));
                    faceVertices.add(vs2.get(i+1));
                    faceVertices.add(vs2.get(i));
                    faces.add(new Face(faceVertices));
                }

                break;
        }

        Mesh mesh = new Mesh(vertices, faces);
        return mesh;
    }

    private static List<Vertex> getPerpendicularVertices(Position pos, List<Position> shiftVectors, boolean mirrorVectors){

        List<Vertex> vertices1 = new ArrayList<>();
        List<Vertex> vertices2 = new ArrayList<>();

        for(Position p:shiftVectors){
            vertices1.add(new Vertex(pos.addRelativeToHeading(p)));
            vertices2.add(new Vertex(pos.addRelativeToHeading(p.mirronOnXZ())));
        }

        Collections.reverse(vertices2);
        if(mirrorVectors){
            vertices1.addAll(vertices2);
        }
        return  vertices1;
    }

    private static boolean checkForRoadMarkType(Mode mode, Lane.RoadMarkType roadMarkType){
        return (mode.equals(Mode.ROAD_MARK_SOLID) && roadMarkType.equals(Lane.RoadMarkType.SOLID))
        || (mode.equals(Mode.ROAD_MARK_BROKEN) && roadMarkType.equals(Lane.RoadMarkType.BROKEN));
    }

}
