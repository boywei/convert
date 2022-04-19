package json.tree.entity;

import java.util.List;

public class ProbabilityTransition {
    private int id;
    private List<Position> linkPoints;
    private int sourceId;
    private int targetId;
    private Position treeTextPosition;
    private String weight;

    private int level;
    private int group;
    private int number;

    private BranchPoint sourceBranchPoint;
    private Behavior targetBehavior;

    public BranchPoint getSourceBranchPoint() {
        return sourceBranchPoint;
    }

    public void setSourceBranchPoint(BranchPoint sourceBranchPoint) {
        this.sourceBranchPoint = sourceBranchPoint;
    }

    public Behavior getTargetBehavior() {
        return targetBehavior;
    }

    public void setTargetBehavior(Behavior targetBehavior) {
        this.targetBehavior = targetBehavior;
    }

    public ProbabilityTransition(int id, List<Position> linkPoints, int sourceId, int targetId, Position treeTextPosition, String weight) {
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

    public List<Position> getLinkPoints() {
        return linkPoints;
    }

    public void setLinkPoints(List<Position> linkPoints) {
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
