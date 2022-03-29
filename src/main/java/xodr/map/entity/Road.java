package xodr.map.entity;

/**
 Road的结构体

 elementType = 1        表示Road类型
 roadId                 唯一标识一条Road
 junctionId             junction的id 表示当前Road在那一个junction中作为连接路   -1表示不是连接路 不属于任何一条junction
 junctionIndex          索引值 用于在junction数组中索引
 roadLengthIndex        路的总长度索引   根据这个索引值可以在roadLengths数组中找到路的总长度
 predecessorElementType predecessor的类型 根据这个类型 在不同的数组中进行索引
 predecessorIndex       索引值 表示当前Road的前继 可以是Road 也可以是一个junction  需要配合predecessorElementType在对应的类型数组中索引    用-1表示空
 successorElementType   successor的类型 根据这个类型 在不同的数组中进行索引
 successorIndex         索引值 表示当前Road的后继 可以是Road 也可以是一个junction  需要配合successorElementType在对应的类型数组中索引    用-1表示空
 maxSpeedIndex          路的最大限速索引 根据这个索引可以在roadMaxSpeeds数组中找到路的最大限速
 laneSections[]         表示当前Road中的道路段索引数组  道路段的顺序就是数组的顺序   道路段中又有很多不同的道路Lane
 **/
public class Road {
    private int elementType;
    private int roadId;
    private int junctionIndex;
    private int junctionId;
    private int roadLengthIndex;
    private int predecessorElementType;
    private int predecessorIndex;
    private int successorElementType;
    private int successorIndex;
    private int maxSpeedIndex;
    private int[] laneSections;

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

    public int getJunctionIndex() {
        return junctionIndex;
    }

    public void setJunctionIndex(int junctionIndex) {
        this.junctionIndex = junctionIndex;
    }

    public int getJunctionId() {
        return junctionId;
    }

    public void setJunctionId(int junctionId) {
        this.junctionId = junctionId;
    }

    public int getRoadLengthIndex() {
        return roadLengthIndex;
    }

    public void setRoadLengthIndex(int roadLengthIndex) {
        this.roadLengthIndex = roadLengthIndex;
    }

    public int getPredecessorElementType() {
        return predecessorElementType;
    }

    public void setPredecessorElementType(int predecessorElementType) {
        this.predecessorElementType = predecessorElementType;
    }

    public int getPredecessorIndex() {
        return predecessorIndex;
    }

    public void setPredecessorIndex(int predecessorIndex) {
        this.predecessorIndex = predecessorIndex;
    }

    public int getSuccessorElementType() {
        return successorElementType;
    }

    public void setSuccessorElementType(int successorElementType) {
        this.successorElementType = successorElementType;
    }

    public int getSuccessorIndex() {
        return successorIndex;
    }

    public void setSuccessorIndex(int successorIndex) {
        this.successorIndex = successorIndex;
    }

    public int getMaxSpeedIndex() {
        return maxSpeedIndex;
    }

    public void setMaxSpeedIndex(int maxSpeedIndex) {
        this.maxSpeedIndex = maxSpeedIndex;
    }

    public int[] getLaneSections() {
        return laneSections;
    }

    public void setLaneSections(int[] laneSections) {
        this.laneSections = laneSections;
    }
}
