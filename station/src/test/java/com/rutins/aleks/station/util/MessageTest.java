package com.rutins.aleks.station.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.rutins.aleks.station.State;

import org.junit.jupiter.api.Test;

class MessageTest {
    enum MessageType {
        Add, Subtract
    }
    @Test
    void canSendValue() throws InterruptedException {
        var state = State.constructor().<Integer, Message.WithValue<MessageType, Integer>>create((current, message) -> {
            switch(message.message()) {
                case Add:
                    return current + message.value();
                case Subtract:
                    return current - message.value();
            }
            return current;
        }, 5);
        assertEquals(state.get(), 5);
        state.mutate(Message.withValue(MessageType.Add, 6)).await();
        assertEquals(state.get(), 11);
        state.mutate(Message.withValue(MessageType.Subtract, 5)).await();
        assertEquals(state.get(), 6);
    }
}
