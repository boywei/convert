package map;

/**
 Lane结构体

 elementType = 3          表示Lane类型
 roadId                   当前Lane所属Road的id
 roadIndex                当前Lane所属Road的索引值
 laneSectionIndex         当前Lane所属LaneSection的索引值
 laneId                   标志当前Lane在LaneSection中的相对位置 中心线为0 左边的道路id一次递减 右边的道路id一次递增
 type                     表示当前lane是什么类型 1表示driving
 predecessorIndex         前继Lane的索引值
 successorIndex           后继Lane的索引值
 laneChange               int类型 由于车道从右到左id递增 用该变量表示当前lane是否允许变道 -1表示不知道 1表示允许向左变道 2表示允许向右变道 3表示两边都允许 4表示两边都不允许

 **/

public class Lane {
    private int elementType;
    private int roadId;
    private int roadIndex;
    private int laneSectionIndex;
    private int laneId;
    private int type;
    private int predecessorIndex;
    private int successorIndex;
    private int laneChange;

    public int getElementType() {
        return elementType;
    }

    public void setElementType(int elementType) {
        this.elementType = elementType;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public int getRoadIndex() {
        return roadIndex;
    }

    public void setRoadIndex(int roadIndex) {
        this.roadIndex = roadIndex;
    }

    public int getLaneSectionIndex() {
        return laneSectionIndex;
    }

    public void setLaneSectionIndex(int laneSectionIndex) {
        this.laneSectionIndex = laneSectionIndex;
    }

    public int getLaneId() {
        return laneId;
    }

    public void setLaneId(int laneId) {
        this.laneId = laneId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPredecessorIndex() {
        return predecessorIndex;
    }

    public void setPredecessorIndex(int predecessorIndex) {
        this.predecessorIndex = predecessorIndex;
    }

    public int getSuccessorIndex() {
        return successorIndex;
    }

    public void setSuccessorIndex(int successorIndex) {
        this.successorIndex = successorIndex;
    }

    public int getLaneChange() {
        return laneChange;
    }

    public void setLaneChange(int laneChange) {
        this.laneChange = laneChange;
    }
}