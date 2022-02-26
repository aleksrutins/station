package station;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import station.callbacks.Reducer;

enum MessageType {
    Add,
    Subtract
}

class StateTest {
    private final Reducer<Integer, MessageType> reducer = (value, message) -> switch (message) {
        case Add -> value + 1;
        case Subtract -> value - 1;
    };
    private final State<Integer, MessageType> state = State.constructor().create(reducer, 5);

    @Test 
    void canReadDefault() {
        assertEquals(5, state.get());
    }

    @Test
    void canMutate() {
        var stateCopy = state;
        assertEquals(5, stateCopy.get());
        stateCopy.mutate(MessageType.Add);
        assertEquals(6, stateCopy.get());
        stateCopy.mutate(MessageType.Subtract);
        assertEquals(5, stateCopy.get());
    }

    @Test
    void canObserve() {
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
        stateCopy.mutate(MessageType.Subtract);
        assertEquals(true, observed.get());
        assertEquals(5, oldValue.get());
        assertEquals(4, newValue.get());
    }
}
