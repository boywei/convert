package json.tree.entity;

import java.util.Map;

public class Car {
    private boolean heading;
    private double initSpeed;
    private double maxSpeed;
    private String model;
    private String name;
    private double roadDeviation;
    private String treePath;
    private Map<String, Double> locationParams;
    // 通过treePath的对应文件获取
    private MTree mTree;
    // 以下变量不能直接获取，需要初始化后通过Map获取
    private int laneId;
    private double minOffset;
    private double maxOffset;
    private int roadId;

    private double offset;
    private int laneSectionId;
    private int laneIndex;
    private int laneSectionIndex;
    private int roadIndex;
    private double width = 1.5;
    private double length = 2.5;

    public Map<String, Double> getLocationParams() {
        return locationParams;
    }

    public void setLocationParams(Map<String, Double> locationParams) {
        this.locationParams = locationParams;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
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

    public double getMinOffset() {
        return minOffset;
    }

    public void setMinOffset(double minOffset) {
        this.minOffset = minOffset;
    }

    public double getMaxOffset() {
        return maxOffset;
    }

    public void setMaxOffset(double maxOffset) {
        this.maxOffset = maxOffset;
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

    @Override
    public String toString() {
        return "Car{" +
                "heading=" + heading +
                ", initSpeed=" + initSpeed +
                ", laneId=" + laneId +
                ", mTree=" + mTree +
                ", name='" + name + '\'' +
                ", offset=" + offset +
                ", roadDeviation=" + roadDeviation +
                ", roadId=" + roadId +
                ", treePath='" + treePath + '\'' +
                ", minOffset=" + minOffset +
                ", maxOffset=" + maxOffset +
                ", maxSpeed=" + maxSpeed +
                ", laneSectionId=" + laneSectionId +
                ", laneIndex=" + laneIndex +
                ", laneSectionIndex=" + laneSectionIndex +
                ", roadIndex=" + roadIndex +
                ", width=" + width +
                ", length=" + length +
                '}';
    }
}
