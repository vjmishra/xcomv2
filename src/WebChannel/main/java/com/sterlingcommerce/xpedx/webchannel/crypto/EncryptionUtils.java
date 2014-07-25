package com.sterlingcommerce.xpedx.webchannel.crypto;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.sterlingcommerce.xpedx.webchannel.common.punchout.PunchoutOciUtil;
import com.sterlingcommerce.xpedx.webchannel.common.punchout.PunchoutOciUtil.OciCredentials;

/**
 * @author http://techie-experience.blogspot.com/2012/10/encryption-and-decryption-using-aes.html
 */
public class EncryptionUtils {

	private static byte[] key = { 120, -33, 8, -120, -58, 119, 68, 99, -102, -67, 81, -127, -91, 54, -25, 110 }; // 128-bit key (16 bytes)

	public static String encrypt(String strToEncrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			final byte[] encryptedBytes = Base64.encodeBase64(cipher.doFinal(strToEncrypt.getBytes()));
			final String encryptedString = new String(encryptedBytes);
			return encryptedString;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String strToDecrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			final byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(strToDecrypt.getBytes()));
			final String decryptedString = new String(decryptedBytes);
			return decryptedString;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String encryptToUrl(String strToEncrypt) {
		try {
			final String encryptedString = encrypt(strToEncrypt);
			final String encodedEncryptedString = URLEncoder.encode(encryptedString, "utf-8");
			return encodedEncryptedString;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String decryptFromUrl(String strToDecrypt) {
		try {
			String decodedEncryptedString = URLDecoder.decode(strToDecrypt, "utf-8");
			String decryptedString = decrypt(decodedEncryptedString);
			return decryptedString;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String args[]) throws Exception {
		if (args.length < 2) {
			System.err.println("Usage: " + EncryptionUtils.class.getSimpleName() + " <unencrypted username> <unencrypted password>");
			System.exit(1);
			return;
		}

		String userId = args[0];
		String password = args[1];

		String encrypted = PunchoutOciUtil.encryptData(new OciCredentials(userId, password));
		System.out.println("URL parameter:	" + encrypted);


//		for (String arg : args) {
//			System.out.println("Argument:	" + arg);
//			System.out.println("Encrypted:	" + encrypt(arg));
//			System.out.println("For URL:	" + encryptToUrl(arg));
//			System.out.println();
//		}
	}

}
