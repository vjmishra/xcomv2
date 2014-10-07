package com.sterlingcommerce.xpedx.webchannel.crypto;

import java.security.SecureRandom;

import javax.crypto.KeyGenerator;

/**
 * @author https://www.owasp.org/index.php/Using_the_Java_Cryptographic_Extensions
 */
public class KeyGen {

	public static void main(String[] args) throws Exception {
		/**
		 * Step 1. Generate an AES key using KeyGenerator Initialize the keysize to 128 bits (16 bytes)
		 *
		 */
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);

		/**
		 * Step 2. Generate an Initialization Vector (IV) a. Use SecureRandom to generate random bits The size of the IV matches the blocksize of the cipher (128 bits for AES) b.
		 * Construct the appropriate IvParameterSpec object for the data to pass to Cipher's init() method
		 */

		final int AES_KEYLENGTH = 128; // change this as desired for the security level you want
		byte[] iv = new byte[AES_KEYLENGTH / 8]; // Save the IV bytes or send it in plaintext with the encrypted data so you can decrypt the data later
		SecureRandom prng = new SecureRandom();
		prng.nextBytes(iv);

		StringBuilder buf = new StringBuilder(4 * iv.length);
		boolean firstIter = true;
		for (byte b : iv) {
			if (firstIter) {
				firstIter = false;
			} else {
				buf.append(", ");
			}
			buf.append(b);
		}
		System.out.println(buf);
	}

}
