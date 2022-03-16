package entity;

public class ProbabilityTransition {
    private int id;
    private Position[] linkPoints;
    private int sourceId;
    private int targetId;
    private Position treeTextPosition;
    private String weight;

    public ProbabilityTransition(int id, Position[] linkPoints, int sourceId, int targetId, Position treeTextPosition, String weight) {
        this.id = id;
        this.linkPoints = linkPoints;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.treeTextPosition = treeTextPosition;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position[] getLinkPoints() {
        return linkPoints;
    }

    public void setLinkPoints(Position[] linkPoints) {
        this.linkPoints = linkPoints;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public Position getTreeTextPosition() {
        return treeTextPosition;
    }

    public void setTreeTextPosition(Position treeTextPosition) {
        this.treeTextPosition = treeTextPosition;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
