package station.callbacks;

public interface Observer<T> {
    void react(T oldValue, T value);
}
