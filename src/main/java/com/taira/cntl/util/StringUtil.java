package com.taira.cntl.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class StringUtil {

	public static String getHashValue(String content, String instanceType) {
		MessageDigest md;
		byte[] mdBytes;
		try {
			md = MessageDigest.getInstance(instanceType);
			md.update(content.getBytes());
			mdBytes = md.digest();
			String password = DatatypeConverter.printHexBinary(mdBytes);
			return password;
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}
}
