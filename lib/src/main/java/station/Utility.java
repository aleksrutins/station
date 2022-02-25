package station;

public interface Utility<Value> {
    void run(Value value);
    UtilityTrigger[] triggers();
}
