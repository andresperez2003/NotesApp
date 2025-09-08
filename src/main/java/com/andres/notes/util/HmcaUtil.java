package com.andres.notes.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmcaUtil {

    public static String generateSignature(String secret, String timestamp, String body) throws  Exception{
        String data = timestamp+body;
        Mac sha_256_HMCA = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(),"HmacSHA256");
        sha_256_HMCA.init(secretKey);

        byte[] hash = sha_256_HMCA.doFinal(data.getBytes());

        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes){
        StringBuilder hexString = new StringBuilder();
        for(byte b: bytes){
            String hex= Integer.toHexString(0xff & b);
            if(hex.length() == 1 ) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
