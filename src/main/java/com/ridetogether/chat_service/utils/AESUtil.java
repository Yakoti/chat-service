//package com.ridetogether.chat_service.utils;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import javax.crypto.Cipher;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//@Component
//public class AESUtil {
//
//    @Value("${aes.secret.key}")
//    private String aesKeyBase64;
//
//    private SecretKey secretKey;
//
//    @PostConstruct
//    public void init() {
//        byte[] key = Base64.getDecoder().decode(aesKeyBase64);
//        secretKey = new SecretKeySpec(key, "AES");
//    }
//
//    public String encryptText(String plain) throws Exception {
//        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        c.init(Cipher.ENCRYPT_MODE, secretKey);
//        return Base64.getEncoder().encodeToString(c.doFinal(plain.getBytes(StandardCharsets.UTF_8)));
//    }
//
//    public byte[] encryptFileBytes(byte[] fileBytes) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        // Encrypt the file bytes
//        return cipher.doFinal(fileBytes);
//    }
//    public byte[] decryptFileBytes(byte[] encryptedBytes) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        return cipher.doFinal(encryptedBytes);
//    }
//}
