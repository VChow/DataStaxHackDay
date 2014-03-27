package com.kudu.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.kudu.activities.MainActivity;

public class KeyModel {
	String url = "http://10.0.3.2:8080/KuduServer/key";
	private final BigDecimal p = new BigDecimal(
			"18532395500947174450709383384936679868383424444311405679463280782405796233163977");
	private final BigDecimal g = new BigDecimal(
			"2193992993218604310884461864618001945131790925282531768679169054389241527895222169476723691605898517");
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

		BigDecimal calculate = g.pow(a);
		BigDecimal modClient = calculate.remainder(p);
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
	}
	
	public void getKey() throws ClientProtocolException, IOException, JSONException {
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
		HttpResponse response = httpclient.execute(httppost);
		InputStream in = response.getEntity().getContent();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		int cp;
		while((cp = reader.read()) != -1)
		{
			sb.append((char)cp);
		}
		in.close();
		
		JSONObject json = new JSONObject(sb.toString());
		
		HashMap<String, String> list = new HashMap<String, String>();
		Iterator<String> iterate = json.keys();
		while(iterate.hasNext()) {
			String key = iterate.next();
			list.put(key, json.getString(key));

		}
		if (list.isEmpty())
		{
			
		}
		else if (list.size() == 1)
		{
			Map.Entry<String, String> entry = list.entrySet().iterator().next();
			if(checkLocalDiffie(entry.getKey()))
			{
				calculateAESKey(entry.getKey(), entry.getValue());
			}
			else
			{
				sendDiffie(entry.getKey());
			}
		}
		else
		{
			Iterator iterator = list.keySet().iterator();
			while(iterator.hasNext())
			{
				Map.Entry<String, String> entry = (Map.Entry<String, String>)iterator.next();
				if(checkLocalDiffie(entry.getKey()))
				{
					calculateAESKey(entry.getKey(), entry.getValue());
				}
				else
				{
					sendDiffie(entry.getKey());
				}
			}
		}
		
	}
	
	private boolean checkLocalDiffie(String friend)
	{
		DatabaseHelper db = MainActivity.db;
		
		return db.checkDiffieExists(friend);
	}
	
	private void calculateAESKey(String friend, String diffieNumber)
	{
		DatabaseHelper db = MainActivity.db;
		
		int a = Integer.parseInt(db.getDiffie(friend));
		BigDecimal B = new BigDecimal(diffieNumber);
		
		BigDecimal calculate = B.pow(a);
		BigDecimal modClient = calculate.remainder(p);
		
		String AESKey = modClient.toString();
		
		db.insertAES(friend, AESKey);
	}
	
	private void sendDiffie(String friend) throws ClientProtocolException, IOException
	{
		DatabaseHelper db = MainActivity.db;
		
		SecureRandom sr;
		int a = 0;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
			a = sr.nextInt(1024); //local database
		} catch (NoSuchAlgorithmException nsae) {}

		BigDecimal calculate = g.pow(a);
		BigDecimal modClient = calculate.remainder(p);
		
		db.insertDiffie(friend, String.valueOf(a));
		
		Session session = new Session();
		session = MainActivity.db.checkSessionExists();
		String username = session.getUsername();
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("retrieve", "false"));
		params.add(new BasicNameValuePair("send", "true"));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("key", modClient.toString()));
		httppost.setEntity(new UrlEncodedFormEntity(params));
		httpclient.execute(httppost);
		
	}
}

