package ready;

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
    private static final Cipher cipher;

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
            cipher = Cipher.getInstance(ALGORITHM + "/" + MODE + "/" + PADDING);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(byte[] key, byte[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec keySpec = new SecretKeySpec(fillKey(key), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    public static byte[] decryptRSA(byte[] key, byte[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec keySpec = new SecretKeySpec(fillKey(key), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    private static byte[] fillKey(byte[] key) {
        if (key.length > 32) {
            throw new RuntimeException("Key length invalid. Key needs to be 32 byte - 256 bit!!!");
        } else if (key.length < 32) {
            byte[] newKey = new byte[32];
            System.arraycopy(key, 0, newKey, 0, key.length);
            for (int i = key.length; i < newKey.length; i++) {
                newKey[i] = (byte) 0;
            }
            return newKey;
        }
        return key;
    }
}
