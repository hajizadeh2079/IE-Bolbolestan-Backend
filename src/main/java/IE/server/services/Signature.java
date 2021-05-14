package IE.server.services;

public class Signature {
    public static byte[] getSignature(String key) {
        byte[] signature = new byte[256];
        System.arraycopy(key.getBytes(), 0, signature, 256 - key.length(), key.length());
        return signature;
    }
}
