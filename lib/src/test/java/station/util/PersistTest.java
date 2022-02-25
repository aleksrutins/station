package station.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class PersistTest {
    @Test
    void canPersist() throws ClassNotFoundException, IOException {
        new Persist(Persist.ToFile("/tmp/test-persist.ser")).run(Integer.valueOf(10));
        assertEquals(Persist.<Integer>getFromFile("/tmp/test-persist.ser").intValue(), 10);
    }
}
