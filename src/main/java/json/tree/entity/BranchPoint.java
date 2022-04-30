package json.tree.entity;

import lombok.Data;

import java.util.List;

@Data
public class BranchPoint {

    private int id;
    private Position position;
    // 以下需通过计算获得
    private int level;
    private int group;
    private int number;
    private List<ProbabilityTransition> nextTransitions;
    private List<Behavior> nextBehaviors;

}
