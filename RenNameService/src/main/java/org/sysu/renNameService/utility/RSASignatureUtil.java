/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.utility;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.sysu.renNameService.GlobalContext;

/**
 * Author: Rinkako
 * Date  : 2018/1/31
 * Usage : Methods for data RSA Signature.
 */
public class RSASignatureUtil {
    /**
     * Generate a pair of RSA key.
     * @return Key pair instance
     */
    public static KeyPair GenerateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(1024);
            return keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            LogUtil.Log("GenerateKeyPair but exception occurred, " + ex, RSASignatureUtil.class.getName(),
                    LogUtil.LogLevelType.ERROR, "");
            return null;
        }
    }

    /**
     * Signature a data.
     * @param data data to be signature in string
     * @param privateKey signature using private key in Base64 string
     * @return signature data in Base64
     */
    public static String Signature(String data, String privateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey priKey = keyFactory.generatePrivate(keySpec);

            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initSign(priKey);
            signature.update(data.getBytes());
            return new String(Base64.encodeBase64(signature.sign()));
        }
        catch (Exception ex) {
            LogUtil.Log("Signature but exception occurred, " + ex, RSASignatureUtil.class.getName(),
                    LogUtil.LogLevelType.ERROR, "");
            return null;
        }
    }

    /**
     * Verify a signature of its signed data.
     * @param data data to be verify in string
     * @param publicKey public key in Base64 string
     * @param sign signature string
     * @return boolean of verify result
     */
    public static boolean Verify(String data, String publicKey, String sign) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(data.getBytes());
            return signature.verify(Base64.decodeBase64(sign));
        }
            catch (Exception ex) {
            LogUtil.Log("Verify but exception occurred, " + ex, RSASignatureUtil.class.getName(),
                    LogUtil.LogLevelType.ERROR, "");
            return false;
        }
    }

    /**
     * Translate a Base64 string to URL safe Base64 string.
     * @param encodeBase64 Base64 string to be translated
     * @return URL safe string
     */
    public static String SafeUrlBase64Encode(String encodeBase64) {
        String safeBase64Str = encodeBase64.replace('+', '-');
        safeBase64Str = safeBase64Str.replace('/', '_');
        safeBase64Str = safeBase64Str.replaceAll("=", "");
        return safeBase64Str;
    }

    /**
     * Translate a URL safe Base64 string to original Base64 string.
     * @param safeBase64Str URL safe Base64 string
     * @return original Base64 string
     */
    public static String SafeUrlBase64Decode(final String safeBase64Str) {
        String base64Str = safeBase64Str.replace('-', '+');
        base64Str = base64Str.replace('_', '/');
        int mod4 = base64Str.length() % 4;
        if (mod4 > 0) {
            base64Str = base64Str + "====".substring(mod4);
        }
        return base64Str;
    }

    /**
     * Algorithm for key generator.
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * Algorithm for signature, should be the same with other BO module.
     */
    private static final String SIGN_ALGORITHM = "MD5withRSA";

    public static void main(String[] args) {
        String data = "123456";
        String verifyData = "123456";
        String sign = Signature(data, GlobalContext.PRIVATE_KEY);
        System.out.println(sign);
        boolean isValid = Verify(verifyData, GlobalContext.PUBLIC_KEY, sign);
        System.out.println(isValid);
    }
}