package json.importer;

import com.alibaba.fastjson.JSONObject;
import json.tree.TreeDataContainer;
import json.tree.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONParser {

    private static final int N = 3;

    private static List<Behavior> behaviorList;
    private static List<CommonTransition> commonTransitionList;
    private static List<ProbabilityTransition> probabilityTransitionList;
    private static List<BranchPoint> branchPointList;

    // 从locationParams初始化car的道路信息 TODO: 道路索引初始化
    private static void initLocationParams(Car car) {
        Map<String, Double> locationParams = car.getLocationParams();
        car.setRoadId((int) Math.round(locationParams.get("road id")));
        car.setLaneId((int) Math.round(locationParams.get("lane id")));
        car.setMinOffset(locationParams.get("min longitudinal offset"));
        car.setMaxOffset(locationParams.get("max longitudinal offset"));

        System.out.println(car.getName() + "道路信息初始化完成："
                + "road id: " + car.getRoadId()
                + ", lane id: " + car.getLaneId()
                + ", min offset: " + car.getMinOffset()
                + ", max offset: " + car.getMaxOffset());
    }

    // 将所有id去重，并重新分配
    private static void modifyId(Car car) {
        int id = 1;
        int locationId = 1;
        Map<Integer, Integer> ids = new HashMap<>(); // 原id -> 去重后id
        Map<String, Integer> locationIds = new HashMap<>(); // 行为（Location）对应的id

        for(Behavior behavior : car.getmTree().getBehaviors()) {
            if(!locationIds.containsKey(behavior.getName())) {
                locationIds.put(behavior.getName(), locationId);
                ids.put(behavior.getId(), locationId);
                behavior.setId(locationId);
                locationId++;
                id++;
            } else {
                int nowId = locationIds.get(behavior.getName());
                ids.put(behavior.getId(), nowId);
                behavior.setId(nowId);
            }
        }

        for(BranchPoint branchPoint : car.getmTree().getBranchPoints()) {
            ids.put(branchPoint.getId(), id);
            branchPoint.setId(id++);
        }

        for(CommonTransition commonTransition : car.getmTree().getCommonTransitions()) {
            ids.put(commonTransition.getId(), id);
            commonTransition.setId(id++);
            commonTransition.setSourceId(ids.get(commonTransition.getSourceId()));
            commonTransition.setTargetId(ids.get(commonTransition.getTargetId()));
        }

        for(ProbabilityTransition probabilityTransition : car.getmTree().getProbabilityTransitions()) {
            ids.put(probabilityTransition.getId(), id);
            probabilityTransition.setId(id++);
            probabilityTransition.setSourceId(ids.get(probabilityTransition.getSourceId()));
            probabilityTransition.setTargetId(ids.get(probabilityTransition.getTargetId()));
        }

    }

    private static void initEdge2(Car car) {
        MTree mTree = car.getmTree();
        List<Behavior> behaviors = mTree.getBehaviors();
        List<CommonTransition> commonTransitions = mTree.getCommonTransitions();
        List<ProbabilityTransition> probabilityTransitions = mTree.getProbabilityTransitions();
        List<BranchPoint> branchPoints = mTree.getBranchPoints();

        behaviorList = new ArrayList<>();
        commonTransitionList = new ArrayList<>();
        probabilityTransitionList = new ArrayList<>();
        branchPointList = new ArrayList<>();

        // 0. init idMap
        // id -> Behavior
        Map<Integer, Behavior> idBehaviorMap = new HashMap<>();
        Map<Integer, BranchPoint> idBranchPointMap = new HashMap<>();
        for(Behavior behavior : behaviors) {
            idBehaviorMap.put(behavior.getId(), behavior);
        }
        for(BranchPoint branchPoint : branchPoints) {
            idBranchPointMap.put(branchPoint.getId(), branchPoint);
        }

        // 1. init next[]
        for(Behavior behavior : behaviors) {
            behavior.setNextBehaviors(new ArrayList<>());
            behavior.setNextTransitions(new ArrayList<>());
            behavior.setNextBranchPoints(new ArrayList<>());
            // 以该behavior id为source的边
            for(CommonTransition commonTransition : commonTransitions) {
                if(behavior.getId() == commonTransition.getSourceId()) {
                    // next commonTransition
                    commonTransition.setSourceBehavior(behavior);
                    if(idBehaviorMap.containsKey(commonTransition.getTargetId())) { // next behavior
                        Behavior behavior1 = idBehaviorMap.get(commonTransition.getTargetId());
                        commonTransition.setTargetBehavior(behavior1);
                        List<Behavior> behaviorList = behavior.getNextBehaviors();
                        behaviorList.add(behavior1);
                        behavior.setNextBehaviors(behaviorList);
                    } else { // next branchPoint
                        BranchPoint branchPoint = idBranchPointMap.get(commonTransition.getTargetId());
                        commonTransition.setTargetBranchPoint(branchPoint);
                        List<BranchPoint> branchPointList = behavior.getNextBranchPoints();
                        branchPointList.add(branchPoint);
                        behavior.setNextBranchPoints(branchPointList);
                    }
                    List<CommonTransition> commonTransitionList = behavior.getNextTransitions();
                    commonTransitionList.add(commonTransition);
                    behavior.setNextTransitions(commonTransitionList);
                }
            }
        }

        for(BranchPoint branchPoint : branchPoints) {
            branchPoint.setNextBehaviors(new ArrayList<>());
            branchPoint.setNextTransitions(new ArrayList<>());
            for(ProbabilityTransition probabilityTransition : probabilityTransitions) {
                if(branchPoint.getId() == probabilityTransition.getSourceId()) {
                    // next probabilityTransition
                    probabilityTransition.setSourceBranchPoint(branchPoint);
                    if(idBehaviorMap.containsKey(probabilityTransition.getTargetId())) {
                        Behavior behavior = idBehaviorMap.get(probabilityTransition.getTargetId());
                        probabilityTransition.setTargetBehavior(behavior);
                        List<Behavior> behaviorList = branchPoint.getNextBehaviors();
                        behaviorList.add(behavior);
                        branchPoint.setNextBehaviors(behaviorList);
                    }
                    List<ProbabilityTransition> probabilityTransitionList = branchPoint.getNextTransitions();
                    probabilityTransitionList.add(probabilityTransition);
                    branchPoint.setNextTransitions(probabilityTransitionList);
                }
            }
        }

        // 2. build
        // root
        for(Behavior behavior : behaviors) {
            if(behavior.getId() == mTree.getRootId()) {
                behavior.setLevel(1);
                behavior.setGroup(1);
                behavior.setNumber(0);
                behaviorList.add(behavior);
                buildTree2(behavior);
                break;
            }
        }

        // 3. 加入到全局变量中
        mTree.setProbabilityTransitions(probabilityTransitionList);
        mTree.setBranchPoints(branchPointList);
        mTree.setCommonTransitions(commonTransitionList);
        mTree.setBehaviors(behaviorList);
    }

    // 递归初始化
    private static void buildTree2(Behavior sourceBehavior) {
        int number = 1;
        // 初始化 以该behavior为source的边
        for(CommonTransition commonTransition : sourceBehavior.getNextTransitions()) {
            // 更改对应边的三元组
            commonTransition.setLevel(sourceBehavior.getLevel());
            commonTransition.setGroup(sourceBehavior.getGroup());
            commonTransition.setNumber(number);
            commonTransitionList.add(commonTransition);
            number ++;
            initCommonTransition(commonTransition);
        }

    }

    private static void initCommonTransition(CommonTransition commonTransition) {
        if(commonTransition.getTargetBehavior() != null) {
            Behavior behavior = commonTransition.getTargetBehavior();
            // 更改对应behavior的三元组
            behavior.setLevel(commonTransition.getLevel() + 1);
            behavior.setGroup((commonTransition.getGroup() - 1) * N + commonTransition.getNumber());
            behavior.setNumber(0);
            behaviorList.add(behavior);
            buildTree2(behavior);
        } else if(commonTransition.getTargetBranchPoint() != null) {
            BranchPoint branchPoint = commonTransition.getTargetBranchPoint();
            // 更改对应branchPoint的三元组
            branchPoint.setLevel(commonTransition.getLevel() + 1);
            branchPoint.setGroup((commonTransition.getGroup() - 1) * N + commonTransition.getNumber());
            branchPoint.setNumber(0);
            branchPointList.add(branchPoint);
            initBranchPoint(branchPoint);
        }
    }

    private static void initBranchPoint(BranchPoint branchPoint) {
        int number = 1;
        for(ProbabilityTransition probabilityTransition : branchPoint.getNextTransitions()) {
            // 更改对应边的三元组
            probabilityTransition.setLevel(branchPoint.getLevel());
            probabilityTransition.setGroup(branchPoint.getGroup());
            probabilityTransition.setNumber(number);
            probabilityTransitionList.add(probabilityTransition);
            number ++;
            initProbabilityTransition(probabilityTransition);
        }
    }

    private static void initProbabilityTransition(ProbabilityTransition probabilityTransition) {
        if(probabilityTransition.getTargetBehavior() != null) {
            // 更改该behavior对应的三元组
            Behavior behavior = probabilityTransition.getTargetBehavior();
            behavior.setLevel(probabilityTransition.getLevel() + 1);
            behavior.setGroup((probabilityTransition.getGroup() - 1) * N + probabilityTransition.getNumber());
            behavior.setNumber(0);
            behaviorList.add(behavior);
            buildTree2(behavior);
        }
    }

    // 初始化各边和各自环对应的三元组
    private static void initEdge(Car car) {
        List<Behavior> behaviors = car.getmTree().getBehaviors();
        List<CommonTransition> commonTransitions = car.getmTree().getCommonTransitions();
        List<ProbabilityTransition> probabilityTransitions = car.getmTree().getProbabilityTransitions();
        List<BranchPoint> branchPoints = car.getmTree().getBranchPoints();

        behaviors.get(0).setLevel(1);
        behaviors.get(0).setGroup(1);
        behaviors.get(0).setNumber(0);
        buildTree(behaviors.get(0), behaviors, commonTransitions, probabilityTransitions, branchPoints);

        MTree mTree = car.getmTree();
        mTree.setBehaviors(behaviors);
        mTree.setCommonTransitions(commonTransitions);
        mTree.setBranchPoints(branchPoints);
    }

    // 递归初始化
    private static void buildTree(Behavior sourceBehavior, List<Behavior> behaviors, List<CommonTransition> commonTransitions,
                                  List<ProbabilityTransition> probabilityTransitions, List<BranchPoint> branchPoints) {
        int number = 1;
        // 找出以该behavior为source的边
        for(CommonTransition commonTransition : commonTransitions) {
            if(commonTransition.getSourceId() == sourceBehavior.getId()) {
                // 更改对应边的三元组
                commonTransition.setLevel(sourceBehavior.getLevel());
                commonTransition.setGroup(sourceBehavior.getGroup());
                commonTransition.setNumber(number);
                number ++;
                // 找出该边targetId对应的behavior
                for(Behavior behavior : behaviors) {
                    if(behavior.getId() == commonTransition.getTargetId()) {
                        // 更改对应behavior的三元组
                        behavior.setLevel(commonTransition.getLevel() + 1);
                        behavior.setGroup((commonTransition.getGroup() - 1) * N + commonTransition.getNumber());
                        behavior.setNumber(0);
                        buildTree(behavior, behaviors, commonTransitions, probabilityTransitions, branchPoints);
                    }
                }
                // 找出该边targetId对应的branchPoint
                for(BranchPoint branchPoint: branchPoints) {
                    if(branchPoint.getId() == commonTransition.getTargetId()) {
                        // 更改对应branchPoint的三元组
                        branchPoint.setLevel(commonTransition.getLevel() + 1);
                        branchPoint.setGroup((commonTransition.getGroup() - 1) * N + commonTransition.getNumber());
                        branchPoint.setNumber(0);
                        number ++;
                        // 找出以该branchPoint为source的commonTransition
                        int number2 = 1;
                        for(ProbabilityTransition probabilityTransition : probabilityTransitions) {
                            if(probabilityTransition.getSourceId() == branchPoint.getId()) {
                                // 更改对应边的三元组
                                probabilityTransition.setLevel(branchPoint.getLevel());
                                probabilityTransition.setGroup(branchPoint.getGroup());
                                probabilityTransition.setNumber(number2);
                                number2 ++;
                                // 找出该边的targetId对应的behavior
                                int number3 = 1;
                                for(Behavior behavior : behaviors) {
                                    if(behavior.getId() == probabilityTransition.getTargetId()) {
                                        // 更改该behavior对应的三元组
                                        behavior.setLevel(probabilityTransition.getLevel() + 1);
                                        behavior.setGroup((probabilityTransition.getGroup() - 1) * N + probabilityTransition.getNumber());
                                        behavior.setNumber(number3);
                                        number3 ++;
                                        buildTree(behavior, behaviors, commonTransitions, probabilityTransitions, branchPoints);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static TreeDataContainer parse(String input, String treePathPrefix) {
        System.out.println("Parsing input...");


        TreeDataContainer container = JSONObject.parseObject(input, TreeDataContainer.class);

        List<Car> cars = new ArrayList<>();
        for(Car car : container.getCars()) {
            // 从路径中获取行为树
            String treeStr = JSONInputReader.readFromFile(treePathPrefix + car.getTreePath());
            MTree tree = JSONObject.parseObject(treeStr, MTree.class);
            car.setmTree(tree);
            // 初始化location
            initLocationParams(car);
            // 初始化（level, group, number）
            initEdge2(car);
            // 需要对id进行去重并更改，将同名节点归为同一id，否则会有重名节点
            modifyId(car);

            cars.add(car);
        }
        container.setCars(cars);

        System.out.println("Finishing parsing...");
        return container;
    }

}
