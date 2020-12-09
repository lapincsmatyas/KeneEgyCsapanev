package com.example.cb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Generator {
    public static String getMd5OfFile(String uri) throws IOException {

        try (FileInputStream fis = new FileInputStream(new File(uri))) {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);

            }

            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMd5OfByteArray(byte[] array) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(array);
            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
