package com.rutins.aleks.station;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.rutins.aleks.station.callbacks.Reducer;

import org.junit.jupiter.api.Test;

class TestUtility<V> implements Utility<V> {

    public void run(V value) {
        assertNotNull(value);
        assertInstanceOf(Integer.class, value);
        assertEquals(6, value);
    }

    public UtilityTrigger[] triggers() {
        return new UtilityTrigger[] {
            UtilityTrigger.OBSERVE
        };
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
    void canUseUtilities() {
        var localState = State.constructor()
                        .use(TestUtility.class)
                        .create(reducer, 5);
        assertEquals(5, localState.get());
        localState.mutate(MessageType.Add);
        assertEquals(6, localState.get());

        localState = State.constructor()
                    .use(state -> new TestUtility<Integer>())
                    .create(reducer, 5);
        assertEquals(5, localState.get());
        localState.mutate(MessageType.Add);
        assertEquals(6, localState.get());
    }
}
