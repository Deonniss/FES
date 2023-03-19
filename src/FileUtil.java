import java.io.*;
import java.security.Key;

public final class FileUtil {

    private FileUtil() {
    }

    public static void saveKeyToFile(Key key, String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(key);
        }
    }

    public static Key loadKeyFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis);) {
            return (Key) ois.readObject();
        }
    }
}
