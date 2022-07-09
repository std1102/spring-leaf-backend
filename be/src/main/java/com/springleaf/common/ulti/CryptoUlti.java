package com.springleaf.common.ulti;

import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
public class CryptoUlti {

    private static KeyPair initKeyPair() {
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkb1eEgoIW3gCvnPXVQYo11bI78nSlcbJrjFciHwxndaxIBe16QwWFrlubyTbm+F0sT9B7H3ia2hhLgrH9nv+ILOuKSpLpR3UU5jD1qqMoXivfBE9ZhtKBXaZMxPpIubLGS7P2U8gYnqQPw+2/U2rtakn3z4yUMpJtrytFBG+g70BlOeRIjWk41PD/fjloq8j2Mnr457+1KrYtM2ego82/jMXG8u/N3BdhbffSKTsRDJ85GSRQxHSaNxIkCi4jLqoMSF6aK867r0u+y3n4PPv6+hUOAjuFaZEfJaqloxcazzA6YHq14uAALDJsu2DVeYDcTNEbshyBOCdyXJyyVPHbQIDAQAB";
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCRvV4SCghbeAK+c9dVBijXVsjvydKVxsmuMVyIfDGd1rEgF7XpDBYWuW5vJNub4XSxP0HsfeJraGEuCsf2e/4gs64pKkulHdRTmMPWqoyheK98ET1mG0oFdpkzE+ki5ssZLs/ZTyBiepA/D7b9Tau1qSffPjJQykm2vK0UEb6DvQGU55EiNaTjU8P9+OWiryPYyevjnv7Uqti0zZ6Cjzb+Mxcby783cF2Ft99IpOxEMnzkZJFDEdJo3EiQKLiMuqgxIXporzruvS77Lefg8+/r6FQ4CO4VpkR8lqqWjFxrPMDpgerXi4AAsMmy7YNV5gNxM0RuyHIE4J3JcnLJU8dtAgMBAAECggEADjUs14tc1zXy5og6hNJt5thSDHZbbjiL7sxnHfMljyKf7+X/2PFRsfNv3av1MNQg4SqujZSUHy500t1AMzpRQvThVKgH6dfnnCK3bNUBkrhGO00e9DZS2AeA1ebqAnoHcLQMlT3OhWs3Zr5sc3M/+xy7g1r34sJlv0fTg7JbJVKBCs5zJT8b+ujlEbgv3KnIET+CS68viLKC5yDIuXy4G8YOLLtYk1TFgZqNnMY+b48kBxHWa4c+IcYRlLSkYBm6yuE3A6zMXcyJzss1u+EsDWPwWdLzXLnUzlFej0HuMWjIvZllAFRemq9F2FOEeKz9zFxSFMO/uH3nJbjJpL2t7QKBgQDFE4djA7ySibYOfM1LIOBom6VRDsEgXvzs3l026JlmVV8ODZ9LPSWB3mqbe7avXmdiC21rsdjt04lBw02XYlzxOQPArnu+zXwq1KmGiNke1nhUQDINAFW7glrOQwkzwm+7P012F/F2kvSmW4B2ftLT6wntlzNyiiyt+c8XOt+8OwKBgQC9UHjTDf6ScxCWWLDE5+rUmetdW63blFSLKnnd6fXASqjsX+StaMnXwHZybXPHbHEWMOwHLi99hofnPCiKE3uHvmBngqxUt+BtCZYwk6aep5GrCBhiZgFq9b01NsNpRt+bqtcxFUtN3TYgZHlRwDeW2PqkK+8Np29QMRaUwO1YdwKBgBUlW0mMhRdUH2y+h2igRnPy/3ILGz3V812XHnldy9fnISPo4ka4cOXWb6nvRFLc1kr23u7HTnd2UyvzQMgVjdN0QqrSDgUTF1ehKe99j453hMHFgZlTh99uxT+MgdhD7KkyyaT6PLZ0XjvbZZVRVl3WwmHz71lC6jhgrY/YiVkxAoGBAJRPTLibSApXFnGSPbmkrnWrt5XF0ugoxirYNezB8wKX+spjPXUUmLvrTV/Lm15p+BYYcXAWoKHIGQPwPxoctTPc0w2Ec5lTDACf+AdKRMgaIqZIi0HbA9VVessLgf8hxN/g8QDE29++Iz8xN1HyMhp9Fa4ztO2PdEbwje4vTkF1AoGAfdhg/lJALktLn7JXtcs/XG5dgaa8g/BFxzxpVMPquyqpz15lRaIwjX/6Ocq/WJeDNakEDOdDwuY2Win0H/IzPtSVq3eXKh9Kp05pZ1Y1yxx4wEp5mZ9CRvRobulnkaKquWFYW614QaGx4v9Yg83OyxToWyX7BdwRSEHDDLt0COU=";
        return new KeyPair(getPublicKey(), getPrivateKey());
    }

    private static String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkb1eEgoIW3gCvnPXVQYo11bI78nSlcbJrjFciHwxndaxIBe16QwWFrlubyTbm+F0sT9B7H3ia2hhLgrH9nv+ILOuKSpLpR3UU5jD1qqMoXivfBE9ZhtKBXaZMxPpIubLGS7P2U8gYnqQPw+2/U2rtakn3z4yUMpJtrytFBG+g70BlOeRIjWk41PD/fjloq8j2Mnr457+1KrYtM2ego82/jMXG8u/N3BdhbffSKTsRDJ85GSRQxHSaNxIkCi4jLqoMSF6aK867r0u+y3n4PPv6+hUOAjuFaZEfJaqloxcazzA6YHq14uAALDJsu2DVeYDcTNEbshyBOCdyXJyyVPHbQIDAQAB";
    private static String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCRvV4SCghbeAK+c9dVBijXVsjvydKVxsmuMVyIfDGd1rEgF7XpDBYWuW5vJNub4XSxP0HsfeJraGEuCsf2e/4gs64pKkulHdRTmMPWqoyheK98ET1mG0oFdpkzE+ki5ssZLs/ZTyBiepA/D7b9Tau1qSffPjJQykm2vK0UEb6DvQGU55EiNaTjU8P9+OWiryPYyevjnv7Uqti0zZ6Cjzb+Mxcby783cF2Ft99IpOxEMnzkZJFDEdJo3EiQKLiMuqgxIXporzruvS77Lefg8+/r6FQ4CO4VpkR8lqqWjFxrPMDpgerXi4AAsMmy7YNV5gNxM0RuyHIE4J3JcnLJU8dtAgMBAAECggEADjUs14tc1zXy5og6hNJt5thSDHZbbjiL7sxnHfMljyKf7+X/2PFRsfNv3av1MNQg4SqujZSUHy500t1AMzpRQvThVKgH6dfnnCK3bNUBkrhGO00e9DZS2AeA1ebqAnoHcLQMlT3OhWs3Zr5sc3M/+xy7g1r34sJlv0fTg7JbJVKBCs5zJT8b+ujlEbgv3KnIET+CS68viLKC5yDIuXy4G8YOLLtYk1TFgZqNnMY+b48kBxHWa4c+IcYRlLSkYBm6yuE3A6zMXcyJzss1u+EsDWPwWdLzXLnUzlFej0HuMWjIvZllAFRemq9F2FOEeKz9zFxSFMO/uH3nJbjJpL2t7QKBgQDFE4djA7ySibYOfM1LIOBom6VRDsEgXvzs3l026JlmVV8ODZ9LPSWB3mqbe7avXmdiC21rsdjt04lBw02XYlzxOQPArnu+zXwq1KmGiNke1nhUQDINAFW7glrOQwkzwm+7P012F/F2kvSmW4B2ftLT6wntlzNyiiyt+c8XOt+8OwKBgQC9UHjTDf6ScxCWWLDE5+rUmetdW63blFSLKnnd6fXASqjsX+StaMnXwHZybXPHbHEWMOwHLi99hofnPCiKE3uHvmBngqxUt+BtCZYwk6aep5GrCBhiZgFq9b01NsNpRt+bqtcxFUtN3TYgZHlRwDeW2PqkK+8Np29QMRaUwO1YdwKBgBUlW0mMhRdUH2y+h2igRnPy/3ILGz3V812XHnldy9fnISPo4ka4cOXWb6nvRFLc1kr23u7HTnd2UyvzQMgVjdN0QqrSDgUTF1ehKe99j453hMHFgZlTh99uxT+MgdhD7KkyyaT6PLZ0XjvbZZVRVl3WwmHz71lC6jhgrY/YiVkxAoGBAJRPTLibSApXFnGSPbmkrnWrt5XF0ugoxirYNezB8wKX+spjPXUUmLvrTV/Lm15p+BYYcXAWoKHIGQPwPxoctTPc0w2Ec5lTDACf+AdKRMgaIqZIi0HbA9VVessLgf8hxN/g8QDE29++Iz8xN1HyMhp9Fa4ztO2PdEbwje4vTkF1AoGAfdhg/lJALktLn7JXtcs/XG5dgaa8g/BFxzxpVMPquyqpz15lRaIwjX/6Ocq/WJeDNakEDOdDwuY2Win0H/IzPtSVq3eXKh9Kp05pZ1Y1yxx4wEp5mZ9CRvRobulnkaKquWFYW614QaGx4v9Yg83OyxToWyX7BdwRSEHDDLt0COU=";


    public static PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(PRIVATE_KEY.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static PublicKey getPublicKey() {
        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static byte[] encrypt(byte[] data) {
        try {
            byte[] iv = $.randomByte(16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, initAESKey(), ivParameterSpec);
            byte[] cipherData = cipher.doFinal(data);
            return $.concat(iv, cipherData);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static byte[] decrypt(byte[] data) {
        try {
            byte[] iv = Arrays.copyOfRange(data, 0, 16);
            byte[] ct = Arrays.copyOfRange(data, 16, data.length);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, initAESKey(), ivParameterSpec);
            return cipher.doFinal(ct);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private static Key initAESKey() {
        String encodedKey = "ktyAUWGmXcHdNtiI0daV+w==";
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

}
