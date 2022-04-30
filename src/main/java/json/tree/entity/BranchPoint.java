package json.tree.entity;

import java.util.List;

public class BranchPoint {
    private int id;
    private Position position;
    // 以下需通过计算获得
    private int level;
    private int group;
    private int number;
    private List<ProbabilityTransition> nextTransitions;
    private List<Behavior> nextBehaviors;

    public List<ProbabilityTransition> getNextTransitions() {
        return nextTransitions;
    }

    public void setNextTransitions(List<ProbabilityTransition> nextTransitions) {
        this.nextTransitions = nextTransitions;
    }

    public List<Behavior> getNextBehaviors() {
        return nextBehaviors;
    }

    public void setNextBehaviors(List<Behavior> nextBehaviors) {
        this.nextBehaviors = nextBehaviors;
    }

    public BranchPoint(int id, Position position) {
        this.id = id;
        this.position = position;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
