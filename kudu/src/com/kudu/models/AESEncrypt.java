package com.kudu.models;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.*;

import com.kudu.activities.MainActivity;

public class AESEncrypt {
	public AESEncrypt() {}
	
	public String Encrypt(String message, String friendName) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		String key = MainActivity.db.getAES(friendName);
		
		byte[] bytes = key.getBytes(Charset.forName("UTF-8"));
		SecretKey sc = new SecretKeySpec(bytes, "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, sc);
		byte[] encVal = c.doFinal(message.getBytes());
		String encryto = new String(encVal,"UTF-8");
		return encryto;
	}
	
	public String Decrypt(String message, String friendName) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		String key = MainActivity.db.getAES(friendName);
		byte[] bytes = key.getBytes(Charset.forName("UTF-8"));
		SecretKey sc = new SecretKeySpec(bytes, "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.DECRYPT_MODE, sc);
		byte[] encVal = c.doFinal(message.getBytes());
		String decryto = new String(encVal,"UTF-8");
		return decryto;
	}
	
}
