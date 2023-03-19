import java.io.*;

import java.security.*;
import java.security.KeyPair;

public class KeyToFileExample {

    public static void savePublicKeyToFile(PublicKey publicKey, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(publicKey);
        oos.close();
        fos.close();
    }

    public static void savePrivateKeyToFile(PrivateKey privateKey, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(privateKey);
        oos.close();
        fos.close();
    }

    public static PublicKey loadPublicKeyFromFile(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        PublicKey publicKey = (PublicKey) ois.readObject();
        ois.close();
        fis.close();
        return publicKey;
    }

    public static PrivateKey loadPrivateKeyFromFile(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        PrivateKey privateKey = (PrivateKey) ois.readObject();
        ois.close();
        fis.close();
        return privateKey;
    }

    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        savePublicKeyToFile(publicKey, "public.key");
        savePrivateKeyToFile(privateKey, "private.key");

        PublicKey loadedPublicKey = loadPublicKeyFromFile("public.key");
        PrivateKey loadedPrivateKey = loadPrivateKeyFromFile("private.key");

        System.out.println("Original Public Key: " + publicKey);
        System.out.println("Loaded Public Key: " + loadedPublicKey);
        System.out.println("Original Private Key: " + privateKey);
        System.out.println("Loaded Private Key: " + loadedPrivateKey);
    }
}
