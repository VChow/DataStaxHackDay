package com.kudu.models;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyModel {
	private BigDecimal modClient, modServer;
	private final BigDecimal p = new BigDecimal(
			"161584800623216335992162648767916562904579054975368147874489181252733246179848615146351680005876145824996724218326196142529142478089334141929656834361770748807571230517120578728630655119420784349953120523486154042134143011086385279644621600607278180380013105059384728688894847847831738856050183971640767199817");
	private final BigDecimal g = new BigDecimal(
			"145634980516397404927006278601806104242433921745351499973373096230120041733509934458203376775339271567342147326417787252459700811442631238772614318462672227578281950360599822086023145197416998949447779496746249173485667974973105371317977010673834800689778360489742001839773846406864924961000946558980347680573");

	public KeyModel() {}
	
	/*
	 * The key that is calculate to send to server.
	 */
	public BigDecimal calculateClientKey() {
		SecureRandom sr;
		int a = 0;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
			a = sr.nextInt(1024);
		} catch (NoSuchAlgorithmException nsae) {}

		BigDecimal calculate = p.pow(a);
		modClient = calculate.remainder(g);
		return modClient;
	}
	
	/*
	 * Calculate the key that has been sent from the server.
	 */
	public BigDecimal calculateServerKey(int keyFromServer) {
		BigDecimal calculate = p.pow(keyFromServer);
		modServer = calculate.remainder(g);
		return modServer;
	}
}

