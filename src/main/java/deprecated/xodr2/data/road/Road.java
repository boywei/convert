package deprecated.xodr2.data.road;

import deprecated.xodr2.data.RoadType;
import deprecated.xodr2.data.road.lane.LaneSection;
import deprecated.xodr2.data.road.lane.OuterLane;
import deprecated.xodr2.data.road.referenceline.ReferenceLine;
import deprecated.xodr2.data.road.referenceline.Track;

import java.util.List;

public class Road {
    private int id, junctionId;
    private int previousRoad, nextRoad;
    private RoadType previousRoadType, nextRoadType;
    private boolean meshed;

    private ReferenceLine referenceLine;
    private List<LaneSection> laneSections;
    private List<RoadSignal> roadSignals;

    public Road(ReferenceLine referenceLine, int id, int previousRoad, int nextRoad, RoadType previousRoadType, RoadType nextRoadType, List<LaneSection> laneSections, int junctionId, List<RoadSignal> roadSignals) {
        this.referenceLine = referenceLine;
        this.id  = id;
        this.previousRoad = previousRoad;
        this.nextRoad = nextRoad;
        this.previousRoadType = previousRoadType;
        this.nextRoadType = nextRoadType;
        this.laneSections = laneSections;
        this.junctionId = junctionId;
        this.roadSignals = roadSignals;
        this.meshed = false;
    }

    public double getLeftWidthAtPosition(double l){
        // TODO: improve calcualtion
        double w = 0;
        for(LaneSection laneSection:laneSections){
            for(OuterLane lane:laneSection.getLeftLanes()){
                w += lane.getWidthA();
            }
        }
        return w;
    }

    public double getRightWidthAtPosition(double l){
        // TODO: improve calcualtion
        double w = 0;
        for(LaneSection laneSection:laneSections){
            for(OuterLane lane:laneSection.getRightLanes()){
                w += lane.getWidthA();
            }
        }
        return w;
    }

    public int getJunctionId(){
        return junctionId;
    }

    public ReferenceLine getReferenceLine() {
        return referenceLine;
    }

    public int getId(){
        return id;
    }

    public List<LaneSection> getLaneSections() {
        return laneSections;
    }

    public int getPreviousRoad(){
        return previousRoad;
    }

    public int getNextRoad(){
        return nextRoad;
    }

    public RoadType getPreviousRoadType() {
        return previousRoadType;
    }

    public RoadType getNextRoadType() {
        return nextRoadType;
    }

    public void setPreviousRoadType(RoadType type){
        this.previousRoadType = type;
    }

    public boolean isMeshed(){
        return meshed;
    }

    public void setMeshed(boolean meshed){
        this.meshed = meshed;
    }

    public double getLenght(){
        double sum = 0;
        for(Track t : referenceLine.getTracks())
            sum += t.getLength();

        return sum;
    }

    public List<RoadSignal> getRoadSignals() {
        return roadSignals;
    }

}
