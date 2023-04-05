import crypto.CryptoGOST28147;
import crypto.CryptoRSAUtil;
import crypto.KeyGenerator;
import crypto.Sha256Util;
import file.FileUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static String path;

    public static void main(String[] args) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ClassNotFoundException {

        if (args.length != 1) return;
        path = args[0];
        String command = "";
        while (!command.equals("exit")) {
            System.out.println("Введите команду");
            command = scanner.nextLine();
            switch (command) {
                case "help" -> help();
                case "gen keys" -> genKeys();
                case "encrypt" -> encryptFile();
                case "decrypt" -> decryptFile();
            }
        }
    }

    private static void help() {
        System.out.println("""
                gen keys - Генерация основного, приватного и публичного ключа\s
                decrypt [path to file] [path to public key] [key]- Шифрование данных\s
                encrypt [path to file] [path to private key] [secret key]- Дешифрование данных
                """);
    }

    private static void genKeys() throws IOException {
        KeyPair keyPair = KeyGenerator.generateRsaKeyPair();
        FileUtil.saveKeyToFile(keyPair.getPublic(), path.concat("/public.rsa"));
        FileUtil.saveKeyToFile(keyPair.getPrivate(), path.concat("/private.rsa"));
    }

    private static void encryptFile() throws IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String pathToFile, key, pathToPublicKey;
        System.out.println("Введите путь к файлу");
        pathToFile = scanner.nextLine();
        System.out.println("Введите путь к публичному ключу");
        pathToPublicKey = scanner.nextLine();
        System.out.println("Введите ключ");
        key = scanner.nextLine();
        PublicKey publicKey = (PublicKey) FileUtil.loadKeyFromFile(path.concat("/".concat(pathToPublicKey)));
        byte[] data = FileUtil.loadDataFromFile(path.concat("/".concat(pathToFile)));
        byte[] encryptData = CryptoGOST28147.encrypt(key.getBytes(), data);
        FileUtil.saveDataToFile(encryptData, path.concat("/".concat(Sha256Util.hash(encryptData))).concat(".edata"));
        byte[] encryptKey = CryptoRSAUtil.encryptRSA(key.getBytes(), publicKey);
        FileUtil.saveDataToFile(encryptKey, path.concat("/".concat(Sha256Util.hash(encryptKey)).concat(".ekey")));
    }

    private static void decryptFile() throws IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String pathToEncryptFile, secretKey, pathToPrivateKey;
        System.out.println("Введите путь к зашифрованному файлу");
        pathToEncryptFile = scanner.nextLine();
        System.out.println("Введите путь к приватному ключу");
        pathToPrivateKey = scanner.nextLine();
        System.out.println("Введите путь к зашифрованному ключу");
        secretKey = scanner.nextLine();
        byte[] keyData = FileUtil.loadDataFromFile(path.concat("/".concat(secretKey)));
        PrivateKey privateKey = (PrivateKey) FileUtil.loadKeyFromFile(path.concat("/".concat(pathToPrivateKey)));
        byte[] decryptKey = CryptoRSAUtil.decryptRSA(keyData, privateKey);
        byte[] data = FileUtil.loadDataFromFile(path.concat("/".concat(pathToEncryptFile)));
        byte[] encryptData = CryptoGOST28147.decrypt(decryptKey, data);
        FileUtil.saveDataToFile(encryptData, path.concat("/".concat(Sha256Util.hash(encryptData)).concat(".ddata")));
    }
}
