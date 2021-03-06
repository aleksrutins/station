package com.rutins.aleks.station;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.rutins.aleks.station.callbacks.Observer;
import com.rutins.aleks.station.callbacks.Reducer;

public class State<T, Msg> {
    private volatile T value;
    private Reducer<T, Msg> reducer;
    private List<Observer<T>> observers;

    private List<Utility<T>> instanceUtilities = new ArrayList<>();

    protected State(Reducer<T, Msg> reducer, T initialValue, List<Function<State<?, ?>, ? extends Utility<?>>> utilities) {
        value = initialValue;
        this.reducer = reducer;
        observers = new ArrayList<>();
        for (var utility : utilities) {
            try {
                instanceUtilities.add((Utility<T>)utility.apply(this));
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

    public synchronized CountDownLatch mutate(Msg message) {
        final var oldValue = value;
        value = reducer.apply(value, message);
        var latch = new CountDownLatch(observers.size());
        for (Observer<T> observer : observers) {
            new Thread(() -> {
                observer.react(oldValue, value);
                latch.countDown();
            }).start();
        }
        return latch;
    }

    public <U extends Utility<T>> U getUtility(Class<U> type) {
        for (Utility<T> utility : instanceUtilities) {
            if(type.equals(utility.getClass())) {
                return (U) utility;
            }
        }
        return null;
    }

    public synchronized T get() {
        return value;
    }

    @SafeVarargs
    public static <T, U extends Utility<? super T>> State<T, NullType> immutable(T value, Class<U>...utilities) {
        var constructor = constructor();
        for (var utility : utilities) {
            constructor = constructor.use(utility);
        }
        return constructor.create((val, msg) -> val, value);
    }
}
