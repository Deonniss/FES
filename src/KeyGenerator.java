import java.security.*;

public final class KeyGenerator {

    private static final KeyPairGenerator keyPairGenerator;

    static {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private KeyGenerator() {}

    public static KeyPair generateKeyPair() {
        return keyPairGenerator.generateKeyPair();
    }

}
