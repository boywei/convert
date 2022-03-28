package xodr.data.road.lane;

public class OuterLane extends Lane {

    private double widthOffset;
    private double widthA;
    private double widthB;
    private double widthC;
    private double widthD;

    public OuterLane(int laneID,  double roadMarkOffset, RoadMarkType roadMarkType, double roadMarkWidth, double widthOffset, double widthA, double widthB, double widthC, double widthD) {
        super(laneID, roadMarkOffset, roadMarkType, roadMarkWidth);
        this.widthOffset = widthOffset;
        this.widthA = widthA;
        this.widthB = widthB;
        this.widthC = widthC;
        this.widthD = widthD;
    }

    public double getWidthOffset() {
        return widthOffset;
    }

    public double getWidthA() {
        return widthA;
    }

    public double getWidthB() {
        return widthB;
    }

    public double getWidthC() {
        return widthC;
    }

    public double getWidthD() {
        return widthD;
    }

}
