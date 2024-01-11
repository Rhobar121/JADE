package agents;

public enum CarActions {
    FIND,
    BUY,
    CANCEL,
    APPROVE,
    DENIAL,
    PAY;

    public static CarActions fromString(String val){
        return CarActions.valueOf(val);
    }
}
