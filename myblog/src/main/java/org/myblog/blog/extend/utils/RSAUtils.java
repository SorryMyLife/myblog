package org.myblog.blog.extend.utils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {
    private KeyPair keyPair = null;

    public RSAUtils(int length) {
        keyPair = getKeyPair(length);
    }

    public RSAUtils() {
        keyPair = getKeyPair(1024);
    }

    // 生成秘钥对
    public static KeyPair getKeyPair(int length) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(length);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] DataEncrypt(byte data[], byte publickey[]) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,
                    KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publickey)));
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] DataDecrypt(byte encryptData[], byte privteKey[]) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE,
                    KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privteKey)));
            return cipher.doFinal(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toHexString(byte[] byteArray) {
        final StringBuilder hexString = new StringBuilder();

        if (byteArray == null || byteArray.length < 1) {
            throw new IllegalArgumentException("this byteArray must not be null or empty");
        } else {
            for (int i = 0; i < byteArray.length; i++) {
                if ((byteArray[i] & 0xff) < 0x10)// 0~F前面不零
                    hexString.append("0");
                hexString.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return hexString.toString().toLowerCase();
    }

    public String getPublicKey() {
        return keyPair != null ? toHexString(keyPair.getPublic().getEncoded()) : null;
    }

    public String getPrivateKey() {
        return keyPair != null ? toHexString(keyPair.getPrivate().getEncoded()) : null;
    }

    public String getEncrypt(String srcData , String publicKey) {
        return toHexString(DataEncrypt(srcData.getBytes(),toByteArray(publicKey)));
    }

    public byte[] toByteArray(String hexString) {
        if (hexString.isEmpty()) {
            throw new IllegalArgumentException("this hexString must not be empty");
        } else {
            final byte[] byteArray = new byte[hexString.length() / 2];
            hexString = hexString.toLowerCase();
            int k = 0;
            for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
                byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
                byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
                byteArray[i] = (byte) (high << 4 | low);
                k += 2;
            }
            return byteArray;
        }
    }
    public String getDecrypt(String encryptData ,String privateKey) {
        return new String(DataDecrypt(toByteArray(encryptData), toByteArray(privateKey)));
    }

}
