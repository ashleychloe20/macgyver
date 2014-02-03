package io.macgyver.core.crypto;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.json.Json;
import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;

public class Crypto {

	Logger logger = LoggerFactory.getLogger(Crypto.class);

	@Autowired
	KeyStoreManager keyStoreManager;

	public String encryptString(String plainText, String alias)
			throws GeneralSecurityException {
		KeyStore ks = keyStoreManager.getKeyStore();
		SecretKey sk = (SecretKey) ks.getKey(alias,
				keyStoreManager.getPasswordForKey(alias));
		if (sk==null) {
			throw new KeyStoreException("no such key: "+alias);
		}
		return encryptString(plainText, sk, alias);

	}

	protected static String readFirstLine(Reader r) throws IOException {
		BufferedReader br = new BufferedReader(r);
		String line = br.readLine();
		br.close();
		return line;
	}

	protected static byte[] readByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = input.read(buffer)) > 0) {
			baos.write(buffer, 0, len);
		}
		baos.close();
		return baos.toByteArray();

	}

	protected InputStream decrypt(InputStream encrypted, SecretKey secretKey)
			throws IOException, GeneralSecurityException {

		byte[] b = readByteArray(encrypted);

		byte[] ivdata = new byte[16];

		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		bais.read(ivdata);

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

		c.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivdata));

		return new CipherInputStream(bais, c);
	}

	protected String decryptString(String cipherText, SecretKey sk)
			throws GeneralSecurityException {

		try {

			InputStream input = null;

			;
			input = decrypt(new ByteArrayInputStream(BaseEncoding.base64()
					.decode(cipherText)), sk);
			byte[] b = readFully(input);

			String decryptedString = new String(b, "UTF-8");
			if (Strings.isNullOrEmpty(decryptedString)) {

				throw new GeneralSecurityException();
			}
			return decryptedString;
		} catch (IOException e) {
			throw new GeneralSecurityException(e);
		}
		catch (IllegalArgumentException e) {
			throw new GeneralSecurityException(e);
		}

	}

	public String decryptStringWithPassThrough(String input) {
		try {
			return decryptString(input);
		} catch (Exception e) {
			return input;
		}
	}

	/**
	 * Take a string and parse JSON if it is an encrypted envelope.
	 * 
	 * @param input
	 * @return
	 */
	Optional<JsonObject> decodeEnvelope(String input) {
		
		
		try {
			input = new String(BaseEncoding.base64().decode(input),"UTF-8");
			if (input==null || input.length()<2 || input.charAt(0)!='{') {
				return Optional.absent();
			}
			JsonObject obj = Json.createReader(new StringReader(input))
					.readObject();
			if (obj.containsKey("d") && obj.containsKey("k")) {
				return Optional.of(obj);
			} else {
				return Optional.absent();
			}
		} catch (Exception e) {
			return Optional.absent();
		}

	}

	public String decryptString(String input) throws GeneralSecurityException {

		Optional<JsonObject> envelope = decodeEnvelope(input);
		if (envelope.isPresent()) {
			String keyAlias = envelope.get().getString("k");
			SecretKey key = (SecretKey) keyStoreManager.getKey(keyAlias);
			return decryptString(envelope.get().getString("d"), key);
		}
		throw new GeneralSecurityException("could not decrypt");

	}

	protected Cipher cipherForKey(SecretKey key)
			throws GeneralSecurityException {

		if (!"AES".equals(key.getAlgorithm())) {
			throw new GeneralSecurityException("key algorithm not supported: "
					+ key.getAlgorithm());
		}
		return Cipher.getInstance("AES/CBC/PKCS5Padding");
	}

	protected String encryptString(String plainText, SecretKey secretKey,
			String alias) throws GeneralSecurityException {
		try {
			Cipher c = cipherForKey(secretKey);

			byte[] ivdata = new byte[16];
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.nextBytes(ivdata);

			// DerivedKey key = deriveKey(pbe, salt,keyLenBits, 16*8);
			IvParameterSpec ivps = new IvParameterSpec(ivdata);
			SecretKeySpec sks = new SecretKeySpec(secretKey.getEncoded(),
					secretKey.getAlgorithm());

			c.init(Cipher.ENCRYPT_MODE, sks, ivps);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			baos.write(ivdata);
			CipherOutputStream cos = new CipherOutputStream(baos, c);
			cos.write(plainText.getBytes("UTF-8"));
			cos.close();

			String encoded = BaseEncoding.base64().encode(baos.toByteArray());
			String encodedEnvelope = BaseEncoding.base64().encode(Json.createObjectBuilder().add("k", alias)
					.add("d", encoded).build().toString().getBytes("UTF-8"));
			return encodedEnvelope;

		} catch (IOException e) {
			throw new GeneralSecurityException(e);
		}

	}

	protected byte[] readFully(InputStream input) throws IOException {
		if (input == null) {
			throw new NullPointerException("readFully() cannot accept null");
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1];
		int len = 0;
		while ((len = input.read(buffer)) > 0) {
			baos.write(buffer, 0, len);
		}

		baos.close();
		return baos.toByteArray();
	}

}
