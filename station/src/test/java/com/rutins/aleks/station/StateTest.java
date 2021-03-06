package com.rutins.aleks.station;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.rutins.aleks.station.callbacks.Reducer;

import org.junit.jupiter.api.Test;

enum MessageType {
    Add,
    Subtract
}

class StateTest {
    private final Reducer<Integer, MessageType> reducer = (value, message) -> {
        switch (message) {
            case Add: return Integer.valueOf(value + 1);
            case Subtract: return Integer.valueOf(value - 1);
            default: return value;
        }
    };
    private final State<Integer, MessageType> state = State.constructor().create(reducer, 5);

    @Test 
    void canReadDefault() {
        assertEquals(5, state.get());
    }

    @Test
    void canMutate() throws InterruptedException {
        var stateCopy = state;
        assertEquals(5, stateCopy.get());
        stateCopy.mutate(MessageType.Add).await();
        assertEquals(6, stateCopy.get());
        stateCopy.mutate(MessageType.Subtract).await();
        assertEquals(5, stateCopy.get());
    }

    @Test
    void canObserve() throws InterruptedException {
        var stateCopy = state;
        var observed = new AtomicBoolean(false);
        var oldValue = new AtomicInteger();
        var newValue = new AtomicInteger();
        assertEquals(false, observed.get());
        assertEquals(5, stateCopy.get());
        stateCopy.observe((oldValueObserved, value) -> {
            observed.set(true);
            oldValue.set(oldValueObserved);
            newValue.set(value);
        });
        stateCopy.mutate(MessageType.Subtract).await();
        assertEquals(true, observed.get());
        assertEquals(5, oldValue.get());
        assertEquals(4, newValue.get());
    }

    @Test
    void canThread() throws InterruptedException {
        var stateCopy = state;
        var latch = new CountDownLatch(2);
        var thread1 = new Thread(() -> {
            stateCopy.mutate(MessageType.Add);
            stateCopy.get();
            stateCopy.mutate(MessageType.Subtract);
            stateCopy.get();
            latch.countDown();
        });
        var thread2 = new Thread(() -> {
            stateCopy.mutate(MessageType.Add);
            stateCopy.get();
            stateCopy.mutate(MessageType.Subtract);
            stateCopy.get();
            latch.countDown();
        });
        thread1.start();
        thread2.start();
        latch.await();
    }
}
