package com.rutins.aleks.station.callbacks;

public interface Observer<T> {
    void react(T oldValue, T value);
}
