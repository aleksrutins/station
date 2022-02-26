package com.rutins.aleks.station.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import javax.lang.model.type.NullType;

import com.rutins.aleks.station.State;

import org.junit.jupiter.api.Test;

class PersistTest {
    @Test
    void canPersistState() throws ClassNotFoundException, IOException {
        State<Integer, NullType> state = State.immutable(Integer.valueOf(10), Persist.class);
        state.getUtility(Persist.class).writeToFile("/tmp/test-persist.ser");
        assertEquals(Persist.<Integer>readFromFile("/tmp/test-persist.ser").intValue(), 10);
    }

    @Test
    void canPersistDirectConstructor() throws ClassNotFoundException, IOException {
        new Persist<>(State.immutable(15)).writeToFile("/tmp/test-persist.ser");
        assertEquals(Persist.<Integer>readFromFile("/tmp/test-persist.ser").intValue(), 15);
    }
}
