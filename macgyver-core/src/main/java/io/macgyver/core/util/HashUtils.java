package io.macgyver.core.util;

import io.macgyver.core.MacGyverException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;

public class HashUtils {
	public static String calculateCompositeId(String... args) {

		try {
			MessageDigest md = MessageDigest.getInstance("sha1");
			StringBuffer sb = new StringBuffer();
			for (String s : args) {
				sb.append("/");
				sb.append(Strings.nullToEmpty(s));

			}
			md.update(sb.toString().getBytes("UTF-8"));
			return BaseEncoding.base16().lowerCase().encode(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new MacGyverException(e);
		} catch (UnsupportedEncodingException e) {
			throw new MacGyverException(e);
		}

	}
}
