package ready;

import java.io.*;
import java.security.Key;

public final class FileUtil {

    private FileUtil() {
    }

    public static void saveKeyToFile(Object key, String fileName) throws IOException {
//        File file = new File(fileName);
//        file.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(key);
        }
    }

    public static void saveDataToFile(byte[] data, String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data);
        }
    }

    public static Key loadKeyFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Key) ois.readObject();
        }
    }

    public static byte[] loadDataFromFile(String fileName) throws IOException {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            return buffer;
        }
    }
}
