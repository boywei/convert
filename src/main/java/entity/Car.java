package entity;

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
}
