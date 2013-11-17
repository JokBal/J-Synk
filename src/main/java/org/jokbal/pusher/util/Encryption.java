package org.jokbal.pusher.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/*
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 7.
 * Time: AM 5:26
 * To change this template use File | Settings | File Templates.
 */

public class Encryption {

    /**
     * Returns a HMAC/SHA256 representation of the given string
     * @param data
     * @return
     */
    public static String hmacsha256Representation(String data, String pusherApplicationSecret) {
        try {
            // Create the HMAC/SHA256 key from application secret
            final SecretKeySpec signingKey = new SecretKeySpec( pusherApplicationSecret.getBytes(), "HmacSHA256");

            // Create the message authentication code (MAC)
            final Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            //Process and return data
            byte[] digest = mac.doFinal(data.getBytes("UTF-8"));
            digest = mac.doFinal(data.getBytes());
            //Convert to string
            BigInteger bigInteger = new BigInteger(1,digest);
            return String.format("%0" + (digest.length << 1) + "x", bigInteger);
        } catch (NoSuchAlgorithmException nsae) {
            //We should never come here, because GAE has HMac SHA256
            throw new RuntimeException("No HMac SHA256 algorithm");
        } catch (UnsupportedEncodingException e) {
            //We should never come here, because UTF-8 should be available
            throw new RuntimeException("No UTF-8");
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key exception while converting to HMac SHA256");
        }
    }

}
