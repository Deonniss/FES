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

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Введите директорию для работы");
            path = scanner.nextLine();
        } else {
            path = args[0];
        }

        String command = "";
        while (!command.equals("exit")) {
            try {
                System.out.println("Введите команду");
                command = scanner.nextLine();
                switch (command) {
                    case "help" -> help();
                    case "gen keys" -> genKeys();
                    case "encrypt" -> encryptFile();
                    case "decrypt" -> decryptFile();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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
        String pathPublic = path.concat("/public.rsa");
        String pathPrivate =  path.concat("/private.rsa");
        FileUtil.saveKeyToFile(keyPair.getPublic(), pathPublic);
        FileUtil.saveKeyToFile(keyPair.getPrivate(),pathPrivate);
        System.out.println("Публичный ключ сохранен - ".concat(pathPublic));
        System.out.println("Приватный ключ сохранен - ".concat(pathPrivate));
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
        String pathData = path.concat("/".concat(Sha256Util.hash(encryptData))).concat(".edata");
        FileUtil.saveDataToFile(encryptData, pathData);
        byte[] encryptKey = CryptoRSAUtil.encryptRSA(key.getBytes(), publicKey);
        String pathKey = path.concat("/".concat(Sha256Util.hash(encryptKey)).concat(".ekey"));
        FileUtil.saveDataToFile(encryptKey, pathKey);
        System.out.println("Зашифрованные данные сохранены - ".concat(pathData));
        System.out.println("Зашифрованный пароль сохранен - ".concat(pathKey));
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
        String pathData = path.concat("/".concat(Sha256Util.hash(encryptData)).concat(".ddata"));
        FileUtil.saveDataToFile(encryptData, pathData);
        System.out.println("Расшифрованные данные сохранены - ".concat(pathData));
    }
}
