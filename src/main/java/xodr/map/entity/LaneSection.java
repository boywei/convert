package xodr.map.entity;

import java.util.List;

/**
 LaneSection结构体

 elementType = 2          表示LaneSection类型
 roadIndex                索引值 表示当前LaneSection属于那一条道路 可以在roads数组中查找
 roadId                   唯一标志当前LaneSection所属Road
 LaneSectionId            唯一标志一个LaneSection
 startPosition            起始偏移位置, 距离Road起始位置多少m开始
 lanes                    LaneSection的道路Lane的索引值列表
 length                   LaneSection长度
 **/

public class LaneSection {
    private int elementType;
    private int roadIndex;
    private int roadId;
    private int laneSectionId;
    private double startPosition;
    private List<Integer> lanesIndex;

    private double length;
    private List<Lane> lanes;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getElementType() {
        return elementType;
    }

    public void setElementType(int elementType) {
        this.elementType = elementType;
    }

    public int getRoadIndex() {
        return roadIndex;
    }

    public void setRoadIndex(int roadIndex) {
        this.roadIndex = roadIndex;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public int getLaneSectionId() {
        return laneSectionId;
    }

    public void setLaneSectionId(int laneSectionId) {
        this.laneSectionId = laneSectionId;
    }

    public double getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(double startPosition) {
        this.startPosition = startPosition;
    }

    public List<Integer> getLanesIndex() {
        return lanesIndex;
    }

    public void setLanesIndex(List<Integer> lanesIndex) {
        this.lanesIndex = lanesIndex;
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "LaneSection{" +
                "elementType=" + elementType +
                ", roadIndex=" + roadIndex +
                ", roadId=" + roadId +
                ", laneSectionId=" + laneSectionId +
                ", startPosition=" + startPosition +
                ", lanesIndex=" + lanesIndex +
                ", length=" + length +
                ", lanes=" + lanes +
                ", index=" + index +
                '}';
    }
}
