package json.tree.entity;

import java.util.List;

public class MTree {

    private List<Behavior> behaviors;
    private List<BranchPoint> branchPoints;
    private List<CommonTransition> commonTransitions;
    private List<ProbabilityTransition> probabilityTransitions;
    private int rootId;

    public MTree(List<Behavior> behaviors, List<BranchPoint> branchPoints, List<CommonTransition> commonTransitions, List<ProbabilityTransition> probabilityTransitions, int rootId) {
        this.behaviors = behaviors;
        this.branchPoints = branchPoints;
        this.commonTransitions = commonTransitions;
        this.probabilityTransitions = probabilityTransitions;
        this.rootId = rootId;
    }

    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    public void setBehaviors(List<Behavior> behaviors) {
        this.behaviors = behaviors;
    }

    public List<BranchPoint> getBranchPoints() {
        return branchPoints;
    }

    public void setBranchPoints(List<BranchPoint> branchPoints) {
        this.branchPoints = branchPoints;
    }

    public List<CommonTransition> getCommonTransitions() {
        return commonTransitions;
    }

    public void setCommonTransitions(List<CommonTransition> commonTransitions) {
        this.commonTransitions = commonTransitions;
    }

    public List<ProbabilityTransition> getProbabilityTransitions() {
        return probabilityTransitions;
    }

    public void setProbabilityTransitions(List<ProbabilityTransition> probabilityTransitions) {
        this.probabilityTransitions = probabilityTransitions;
    }

    public int getRootId() {
        return rootId;
    }

    public void setRootId(int rootId) {
        this.rootId = rootId;
    }
}
