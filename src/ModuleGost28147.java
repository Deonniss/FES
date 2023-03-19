import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class ModuleGost28147 {

    private static final String ALGORITHM = "GOST28147";
    private static final String MODE = "ECB";
    private static final String PADDING = "PKCS5Padding";
    private static final String KEY = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";

    private static Cipher cipher;

    public static void main(String[] args) throws Exception {

        Security.addProvider(new BouncyCastleProvider());
        byte[] keyBytes = hexStringToByteArray(KEY);
        SecretKeySpec key = new SecretKeySpec(keyBytes, ALGORITHM);
        cipher = Cipher.getInstance(ALGORITHM + "/" + MODE + "/" + PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        encryptFile("E:\\project\\FES\\data\\124.txt", "E:\\project\\FES\\data\\124e.txt");
        cipher.init(Cipher.DECRYPT_MODE, key);
        decryptFile("E:\\project\\FES\\data\\124e.txt", "E:\\project\\FES\\data\\124d.txt");
    }

    private static void decryptFile(String inputFile, String outputFile) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {

        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] inputBytes = new byte[(int) new File(inputFile).length()];
            inputStream.read(inputBytes);
            byte[] decryptedBytes = cipher.doFinal(inputBytes);
            outputStream.write(decryptedBytes);
        }
    }

    private static void encryptFile(String inputFile, String outputFile) throws IOException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        byte[] inputBytes;
        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {

            inputBytes = new byte[(int) new File(inputFile).length()];
            inputStream.read(inputBytes);
            byte[] encryptedBytes = cipher.doFinal(inputBytes);
            outputStream.write(encryptedBytes);
            System.out.println(new String(encryptedBytes));

        }

    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
