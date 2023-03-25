package ready;

import java.security.*;

public final class KeyGenerator {

    private static final KeyPairGenerator keyPairGenerator;
    private static final SecureRandom secureRandom;

    static {
        try {
            secureRandom = SecureRandom.getInstanceStrong();
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private KeyGenerator() {}

    public static KeyPair generateRsaKeyPair() {
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] generateSecureKey() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return bytes;
    }
}
