package json.tree.entity;

import lombok.Data;

import java.util.List;

@Data
public class MTree {

    private List<Behavior> behaviors;
    private List<BranchPoint> branchPoints;
    private List<CommonTransition> commonTransitions;
    private List<ProbabilityTransition> probabilityTransitions;
    private int rootId;

}
