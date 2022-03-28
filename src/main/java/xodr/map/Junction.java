package xodr.map;

/**
 Junction结构体

 elementType = 4          表示Junction类型
 junctionId               唯一标志一个junction
 connections[]            Connection结构体数组 表示当前junction连接的 incomingRoad 和 connectingRoad

 **/

public class Junction {
    private int elementType;
    private int junctionId;
    private int[] connections;

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

    public int[] getConnections() {
        return connections;
    }

    public void setConnections(int[] connections) {
        this.connections = connections;
    }
}
