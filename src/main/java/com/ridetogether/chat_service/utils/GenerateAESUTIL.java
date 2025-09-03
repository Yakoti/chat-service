//package com.ridetogether.chat_service.utils;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import java.util.Base64;
//
//public class GenerateAESUTIL {
//    public static void setupAES() throws Exception{
//        KeyGenerator keygen=KeyGenerator.getInstance("AES");
//        keygen.init(128);
//        SecretKey secretKey= keygen.generateKey();
//        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        System.out.println("Base64 Encoded AES Key: " + base64Key);
//    }
//}
//
