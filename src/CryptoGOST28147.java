import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class CryptoGOST28147 {

    private static final String ALGORITHM = "GOST28147";
    private static final String MODE = "ECB";
    private static final String PADDING = "PKCS5Padding";
    private static final String KEY_STRING = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";
    private static final SecretKeySpec KEY_SPEC;
    private static final Cipher cipher;

    static {
        try {
            cipher = Cipher.getInstance(ALGORITHM + "/" + MODE + "/" + PADDING);
            Security.addProvider(new BouncyCastleProvider());
            byte[] keyBytes = hexStringToByteArray(KEY_STRING);
            KEY_SPEC = new SecretKeySpec(keyBytes, ALGORITHM);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(byte[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, KEY_SPEC);
        return cipher.doFinal(data);
    }

    public static byte[] decryptRSA(byte[] data, PrivateKey privateKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
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
