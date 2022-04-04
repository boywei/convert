package xodr.map.entity;

import java.util.List;

/**
 Junction结构体

 elementType = 4          表示Junction类型
 junctionId               唯一标志一个junction
 connections[]            Connection结构体数组 表示当前junction连接的 incomingRoad 和 connectingRoad

 **/

public class Junction {
    private int elementType;
    private int junctionId;
    private List<Integer> connectionsIndex;
    private List<Connection> connections;

    public int getElementType() {
        return elementType;
    }

    public void setElementType(int elementType) {
        this.elementType = elementType;
    }

    public int getJunctionId() {
        return junctionId;
    }

    public void setJunctionId(int junctionId) {
        this.junctionId = junctionId;
    }

    public List<Integer> getConnectionsIndex() {
        return connectionsIndex;
    }

    public void setConnectionsIndex(List<Integer> connectionsIndex) {
        this.connectionsIndex = connectionsIndex;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }
}
