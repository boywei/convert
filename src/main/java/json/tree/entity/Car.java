package json.tree.entity;

import java.util.HashMap;

public class Car {
    private boolean heading;
    private double initSpeed;
    private int laneId;
    private MTree mTree;
    private String model;
    private String name;
    private double offset;
    private double roadDeviation;
    private int roadId;
    private String treePath;
    private double minLaneOffset;
    private double maxLaneOffset;

    private int laneSectionId;
    private int laneIndex;
    private int laneSectionIndex;
    private int roadIndex;
    private double width;
    private double length;
    private double widthIndex;
    private double lengthIndex;

    public HashMap<String, Integer> location  = new HashMap<String, Integer>();

    public Car(boolean heading, double initSpeed, int laneId, MTree mTree, String model, String name, double offset, double roadDeviation, int roadId, String treePath) {
        this.heading = heading;
        this.initSpeed = initSpeed;
        this.laneId = laneId;
        this.mTree = mTree;
        this.model = model;
        this.name = name;
        this.offset = offset;
        this.roadDeviation = roadDeviation;
        this.roadId = roadId;
        this.treePath = treePath;
    }

    public boolean isHeading() {
        return heading;
    }

    public void setHeading(boolean heading) {
        this.heading = heading;
    }

    public double getInitSpeed() {
        return initSpeed;
    }

    public void setInitSpeed(double initSpeed) {
        this.initSpeed = initSpeed;
    }

    public int getLaneId() {
        return laneId;
    }

    public void setLaneId(int laneId) {
        this.laneId = laneId;
    }

    public MTree getmTree() {
        return mTree;
    }

    public void setmTree(MTree mTree) {
        this.mTree = mTree;
    }

    public String getString() {
        return model;
    }

    public void setString(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getRoadDeviation() {
        return roadDeviation;
    }

    public void setRoadDeviation(double roadDeviation) {
        this.roadDeviation = roadDeviation;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public String getTreePath() {
        return treePath;
    }

    public void setTreePath(String treePath) {
        this.treePath = treePath;
    }

    public double getMinLaneOffset() {
        return minLaneOffset;
    }

    public void setMinLaneOffset(double minLaneOffset) {
        this.minLaneOffset = minLaneOffset;
    }

    public double getMaxLaneOffset() {
        return maxLaneOffset;
    }

    public void setMaxLaneOffset(double maxLaneOffset) {
        this.maxLaneOffset = maxLaneOffset;
    }

    public int getLaneSectionId() {
        return laneSectionId;
    }

    public void setLaneSectionId(int laneSectionId) {
        this.laneSectionId = laneSectionId;
    }

    public int getLaneIndex() {
        return laneIndex;
    }

    public void setLaneIndex(int laneIndex) {
        this.laneIndex = laneIndex;
    }

    public int getLaneSectionIndex() {
        return laneSectionIndex;
    }

    public void setLaneSectionIndex(int laneSectionIndex) {
        this.laneSectionIndex = laneSectionIndex;
    }

    public int getRoadIndex() {
        return roadIndex;
    }

    public void setRoadIndex(int roadIndex) {
        this.roadIndex = roadIndex;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidthIndex() {
        return widthIndex;
    }

    public void setWidthIndex(double widthIndex) {
        this.widthIndex = widthIndex;
    }

    public double getLengthIndex() {
        return lengthIndex;
    }

    public void setLengthIndex(double lengthIndex) {
        this.lengthIndex = lengthIndex;
    }


}
