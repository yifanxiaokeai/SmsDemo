package com.jiangyu.common.utils;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>Title: 加密工具类 </p>
 * <p> Description:</p>
 * <p>1.字符串进行加密，MD5 SHA-1 SHA-256</p>
 * <p>2.MD5加密</p>
 * <p>3.SHA-1加密</p>
 * <p>4.SHA-256</p>
 * <p>5.3DES加密</p>
 * <p>6.加密String明文输入,String密文输出</p>
 * <p>7.解密 以String密文输入,String明文输出</p>
 * <p>8.3des 解密</p>
 * <p>9.加密以byte[]明文输入,byte[]密文输出</p>
 * <p>10.解密以byte[]密文输入,以byte[]明文输出</p>
 * <p>11.根据某个key生成密钥</p>
 * <p>12.获取MD5的字节数组</p>
 * 
 * <p>13.Rsa公钥加密 </p>
 * <p>13.Rsa私钥解密</p>
 * <p>13.通过公钥文件，获取公钥</p>
 * <p>13.通过私钥文件，获取私钥</p>
 *
 */
@SuppressLint("DefaultLocale")
public class EncryptUtil {

	static String DES = "DES/ECB/NoPadding";
	static String TriDes = "DESede/ECB/NoPadding";

	/**
	 * 
	 * @param strSrc
	 * @param encName
	 *            MD5 SHA-1 SHA-256
	 * @return
	 */
	private static String encrypt(String strSrc, String encName) {
		MessageDigest md = null;
		byte[] bt = strSrc.getBytes();
		try {
			if (encName == null || encName.equals("")) {
				encName = "MD5";
			}
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			return StringUtil.bytesToHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			return null;
		}
	}

	/**
	 * 
	 * @param strSrc
	 * @return
	 */
	public static String encryptMd5(String strSrc) {
		return encrypt(strSrc, "MD5").toString();
	}

	/**
	 * 
	 * @param strSrc
	 * @return
	 */
	public static String encryptSHA1(String strSrc) {
		return encrypt(strSrc, "SHA-1").toString();
	}

	/**
	 * 
	 * @param strSrc
	 * @return
	 */
	public static String encryptSHA256(String strSrc) {
		return encrypt(strSrc, "SHA-256").toString();
	}

	/**
	 * 加密String明文输入,String密文输出
	 * 
	 * @param strMing
	 * @return
	 */
	public static String encryptDes(String source, String key) {
		String strMi = "";
		String algorithm = "DES";
		try {
			return StringUtil.bytesToHex(encryptDesToByte(source.getBytes(), key, algorithm));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return strMi;
	}

	/**
	 * 3Des 加密
	 * 
	 * @param source
	 * @param key
	 * @return
	 */
	public static String encrypt3Des(String source, String key) {
		String strMi = "";
		String algorithm = "DESede";
		try {
			return StringUtil.bytesToHex(encryptDesToByte(source.getBytes(), key, algorithm));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return strMi;
	}

	/**
	 * 解密 以String密文输入,String明文输出
	 * 
	 * @param strMi
	 * @return
	 */
	public static String decodeDes(String encoded, String key) {
		String strMing = "";
		String algorithm = "DES";
		try {
			return new String(decodeDesToByte(hex2byte(encoded.getBytes()),
					key, algorithm), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return strMing;
	}

	/**
	 * 3des 解密
	 * 
	 * @param encoded
	 * @param key
	 * @return
	 */
	public static String decode3Des(String encoded, String key) {
		String strMing = "";
		String algorithm = "DESede";
		try {
			return new String(decodeDesToByte(hex2byte(encoded.getBytes()),
					key, algorithm), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return strMing;
	}

	/**
	 * 加密以byte[]明文输入,byte[]密文输出
	 * 
	 * @param byteS
	 * @return
	 */
	private static byte[] encryptDesToByte(byte[] byteS, String key,
			String algorithm) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, generateKey(key, algorithm));
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 
	 * @param key
	 * @param algorithm
	 *            算法 DES,DESede,Blowfish
	 * @return
	 */
	private static Key generateKey(String key, String algorithm) {
		KeyGenerator _generator = null;
		try {
			_generator = KeyGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		_generator.init(new SecureRandom(key.getBytes()));
		return _generator.generateKey();
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * 
	 * @param byteD
	 * @return
	 */
	private static byte[] decodeDesToByte(byte[] byteD, String key,
			String algorithm) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, generateKey(key, algorithm));
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 获取MD5的字节数组
	 * 
	 * @param str
	 * @return byte[]
	 */
	public static byte[] getMD5Bytes(String str) {
		if (StringUtil.isEmpty(str))
			return new byte[] {};

		String md5 = encrypt(str,"");
		if (!StringUtil.isEmpty(md5)) {
			return md5.getBytes();
		}

		return new byte[] {};
	}

	/**
	 * rsa公钥加密
	 * 
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 模长
		int key_len = publicKey.getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11
		String[] datas = StringUtil.splitString(data, key_len - 11);
		String mi = "";
		// 如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			mi += DataUtil.bcd2Str(cipher.doFinal(s.getBytes()));
		}
		return mi;
	}

	/**
	 * rsa私钥解密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data,
			RSAPrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// 模长
		int key_len = privateKey.getModulus().bitLength() / 8;
		byte[] bytes = data.getBytes();
		byte[] bcd = DataUtil.ASCII_To_BCD(bytes, bytes.length);
		// 如果密文长度大于模长则要分组解密
		String ming = "";
		byte[][] arrays = DataUtil.splitArray(bcd, key_len);
		for (byte[] arr : arrays) {
			ming += new String(cipher.doFinal(arr));
		}
		return ming;
	}

	/**
	 * 通过公钥文件，获取公钥
	 * 
	 * @return
	 * @throws Exception
	 * @throws CertificateException
	 * @throws IOException
	 */
	public static RSAPublicKey getRsaPublicKey(InputStream in) throws Exception {
		CertificateFactory factory = CertificateFactory.getInstance("X.509");
		Certificate cert = factory.generateCertificate(in);
		RSAPublicKey pubKey = (RSAPublicKey) cert.getPublicKey();
		return pubKey;
	}

	public static RSAPublicKey loadPublicKey(String publicKeyStr)
			throws Exception {
		try {
			byte[] bytes = Base64Util.decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 通过私钥文件，获取私钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static RSAPrivateKey getRsaPrivateKey(InputStream inStream)
			throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			if (readLine.charAt(0) == '-') {
				continue;
			} else {
				sb.append(readLine);
				sb.append('\r');
			}
		}
		return loadPrivateKey(sb.toString());
	}

	public static RSAPrivateKey loadPrivateKey(String privateKeyStr)
			throws Exception {
		try {
			byte[] buffer = Base64Util.decode(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 3DES加密
	 * 
	 * @param key
	 * @param input
	 * @return
	 */
	public static String encodeBy3DES(String key, String input) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(DES);
			cipher.init(Cipher.ENCRYPT_MODE, initKey(key));
			byte[] encBytes = cipher.doFinal(input.getBytes("UTF-8"));
			byte[] encBase64Bytes = Base64.encode(encBytes, 0);
			return new String(encBase64Bytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static SecretKey initKey(String key)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] keyBytes = new byte[24];
		byte[] temp = key.getBytes("UTF-8");
		if (keyBytes.length > temp.length) {
			System.arraycopy(temp, 0, keyBytes, 0, temp.length);
		} else {
			System.arraycopy(temp, 0, keyBytes, 0, keyBytes.length);
		}
		return new SecretKeySpec(keyBytes, "DESede");
	}
	 public static byte[] hex2byte(byte[] b) {
	 if ((b.length % 2) != 0)
	 throw new IllegalArgumentException("长度不是偶数");
	 byte[] b2 = new byte[b.length / 2];
	 for (int n = 0; n < b.length; n += 2) {
	 String item = new String(b, n, 2);
	 // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
	 b2[n / 2] = (byte) Integer.parseInt(item, 16);
	 }
	 return b2;
	 }
}
