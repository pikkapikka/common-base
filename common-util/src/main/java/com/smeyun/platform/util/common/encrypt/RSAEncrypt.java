/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: RSAEncrypt.java
 * Author:   qxf
 * Date:     2016年12月1日 下午2:35:25
 */
package com.smeyun.platform.util.common.encrypt;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.platform.util.common.constant.PlatformConstant;
import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * RSA加密解密工具
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class RSAEncrypt
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RSAEncrypt.class);
    
    private static final String KEY_ALGORITHM = "RSA";
    
    /* 貌似默认是RSA/NONE/PKCS1Padding，未验证 */
    private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
    
    /*
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    
    /* RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
    private static final int KEY_SIZE = 2048;
    
    /** 公钥的KEY */
    public static final String PUBLIC_KEY = "publicKey";
    
    /** 私钥的KEY */
    public static final String PRIVATE_KEY = "privateKey";
    
    private RSAEncrypt()
    {
        
    }
    
    /**
     * 生成密钥对。</br>
     * 私钥在返回结果中对应的KEY为：privateKey </br>
     * 公钥在返回结果中对应的KEY为：publicKey
     * 
     * @return 密钥对
     */
    public static Map<String, byte[]> generateKeyBytes()
    {
        try
        {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            
            Map<String, byte[]> keyMap = new HashMap<String, byte[]>();
            keyMap.put(PUBLIC_KEY, publicKey.getEncoded());
            keyMap.put(PRIVATE_KEY, privateKey.getEncoded());
            return keyMap;
        }
        catch (NoSuchAlgorithmException e)
        {
            LOGGER.error("generate key pairs failed:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /**
     * 还原公钥。
     * 
     * @param keyBytes 公钥字节数组
     * @return 公钥对象
     */
    public static PublicKey restorePublicKey(byte[] keyBytes)
    {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        
        try
        {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        }
        catch (Exception e)
        {
            LOGGER.error("restore public key failed:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /**
    * 还原私钥。
    * 
    * @param keyBytes 私钥字节数组
    * @return 私钥对象
    */
    public static PrivateKey restorePrivateKey(byte[] keyBytes)
    {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try
        {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        }
        catch (Exception e)
        {
            LOGGER.error("restore private key failed:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /**
     * 使用RSA公钥加密
     * 
     * @param key 公钥
     * @param content 要加密的字符串
     * @return
     */
    public static String encode(PublicKey key, String content)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(content.getBytes(PlatformConstant.CHARSET_UTF8));
            return Base64.encodeBase64String(bytes);
        }
        catch (Exception e)
        {
            LOGGER.error("encode with RSA failed:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /**
     * 使用RSA私钥解密
     * 
     * @param key 解密
     * @param encodedText
     * @return
     */
    public static String decode(PrivateKey key, String content)
    {
        
        try
        {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.decodeBase64(content)));
        }
        catch (Exception e)
        {
            LOGGER.error("decode with RSA failed:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /**
     * 用私钥对信息生成数字签名
     * 
     * @param data 数据
     * @param privateKey 私钥
     * 
     * @return 签名
     */
    public static String sign(byte[] data, PrivateKey privateKey)
    {
        try
        {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data);
            return Base64.encodeBase64String(signature.sign());
        }
        catch (Exception e)
        {
            LOGGER.error("sign with RSA failed:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    /**
     * 校验数字签名
     * 
     * @param data 数据
     * @param publicKey 公钥
     * @param sign 数字签名
     * 
     * @return 校验成功返回true，失败返回false
     * 
     */
    public static boolean verify(byte[] data, PublicKey publicKey, String sign)
    {
        try
        {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(Base64.decodeBase64(sign));
        }
        catch (Exception e)
        {
            LOGGER.error("verify with RSA failed:", e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
        
    }
}
