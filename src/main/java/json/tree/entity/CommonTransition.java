package json.tree.entity;

import java.util.List;

public class CommonTransition {
    private List<String> guards;
    private int id;
    private List<Position> linkPoints;
    private int sourceId;
    private int targetId;
    private Position treeTextPosition;
    // 以下变量通过计算获得
    private int level;
    private int group;
    private int number;
    private Behavior sourceBehavior;
    private Behavior targetBehavior;
    private BranchPoint targetBranchPoint;

    public Behavior getSourceBehavior() {
        return sourceBehavior;
    }

    public void setSourceBehavior(Behavior sourceBehavior) {
        this.sourceBehavior = sourceBehavior;
    }

    public Behavior getTargetBehavior() {
        return targetBehavior;
    }

    public void setTargetBehavior(Behavior targetBehavior) {
        this.targetBehavior = targetBehavior;
    }

    public BranchPoint getTargetBranchPoint() {
        return targetBranchPoint;
    }

    public void setTargetBranchPoint(BranchPoint targetBranchPoint) {
        this.targetBranchPoint = targetBranchPoint;
    }

    public CommonTransition(List<String> guards, int id, List<Position> linkPoints, int sourceId, int targetId, Position treeTextPosition) {
        this.guards = guards;
        this.id = id;
        this.linkPoints = linkPoints;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.treeTextPosition = treeTextPosition;
    }

    public List<String> getGuards() {
        return guards;
    }

    public void setGuards(List<String> guards) {
        this.guards = guards;
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
