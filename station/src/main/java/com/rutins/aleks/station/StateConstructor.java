package com.rutins.aleks.station;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.rutins.aleks.station.callbacks.Reducer;

public class StateConstructor {
    private final List<Function<State<?, ?>, ? extends Utility<?>>> utilities;

    protected StateConstructor(List<Function<State<?, ?>, ? extends Utility<?>>> initUtilities) {
        utilities = initUtilities;
    }

    public <T extends Utility<?>> StateConstructor use(Function<State<?, ?>, T> utility) {
        var utilitiesCopy = utilities;
        utilitiesCopy.add(utility);
        return new StateConstructor(utilitiesCopy);
    }

    public <T extends Utility<?>> StateConstructor use(Class<T> utility) {
        return use(state -> {
            try {
                return utility.getDeclaredConstructor(State.class).newInstance(state);
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
