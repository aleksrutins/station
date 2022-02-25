package station;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;

import station.callbacks.Reducer;

public class StateConstructor {
    private final List<Supplier<? extends Utility<?>>> utilities;

    protected StateConstructor(List<Supplier<? extends Utility<?>>> initUtilities) {
        utilities = initUtilities;
    }

    public <T extends Utility<?>> StateConstructor use(Supplier<T> utility) {
        var utilitiesCopy = utilities;
        utilitiesCopy.add(utility);
        return new StateConstructor(utilitiesCopy);
    }

    public <T extends Utility<?>> StateConstructor use(Class<T> utility) {
        return use(() -> {
            try {
                return utility.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        });
    }

    public <T, Msg> State<T, Msg> create(Reducer<T, Msg> reducer, T initialValue) {
        return new State<T,Msg>(reducer, initialValue, utilities);
    }
}
