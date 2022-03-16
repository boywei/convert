package entity;

public class MTree {

    private Behavior[] behaviors;
    private BranchPoint[] branchPoints;
    private CommonTransition[] commonTransitions;
    private ProbabilityTransition[] probabilityTransitions;
    private int rootId;

    public MTree(Behavior[] behaviors, BranchPoint[] branchPoints, CommonTransition[] commonTransitions, ProbabilityTransition[] probabilityTransitions, int rootId) {
        this.behaviors = behaviors;
        this.branchPoints = branchPoints;
        this.commonTransitions = commonTransitions;
        this.probabilityTransitions = probabilityTransitions;
        this.rootId = rootId;
    }

    public Behavior[] getBehaviors() {
        return behaviors;
    }

    public void setBehaviors(Behavior[] behaviors) {
        this.behaviors = behaviors;
    }

    public BranchPoint[] getBranchPoints() {
        return branchPoints;
    }

    public void setBranchPoints(BranchPoint[] branchPoints) {
        this.branchPoints = branchPoints;
    }

    public CommonTransition[] getCommonTransitions() {
        return commonTransitions;
    }

    public void setCommonTransitions(CommonTransition[] commonTransitions) {
        this.commonTransitions = commonTransitions;
    }

    public ProbabilityTransition[] getProbabilityTransitions() {
        return probabilityTransitions;
    }

    public void setProbabilityTransitions(ProbabilityTransition[] probabilityTransitions) {
        this.probabilityTransitions = probabilityTransitions;
    }

    public int getRootId() {
        return rootId;
    }

    public void setRootId(int rootId) {
        this.rootId = rootId;
    }
}
