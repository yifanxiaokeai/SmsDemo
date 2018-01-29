package com.jiangyu.common.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    public static final String OUR_RSA_PUBLIC = "dVmNEbf9RN1k6M09";

    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 秘钥长度
     */
    private static final int KEY_SIZE = 128;
    /**
     * 秘钥
     */
    private static final String KEY_STR = AESUtil.OUR_RSA_PUBLIC;

    /**
     * 加密/解密算法/工作模式/填充方法
     */
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";


    /**
     * 获取密钥
     *
     * @return
     * @throws Exception
     */
    public static Key getKey() throws Exception {
        //实例化
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        //AES 要求密钥长度为128位、192位或256位
        kg.init(KEY_SIZE);
        //生成密钥
        SecretKey secretKey = kg.generateKey();
        return secretKey;
    }

    /**
     * 转化密钥
     *
     * @param key 密钥
     * @return Key 密钥
     * @throws Exception
     */
    public static Key codeToKey(String key) throws Exception {
        byte[] keyBytes = Base64Util.decode(key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        return secretKey;
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     * @throws Exception
     */
    private static String decrypt(byte[] data, byte[] key) throws Exception {
        //还原密钥
        Key k = new SecretKeySpec(key, KEY_ALGORITHM);
        /**
         * 实例化
         * 使用PKCS7Padding填充方式，按如下方式实现
         * Cipher.getInstance(CIPHER_ALGORITHM,"BC");
         */
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化，设置解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);
        //执行操作
        return new String(cipher.doFinal(data), "UTF-8");
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static String decrypt(String data) throws Exception {
        return decrypt(Base64Util.decode(data), KEY_STR.getBytes("UTF-8"));
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @return bytes[] 加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        //还原密钥
        Key k = new SecretKeySpec(key, KEY_ALGORITHM);
        /**
         * 实例化
         * 使用PKCS7Padding填充方式，按如下方式实现
         * Cipher.getInstance(CIPHER_ALGORITHM,"BC");
         */
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, k);
        //执行操作
        return cipher.doFinal(data);
    }

    /**
     * 使用公钥对明文进行加密，返回BASE64编码的字符串
     *
     * @param plainText
     * @return
     */
    public static String encrypt( String plainText) {
        try {
            byte[] dataBytes = plainText.getBytes("UTF-8");
            byte[] keyBytes = OUR_RSA_PUBLIC.getBytes("UTF-8"); //Base64.decode(publicKey.getBytes(),Base64.DEFAULT);
            Key k = new SecretKeySpec(keyBytes, "AES");
            Cipher ciper = Cipher.getInstance("AES/ECB/PKCS5Padding");
            ciper.init(Cipher.ENCRYPT_MODE, k);
            byte[] s = ciper.doFinal(dataBytes);
            return new String(android.util.Base64.encode(s, android.util.Base64.DEFAULT));//

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 初始化密钥
     * @return
     * @throws Exception
     */
//    public static String getKeyStr() throws Exception{
//        return Base64.encodeBase64String(getKey().getEncoded());
//    }

//    public static void main(String[] args) throws Exception{
//    	System.out.println("新的秘钥:" + getKeyStr());
//
//    	String miwen="123";
//    	String mw = AESUtil.encrypt(miwen);
//        System.out.println("密文:" + mw);
//        String jm = AESUtil.decrypt(mw);
//        System.out.println("明文:" + jm);
//        System.out.println("明文长度:" + jm.length());
//    }
}