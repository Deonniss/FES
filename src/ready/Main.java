package ready;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main2(String[] args) throws IOException {

        byte[] data = FileUtil.loadDataFromFile("E://project/FES/data/data.txt");
        System.out.println(data);

    }

    public static void main(String[] args) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ClassNotFoundException {
        String command = "";
        while (!command.equals("exit")) {
            System.out.println("������� �������");
            command = scanner.nextLine();
            switch (command) {
                case "help" -> help();
                case "gen keys" -> genKeys();
                case "encrypt" -> encryptFile();
                case "decrypt" -> decryptFile();
            }
        }
//E:\project\FES\data\data.txt
        //E:\project\FES\data\keys\25555890
        //E:\project\FES\data\50118069

        /*
        E:\project\FES\data\50118069
������� ���� � ���������� �����
E:\project\FES\data\keys\private.rsa
������� ���� � �������������� �����
E:\project\FES\data\keys\25555890
         */

        System.out.println("��������� ����� ����������");
        byte[] key = KeyGenerator.generateSecureKey();
        System.out.println(Arrays.toString(key));

        System.out.println("��������� ���������� � ���������� �����");
        KeyPair keyPair = KeyGenerator.generateRsaKeyPair();

        System.out.println("���������� ����� ����������");
        System.out.println("���������� ������");

    }

    private static void help() {
        System.out.println("""
                gen keys - ��������� ���������, ���������� � ���������� �����\s
                decrypt [path to file] [path to public key] [key]- ���������� ������
                """);
    }

    private static void genKeys() throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        KeyPair keyPair = KeyGenerator.generateRsaKeyPair();
        FileUtil.saveKeyToFile(keyPair.getPublic(), "E://project/FES/data/keys/public.rsa");
        FileUtil.saveKeyToFile(keyPair.getPrivate(), "E://project/FES/data/keys/private.rsa");
//        byte[] key = KeyGenerator.generateSecureKey();
//        byte[] encryptedKey = CryptoRSAUtil.encryptRSA(key, keyPair.getPublic());
//        FileUtil.saveKeyToFile(encryptedKey, "/data/keys/key");
    }

    private static void encryptFile() throws IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        String pathToFile, key, pathToPublicKey;
        System.out.println("������� ���� � �����");
        pathToFile = scanner.nextLine();
        System.out.println("������� ���� � ���������� �����");
        pathToPublicKey = scanner.nextLine();
        System.out.println("������� ����");
        key = scanner.nextLine();
        PublicKey publicKey = (PublicKey) FileUtil.loadKeyFromFile(pathToPublicKey);

        byte[] data = FileUtil.loadDataFromFile(pathToFile);
        byte[] encryptData = CryptoGOST28147.encrypt(key.getBytes(), data);
        FileUtil.saveDataToFile(encryptData, "E://project/FES/data/".concat(Sha256Util.hash(encryptData)));

        byte[] encryptKey = CryptoRSAUtil.encryptRSA(key.getBytes(), publicKey);
        FileUtil.saveDataToFile(encryptKey, "E://project/FES/data/keys/".concat(Sha256Util.hash(encryptKey)));

    }

    private static void decryptFile() throws IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        String pathToEncryptFile, secretKey, pathToPrivateKey;
        System.out.println("������� ���� � �������������� �����");
        pathToEncryptFile = scanner.nextLine();
        System.out.println("������� ���� � ���������� �����");
        pathToPrivateKey = scanner.nextLine();
        System.out.println("������� ���� � �������������� �����");
        secretKey = scanner.nextLine();



        byte[] keyData = FileUtil.loadDataFromFile(secretKey);
        PrivateKey privateKey = (PrivateKey) FileUtil.loadKeyFromFile(pathToPrivateKey);
        byte[] decryptKey = CryptoRSAUtil.decryptRSA(keyData, privateKey);


        byte[] data = FileUtil.loadDataFromFile(pathToEncryptFile);
        byte[] encryptData = CryptoGOST28147.encrypt(decryptKey, data);
        FileUtil.saveDataToFile(encryptData, "E://project/FES/data/".concat(Sha256Util.hash(encryptData)));
    }



}
