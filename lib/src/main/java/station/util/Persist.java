package station.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import station.State;
import station.Utility;
import station.UtilityTrigger;

public class Persist<T> implements Utility<T> {
    
    private State<T, ?> stateRef;
    
    public void writeToFile(String name) {
        try (var fileOut = new FileOutputStream(name)) {
            var objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(stateRef.get());
            objOut.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T readFromFile(String name) throws ClassNotFoundException, IOException {
        T val = null;
        var fileIn = new FileInputStream(name);
        var objIn = new ObjectInputStream(fileIn);
        val = (T) objIn.readObject();
        objIn.close();
        fileIn.close();
        return val;
    }

    public Persist(State<T, ?> state) {
        stateRef = state;
    }

    public UtilityTrigger[] triggers() {
        return new UtilityTrigger[] {
            UtilityTrigger.MANUAL
        };
    }
    
}
