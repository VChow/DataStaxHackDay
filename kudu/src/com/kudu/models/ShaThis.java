package com.kudu.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class ShaThis {

	public ShaThis()
	{

	}

	public static String getSha(String encrypt) throws Throwable
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(encrypt.getBytes());

		byte byteData[] = md.digest();

		//convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) 
		{
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		return sb.toString();
	}
}
