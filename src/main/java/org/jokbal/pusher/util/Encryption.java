package org.jokbal.pusher.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 7.
 * Time: 오전 5:26
 * To change this template use File | Settings | File Templates.
 */
public class Encryption {
    public static String hmacSHA256(String secret,String message) throws InvalidKeyException, NoSuchAlgorithmException {
        String algorithm = "HmacSHA256";

        byte[] keyBytes = hexToBytes(secret);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, algorithm);

        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        byte[] macBytes = mac.doFinal(message.getBytes());

        return bytesToHex(macBytes);
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}
