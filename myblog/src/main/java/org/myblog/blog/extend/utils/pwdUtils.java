package org.myblog.blog.extend.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class pwdUtils {

    public static String enpwd(String srcpwd){
        return  md5_16(srcpwd);
    }

    private static String md5_16(String srcpwd){
        return  MD532(srcpwd).substring(8, 24);
    }
    private static String MD532(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }
}
