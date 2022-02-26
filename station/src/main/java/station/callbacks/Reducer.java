package station.callbacks;

public interface Reducer<T, Msg> {
    T apply(T value, Msg message);
}
