package com.kudu.models;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.kudu.activities.MainActivity;

import android.util.Log;

public class KeyModel {
	String url = "http://10.0.3.2:8080/KuduServer/key";
	private final BigDecimal p = new BigDecimal(
			"161584800623216335992162648767916562904579054975368147874489181252733246179848615146351680005876145824996724218326196142529142478089334141929656834361770748807571230517120578728630655119420784349953120523486154042134143011086385279644621600607278180380013105059384728688894847847831738856050183971640767199817");
	private final BigDecimal g = new BigDecimal(
			"145634980516397404927006278601806104242433921745351499973373096230120041733509934458203376775339271567342147326417787252459700811442631238772614318462672227578281950360599822086023145197416998949447779496746249173485667974973105371317977010673834800689778360489742001839773846406864924961000946558980347680573");
	String send = "false";
	String retrieve = "false";
	
	public KeyModel() {}
	
	public BigDecimal generateNewKey() {
		SecureRandom sr;
		int a = 0;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
			a = sr.nextInt(1024); //local database
		} catch (NoSuchAlgorithmException nsae) {}

		BigDecimal calculate = p.pow(a);
		BigDecimal modClient = calculate.remainder(g);
		return modClient;
	}
	
	/*
	 * Calculate the key that has been sent from the server.
	 */
	public BigDecimal calculateKeyFromServer(int keyFromServer) {
		BigDecimal calculate = p.pow(keyFromServer);
		BigDecimal modServer = calculate.remainder(g);
		return modServer;
	}
	
	/*
	 * The key that is calculated to send to server.
	 */
	public void calculateClientKey(String username, String friend) throws IOException, Exception {
		String A = generateNewKey().toString();

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("send", send));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("friend", friend));
		params.add(new BasicNameValuePair("key", A));
		httppost.setEntity(new UrlEncodedFormEntity(params));
		httpclient.execute(httppost);
		send = "false";
	}
	
	public void getKey() throws ClientProtocolException, IOException {
		retrieve = "true";
		Session session = new Session();
		session = MainActivity.db.checkSessionExists();
		String username = session.getUsername();
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("retrieve", retrieve));
		params.add(new BasicNameValuePair("username", username));
		httppost.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse response = null;
		response = httpclient.execute(httppost);
		retrieve = "false";
	}
}

