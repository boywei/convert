package json.tree.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Behavior {

    private int id;
    private String name;
    private Map<String, Double> params;
    private Position position;
    private Position treeTextPosition;

    // 以下变量无法通过解析直接获取，需要通过计算获得
    private int level;
    private int group;
    private int number;
    private List<CommonTransition> nextTransitions;
    private List<Behavior> nextBehaviors;
    private List<BranchPoint> nextBranchPoints;

}
