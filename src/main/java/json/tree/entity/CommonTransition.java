package json.tree.entity;

import lombok.Data;

import java.util.List;

@Data
public class CommonTransition {

    private List<String> guards;
    private int id;
    private List<Position> linkPoints;
    private int sourceId;
    private int targetId;
    private Position treeTextPosition;
    // 以下变量通过计算获得
    private int level;
    private int group;
    private int number;
    private Behavior sourceBehavior;
    private Behavior targetBehavior;
    private BranchPoint targetBranchPoint;

}
