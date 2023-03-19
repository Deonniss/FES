import javax.crypto.Cipher;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public final class CryptoUtil {

    private static final String ALGORITHM = "GOST28147";
    private static final String MODE = "ECB";
    private static final String PADDING = "PKCS5Padding";
    private static final String KEY = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";
    private static final Cipher cipher;

    static {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
