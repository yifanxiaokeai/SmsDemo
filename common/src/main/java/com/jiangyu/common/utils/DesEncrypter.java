package com.jiangyu.common.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>Title:     3Des加密                                          </p>
 * <p>Description:                     </p>
 */
public class DesEncrypter {
	Cipher ecipher;

	Cipher dcipher;

	public DesEncrypter(SecretKey key) throws Exception {
		ecipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		// ecipher.init(Cipher.ENCRYPT_MODE,
		// KeyGenerator.getInstance("DES").generateKey());
	}

	public String encrypt(String str) throws Exception {
		// Encode the string into bytes using utf-8
		byte[] utf8 = str.getBytes("UTF-8");

		// Encrypt
		byte[] enc = ecipher.doFinal(utf8);

		// Encode bytes to base64 to get a string
		return new String(Base64.encode(enc, 0), "UTF-8");
	}

	public static String encodeBy3DES(String key, String input) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, initKey(key));
			byte[] encBytes = cipher.doFinal(input.getBytes("UTF-8"));
			byte[] encBase64Bytes = Base64.encode(encBytes, 0);
			return new String(encBase64Bytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static SecretKey initKey(String key) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		byte[] keyBytes = new byte[24];
		byte[] temp = key.getBytes("UTF-8");
		if (keyBytes.length > temp.length) {
			System.arraycopy(temp, 0, keyBytes, 0, temp.length);
		} else {
			System.arraycopy(temp, 0, keyBytes, 0, keyBytes.length);
		}
		return new SecretKeySpec(keyBytes, "DESede");
	}

}
