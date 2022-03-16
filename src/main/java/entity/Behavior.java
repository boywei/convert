package entity;

import java.util.Map;

public class Behavior {
    private int id;
    private String name;
    private Map<String, String> params;
    private Position position;
    private Position treeTextPosition;

    public Behavior(int id, String name, Map<String, String> params, Position position, Position treeTextPosition) {
        this.id = id;
        this.name = name;
        this.params = params;
        this.position = position;
        this.treeTextPosition = treeTextPosition;
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
