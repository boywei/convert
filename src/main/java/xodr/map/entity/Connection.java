package xodr.map.entity;

import java.util.List;

/**
 Connection结构体

 incomingRoadId          驶入Road的id
 connectingRoadId        驶出Road的id
 incomingRoadIndex       索引值 表示当前Connection连接的驶入Road
 connectingRoadIndex     索引值 表示当前Connection连接Road
 laneLinks               LaneLink结构体数组 表示当前连接Road的Lane 连接 驶入Road的Lane 的信息
 **/

public class Connection {
    private int direction; // 1左转 2直行 3右转
    private int incomingRoadId;
    private int connectingRoadId;
    private int incomingRoadIndex; //
    private int connectingRoadIndex; //
    private List<Integer> laneLinksIndex;

    private List<LaneLink> laneLinks;
    private int index;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIncomingRoadId() {
        return incomingRoadId;
    }

    public void setIncomingRoadId(int incomingRoadId) {
        this.incomingRoadId = incomingRoadId;
    }

    public int getConnectingRoadId() {
        return connectingRoadId;
    }

    public void setConnectingRoadId(int connectingRoadId) {
        this.connectingRoadId = connectingRoadId;
    }

    public int getIncomingRoadIndex() {
        return incomingRoadIndex;
    }

    public void setIncomingRoadIndex(int incomingRoadIndex) {
        this.incomingRoadIndex = incomingRoadIndex;
    }

    public int getConnectingRoadIndex() {
        return connectingRoadIndex;
    }

    public void setConnectingRoadIndex(int connectingRoadIndex) {
        this.connectingRoadIndex = connectingRoadIndex;
    }

    public List<Integer> getLaneLinksIndex() {
        return laneLinksIndex;
    }

    public void setLaneLinksIndex(List<Integer> laneLinksIndex) {
        this.laneLinksIndex = laneLinksIndex;
    }

    public List<LaneLink> getLaneLinks() {
        return laneLinks;
    }

    public void setLaneLinks(List<LaneLink> laneLinks) {
        this.laneLinks = laneLinks;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "direction=" + direction +
                ", incomingRoadId=" + incomingRoadId +
                ", connectingRoadId=" + connectingRoadId +
                ", incomingRoadIndex=" + incomingRoadIndex +
                ", connectingRoadIndex=" + connectingRoadIndex +
                ", laneLinksIndex=" + laneLinksIndex +
                ", laneLinks=" + laneLinks +
                ", index=" + index +
                '}';
    }
}
