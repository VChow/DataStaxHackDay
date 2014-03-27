package com.kudu.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.util.Log;

import com.kudu.activities.MainActivity;

public class KeyModel {
	String url = "http://10.0.3.2:8080/KuduServer/key";
	private final BigDecimal p = new BigDecimal(
			"170251718214558806475093943055621547342725914558276901862731921101025373345183203766503793401078330287306133087019398886054513149934020527592894886076837597125664444387511413843243161474172418254393305790110877636276486154958173329219723494700475404602104944072565865257093336414884194032992540369638390241559");
	private final BigDecimal g = new BigDecimal(
			"20380168968783289261041432298494798373267709921716514919118051861699360605096686451635585045280077711531684380972902943900521986696566893077956435869388678582964796058963479557539538680897351792217018966238799242901221648112399568285623532125960239634431576271046686825745588544160244283947953684660908754447");
	String send = "false";
	String retrieve = "false";
	Boolean receivedDiffie = false;

	public KeyModel() {}

	//	public BigDecimal generateNewKey() {
	//		SecureRandom sr;
	//		int a = 0;
	//		try {
	//			sr = SecureRandom.getInstance("SHA1PRNG");
	//			a = sr.nextInt(1024); //local database
	//		} catch (NoSuchAlgorithmException nsae) {}
	//
	//		BigDecimal calculate = g.pow(a);
	//		BigDecimal modClient = calculate.remainder(p);
	//		return modClient;
	//	}
	//	
	//	/*
	//	 * Calculate the key that has been sent from the server.
	//	 */
	//	public BigDecimal calculateKeyFromServer(int keyFromServer) {
	//		BigDecimal calculate = p.pow(keyFromServer);
	//		BigDecimal modServer = calculate.remainder(g);
	//		return modServer;
	//	}

	public void getKey() throws ClientProtocolException, IOException, JSONException {
		retrieve = "true";
		Session session = new Session();
		session = MainActivity.db.checkSessionExists();
		String username = session.getUsername();

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("send", "false"));
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

		if(sb.length() > 1)
		{
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
					calculateAESKey(entry.getKey(), entry.getValue());
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
						calculateAESKey(entry.getKey(), entry.getValue());
					}
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
		db.deleteDiffie(friend);
		BigDecimal B = new BigDecimal(diffieNumber);

		BigDecimal calculate = B.pow(a);
		BigDecimal modClient = calculate.remainder(p);

		String AESKey = modClient.toString();
		Log.e("AESKEY", AESKey);

		db.insertAES(friend, AESKey);
	}

	public void sendDiffie(String friend) throws ClientProtocolException, IOException
	{
		DatabaseHelper db = MainActivity.db;

		SecureRandom sr;
		int a = 0;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
			a = sr.nextInt(2056); //local database
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
		params.add(new BasicNameValuePair("friend", friend));
		params.add(new BasicNameValuePair("key", modClient.toString()));
		httppost.setEntity(new UrlEncodedFormEntity(params));
		httpclient.execute(httppost);

	}
}

