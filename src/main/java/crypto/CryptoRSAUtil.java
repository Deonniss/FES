package crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public final class CryptoRSAUtil {

    private static final String ALGORITHM = "RSA";
    private static final String MODE = "ECB";
    private static final String PADDING = "OAEPWithSHA-256AndMGF1Padding";
    private static final Cipher cipher;

    static {
        try {
            cipher = Cipher.getInstance(ALGORITHM + "/" + MODE + "/" + PADDING);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptRSA(byte[] data, PublicKey publicKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptRSA(byte[] data, PrivateKey privateKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }
}
