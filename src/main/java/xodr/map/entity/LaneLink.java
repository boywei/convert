package xodr.map.entity;

/**
 LaneLink结构体

 from                    表示当前LaneLink连接的 驶入lane的下标索引
 to                      表示当前LaneLink连接的 驶出lane的下标索引
 laneLinks               表示当前连接Road的Lane 连接 驶入Road的Lane的信息
 **/
public class LaneLink {
    private int from;
    private int to;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
