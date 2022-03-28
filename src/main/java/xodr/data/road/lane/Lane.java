package xodr.data.road.lane;

public abstract class Lane {

    public enum RoadMarkType {
        NONE, SOLID, BROKEN
    }

    private int laneID;
    private double roadMarkOffset;
    private RoadMarkType roadMarkType;
    private double roadMarkWidth;

    public Lane(int laneID, double roadMarkOffset, RoadMarkType roadMarkType, double roadMarkWidth) {
        this.laneID = laneID;
        this.roadMarkOffset = roadMarkOffset;
        this.roadMarkType = roadMarkType;
        this.roadMarkWidth = roadMarkWidth;
    }

    public int getLaneID() {
        return laneID;
    }

    public double getRoadMarkOffset() {
        return roadMarkOffset;
    }

    public RoadMarkType getRoadMarkType() {
        return roadMarkType;
    }

    public double getRoadMarkWidth() {
        return roadMarkWidth;
    }
}
