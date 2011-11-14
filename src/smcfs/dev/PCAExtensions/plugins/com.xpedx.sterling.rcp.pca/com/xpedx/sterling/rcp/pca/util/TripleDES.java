package com.xpedx.sterling.rcp.pca.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class TripleDES {

	private static String svString = "b2bxp3dx";
	private static String skString = "b2b1nt3rnat1onal";

	private static byte[] sharedvector = svString.getBytes();

	private static byte[] sharedkey = skString.getBytes();
	
	private static SecretKey sk = getSecretKey(sharedkey);
	
	private static byte[] sharedkeyfinal = sk.getEncoded();

	//To test Encrypted logic through console 
	/*public static void encryptMain() throws Exception {
		String plaintext = "http://www.gmail.com/";

		String ciphertext = encrypt(plaintext);
		System.out.println(ciphertext);
		System.out.println(decrypt(ciphertext));
	}*/

	public  String encrypt(String plaintext) throws Exception {
		Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sharedkeyfinal, "DESede"),
				new IvParameterSpec(sharedvector));
		byte[] encrypted = c.doFinal(plaintext.getBytes("UTF-8"));
		return Base64.encode(encrypted);
	}

	public  String decrypt(String ciphertext) throws Exception {
		Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sharedkeyfinal, "DESede"),
				new IvParameterSpec(sharedvector));
		byte[] decrypted = c.doFinal(Base64.decode(ciphertext));
		return new String(decrypted, "UTF-8");
	}

	public static SecretKey getSecretKey(byte[] encryptionKey) {
		SecretKey secretKey = null;
		if (encryptionKey == null)
			return null;

		byte[] keyValue = new byte[24]; // final 3DES key  

		if (encryptionKey.length == 16) {
			// Create the third key from the first 8 bytes  
			System.arraycopy(encryptionKey, 0, keyValue, 0, 16);
			System.arraycopy(encryptionKey, 0, keyValue, 16, 8);

		} else if (encryptionKey.length != 24) {
			throw new IllegalArgumentException(
					"A TripleDES key should be 24 bytes long");

		} else {
			keyValue = encryptionKey;
		}
		DESedeKeySpec keySpec;
		try {
			keySpec = new DESedeKeySpec(keyValue);
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			secretKey = keyFactory.generateSecret(keySpec);
		} catch (Exception e) {
			throw new RuntimeException("Error in key Generation", e);
		}
		return secretKey;
	}

}

