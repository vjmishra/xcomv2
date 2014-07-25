package com.sterlingcommerce.xpedx.webchannel.punchout.util;

import com.sterlingcommerce.xpedx.webchannel.crypto.EncryptionUtils;

/**
 * Encapsulates the conversion to/from userId and password to the OCI 'data' parameter.
 */
public class PunchoutOciUtil {

	public static String encryptData(OciCredentials cred) {
		return EncryptionUtils.encryptToUrl(cred.getUserId() + " " + cred.getPassword());
	}

	/**
	 * @param data
	 * @return
	 * @throws IllegalArgumentException If decrypted data is not formatted properly.
	 */
	public static OciCredentials decryptData(String data) {
		if (data == null) {
			throw new IllegalArgumentException("data cannot be null");
		}

		String[] tokens = EncryptionUtils.decrypt(data).split(" ");
		if (tokens.length != 2) {
			throw new IllegalArgumentException("Invalid data");
		}

		return new OciCredentials(tokens[0], tokens[1]);
	}

	// --- Helper classes

	/**
	 * Immutable data structure to hold userId and password.
	 */
	public static class OciCredentials {

		private String userId;
		private String password;

		public OciCredentials(String userId, String password) {
			super();
			this.userId = userId;
			this.password = password;
		}

		public String getUserId() {
			return userId;
		}

		public String getPassword() {
			return password;
		}
	}

}
