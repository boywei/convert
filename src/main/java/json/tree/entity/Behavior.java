package json.tree.entity;

import java.util.List;
import java.util.Map;

public class Behavior {
    private int id;
    private String name;
    private Map<String, Double> params;
    private Position position;
    private Position treeTextPosition;

    private int level;
    private int group;
    private int number;
    private List<CommonTransition> nextTransitions;
    private List<Behavior> nextBehaviors;
    private List<BranchPoint> nextBranchPoints;

    public List<CommonTransition> getNextTransitions() {
        return nextTransitions;
    }

    public void setNextTransitions(List<CommonTransition> nextTransitions) {
        this.nextTransitions = nextTransitions;
    }

    public List<Behavior> getNextBehaviors() {
        return nextBehaviors;
    }

    public void setNextBehaviors(List<Behavior> nextBehaviors) {
        this.nextBehaviors = nextBehaviors;
    }

    public List<BranchPoint> getNextBranchPoints() {
        return nextBranchPoints;
    }

    public void setNextBranchPoints(List<BranchPoint> nextBranchPoints) {
        this.nextBranchPoints = nextBranchPoints;
    }

    public Behavior(int id, String name, Map<String, Double> params, Position position, Position treeTextPosition) {
        this.id = id;
        this.name = name;
        this.params = params;
        this.position = position;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getParams() {
        return params;
    }

    public void setParams(Map<String, Double> params) {
        this.params = params;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getTreeTextPosition() {
        return treeTextPosition;
    }

    public void setTreeTextPosition(Position treeTextPosition) {
        this.treeTextPosition = treeTextPosition;
    }
}
