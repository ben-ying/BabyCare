package com.ben.yjh.babycare.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class MD5Utils {

    public static String getMD5ofStr(String plainText) {
        try {
            return getMD5ofStr(string2Bytes(plainText));
        } catch (Exception e) {

        }
        return null;
    }

    public static String getMD5ofStr(byte str[]) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str);
            byte b[] = md.digest();
            return bytes2HexString(b);

        } catch (Exception e) {

        }
        return null;
    }

    public static byte [] string2Bytes(String s) {
        byte[] bytes = null;
        if (s != null) {
            try {
                bytes = s.getBytes("utf-8");
            } catch (UnsupportedEncodingException e) {

            }
        }
        return bytes;
    }

    public static String bytes2HexString(byte[] bytes) {
        String hs = null;
        if (bytes != null) {
            final int size = bytes.length;
            if (size > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    String tmp = (Integer.toHexString(bytes[i] & 0XFF));
                    if (tmp.length() == 1) {
                        sb.append("0" + tmp);
                    } else {
                        sb.append(tmp);
                    }
                }
                hs = sb.toString().toUpperCase();
            }
        }
        return hs;
    }
}
