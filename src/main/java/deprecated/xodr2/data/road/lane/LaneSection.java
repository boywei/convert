package deprecated.xodr2.data.road.lane;

import java.util.List;

public class LaneSection {

    private List<OuterLane> leftLanes;
    private List<OuterLane> rightLanes;
    private CenterLane middleLane;
    private double offset;

    public LaneSection(List<OuterLane> leftLanes, List<OuterLane> rightLanes, CenterLane middleLane, double offset) {
        this.leftLanes = leftLanes;
        this.rightLanes = rightLanes;
        this.middleLane = middleLane;
        this.offset = offset;
    }

    public List<OuterLane> getLeftLanes() {
        return leftLanes;
    }

    public List<OuterLane> getRightLanes() {
        return rightLanes;
    }

    public CenterLane getMiddleLane() {
        return middleLane;
    }

    public double getOffset() {
        return offset;
    }
    
}
