package map;

/**
 LaneSection结构体

 elementType = 2          表示LaneSection类型
 roadIndex                索引值 表示当前LaneSection属于那一条道路 可以在roads数组中查找
 roadId                   唯一标志当前LaneSection所属Road
 LaneSectionId            唯一标志一个LaneSection
 startPositionIndex       起始偏移位置索引值  可以在startPositions数组中找到当前LaneSection是距离Road起始位置多少m开始
 lanes                    LaneSection的道路Lane的索引值列表
 laneSectionLengthIndex   LaneSection长度索引值  可以在laneSectionLengths数组中找到道路段长度
 **/

public class LaneSection {
    private int elementType;
    private int roadIndex;
    private int roadId;
    private int laneSectionId;
    private int startPositionIndex;
    private int[] lanes;
    private int laneSectionLengthIndex;

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

    public int getStartPositionIndex() {
        return startPositionIndex;
    }

    public void setStartPositionIndex(int startPositionIndex) {
        this.startPositionIndex = startPositionIndex;
    }

    public int[] getLanes() {
        return lanes;
    }

    public void setLanes(int[] lanes) {
        this.lanes = lanes;
    }

    public int getLaneSectionLengthIndex() {
        return laneSectionLengthIndex;
    }

    public void setLaneSectionLengthIndex(int laneSectionLengthIndex) {
        this.laneSectionLengthIndex = laneSectionLengthIndex;
    }
}
