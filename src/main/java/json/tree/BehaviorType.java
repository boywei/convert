package json.tree;

/**
 * @author wei
 * @description TODO
 * @date 2022-04-03 16:40
 */
public enum BehaviorType {

    ACCELERATE("Accelerate"),
    DECELERATE("Decelerate"),
    KEEP("Keep"),
    TURN_LEFT("TurnLeft"),
    TURN_RIGHT("TurnRight"),
    CHANGE_LEFT("ChangeLeft"),
    CHANGE_RIGHT("ChangeRight"),
    IDLE("Idle")
    ;

    private String value;

    BehaviorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
