package com.rutins.aleks.station.util;

public interface Message {
    public static interface WithValue<M, V> {
        public M message();
        public V value();
    }
    public static <M, V> WithValue<M, V> withValue(M message, V value) {
        return new WithValue<>() {

            @Override
            public M message() {
                return message;
            }

            @Override
            public V value() {
                return value;
            }
            
        };
    }
}
