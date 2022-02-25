package station;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import station.callbacks.Reducer;

class UtilityTest {
    private final Reducer<Integer, MessageType> reducer = (value, message) -> switch (message) {
        case Add -> value + 1;
        case Subtract -> value - 1;
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
                    .use(() -> new TestUtility<Integer>())
                    .create(reducer, 5);
        assertEquals(5, localState.get());
        localState.mutate(MessageType.Add);
        assertEquals(6, localState.get());
    }
}
