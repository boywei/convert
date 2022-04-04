package json.tree.entity;

import java.util.Map;

public class Behavior {
    private int id;
    private String name;
    private Map<String, String> params;
    private Position position;
    private Position treeTextPosition;

    private int level;
    private int group;
    private int number;

    private double acceleration;
    private double targetSpeed;
    private double duration;

    public Behavior(int id, String name, Map<String, String> params, Position position, Position treeTextPosition) {
        this.id = id;
        this.name = name;
        this.params = params;
        this.position = position;
        this.treeTextPosition = treeTextPosition;
    }

    public double getTargetSpeed() {
        return targetSpeed;
    }

    public void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
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

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
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
