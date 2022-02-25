package station;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import station.callbacks.Observer;
import station.callbacks.Reducer;

public class State<T, Msg> {
    private T value;
    private Reducer<T, Msg> reducer;
    private List<Observer<T>> observers;

    private List<Utility<T>> instanceUtilities = new ArrayList<>();

    protected State(Reducer<T, Msg> reducer, T initialValue, List<Supplier<? extends Utility<?>>> utilities) {
        value = initialValue;
        this.reducer = reducer;
        observers = new ArrayList<>();
        for (var utility : utilities) {
            try {
                instanceUtilities.add((Utility<T>)utility.get());
            } catch (IllegalArgumentException | SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public void observe(Observer<T> observer) {
        observers.add(observer);
    }

    public static StateConstructor constructor() {
        return new StateConstructor(new ArrayList<>());
    }

    public void mutate(Msg message) {
        final var oldValue = value;
        value = reducer.apply(value, message);
        for (Observer<T> observer : observers) {
            observer.react(oldValue, value);
        }
        for (Utility<T> utility : instanceUtilities) {
            if(Arrays.asList(utility.triggers()).contains(UtilityTrigger.OBSERVE)) {
                utility.run(value);
            }
        }
    }

    public void run(String trigger) {
        for (Utility<T> utility : instanceUtilities) {
            if(Arrays.asList(utility.triggers()).contains(UtilityTrigger.MANUAL) && utility.getClass().getName() == trigger) {
                utility.run(value);
            }
        }
    }

    public T get() {
        return value;
    }
}
