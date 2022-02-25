package station.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;

import station.Utility;
import station.UtilityTrigger;

public class Persist implements Utility<Object> {
    
    private Consumer<Object> fn;
    
    public static Consumer<Object> ToFile(String name) {
        return obj -> {
            try (var fileOut = new FileOutputStream(name)) {
                var objOut = new ObjectOutputStream(fileOut);
                objOut.writeObject(obj);
                objOut.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public static <T> T getFromFile(String name) throws ClassNotFoundException, IOException {
        T val = null;
        var fileIn = new FileInputStream(name);
        var objIn = new ObjectInputStream(fileIn);
        val = (T) objIn.readObject();
        objIn.close();
        fileIn.close();
        return val;
    }

    public Persist(Consumer<Object> persistFn) {
        fn = persistFn;
    }

    public void run(Object value) {
        fn.accept(value);
    }

    public UtilityTrigger[] triggers() {
        return new UtilityTrigger[] {
            UtilityTrigger.MANUAL
        };
    }
    
}
