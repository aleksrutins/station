package com.rutins.aleks.station;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.rutins.aleks.station.callbacks.Reducer;

import org.junit.jupiter.api.Test;

class TestUtility<V> implements Utility<V> {

    public TestUtility(State<V, ?> state) {
        state.observe(this::hook);
    }

    public void hook(V oldValue, V value) {
        assertNotNull(value);
        assertInstanceOf(Integer.class, value);
        assertEquals(6, value);
    }

}

class UtilityTest {
    private final Reducer<Integer, MessageType> reducer = (value, message) -> {
        switch (message) {
            case Add: return Integer.valueOf(value + 1);
            case Subtract: return Integer.valueOf(value - 1);
            default: return value;
        }
    };
    @Test
    void canUseUtilities() throws InterruptedException {
        var localState = State.constructor()
                        .use(TestUtility.class)
                        .create(reducer, 5);
        assertEquals(5, localState.get());
        localState.mutate(MessageType.Add).await();
        assertEquals(6, localState.get());

        localState = State.constructor()
                    .use(state -> new TestUtility<>(state))
                    .create(reducer, 5);
        assertEquals(5, localState.get());
        localState.mutate(MessageType.Add).await();
        assertEquals(6, localState.get());
    }
}
