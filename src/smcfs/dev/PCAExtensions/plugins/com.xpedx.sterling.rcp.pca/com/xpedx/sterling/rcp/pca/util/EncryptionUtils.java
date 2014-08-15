package com.xpedx.sterling.rcp.pca.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


/**
 * This class was mostly copied from the web channel class of the same name in
 * com.sterlingcommerce.xpedx.webchannel.crypto though this uses a different Base64
 * class for encoding that was already being used in Call Center (by TripleDES
 * which uses a different encryption method).
 *
 * @author http://techie-experience.blogspot.com/2012/10/encryption-and-decryption-using-aes.html
 */
public class EncryptionUtils {

	private static byte[] key = { 120, -33, 8, -120, -58, 119, 68, 99, -102, -67, 81, -127, -91, 54, -25, 110 }; // 128-bit key (16 bytes)

	public static String encrypt(String strToEncrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes());
			final String encryptedString = Base64.encode(encryptedBytes);
			return encryptedString;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String strToDecrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			final byte[] decryptedBytes = cipher.doFinal(Base64.decode(strToDecrypt));
			final String decryptedString = new String(decryptedBytes);
			return decryptedString;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
