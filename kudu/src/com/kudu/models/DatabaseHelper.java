package com.kudu.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/** Helper to the database, manages versions and creation */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "kudu.db";
	private static final int DATABASE_VERSION = 1;
	public Session ns = new Session();
	
	//SessionTable
	public static final String SESSION_TABLE = "session";
	//SessionColumns
	public static final String SESSION_UUID = "session_uuid";
	public static final String SESSION_USERNAME = "session_username";
	//SessionTable - Create Statement
	public static final String CREATE_TABLE_SESSION = "CREATE TABLE IF NOT EXISTS "
			+SESSION_TABLE+ "(" + SESSION_USERNAME + " TEXT PRIMARY KEY,"
			+SESSION_UUID+ " TEXT" + ")";
	
	//AESTable
	public static final String AES_TABLE = "aes";
	//AESColumns
	public static final String AES_FRIEND = "aes_friend";
	public static final String AES_DIFFIE = "aes_diffie";
	//AESTable - Create Statement
	public static final String CREATE_TABLE_AES = "CREATE TABLE IF NOT EXISTS "
				+AES_TABLE+ "(" +AES_FRIEND+ " TEXT PRIMARY KEY, " +AES_DIFFIE+ " TEXT" + ")";
	
	//DiffieTable
	public static final String DIFFIE_TABLE = "diffie";
	//DiffieColumns
	public static final String DIFFIE_FRIEND = "diffie_friend";
	public static final String DIFFIE_DIFFIE = "diffie_diffie";
	//DiffieTable - Create Statement
	public static final String CREATE_TABLE_DIFFIE = "CREATE TABLE IF NOT EXISTS "
			+DIFFIE_TABLE+ "(" +DIFFIE_FRIEND+ " TEXT PRIMARY KEY, " +DIFFIE_DIFFIE+ " TEXT" + ")";

	private String[] allSessionColumns = new String[] { SESSION_USERNAME, SESSION_UUID };
	private String[] allAESColumns = new String[] { AES_FRIEND, AES_DIFFIE }; 
	private String[] allDiffieColumns = new String[] { };
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SESSION);
		db.execSQL(CREATE_TABLE_AES);
		db.execSQL(CREATE_TABLE_DIFFIE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + AES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + DIFFIE_TABLE);
		onCreate(db);	
	}
	
	public void createTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		//DROP TABLES - used for testing
		//db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
		//db.execSQL("DROP TABLE IF EXISTS " + AES_TABLE);
		//db.execSQL("DROP TABLE IF EXISTS " + DIFFIE_TABLE);
		db.execSQL(CREATE_TABLE_SESSION);
		db.execSQL(CREATE_TABLE_AES);
		db.execSQL(CREATE_TABLE_DIFFIE);
	}
	
	/*
	 * SESSION TABLE
	 */
	public void insertSession(String uuid, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SESSION_UUID, uuid);
		values.put(SESSION_USERNAME, username);
		db.insert(SESSION_TABLE, null, values);
		ns.setUsername(username);
		ns.setUuid(uuid);
		db.close();
	}
	
	public Session getSession(String uuid, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(SESSION_TABLE, allSessionColumns, null, null, null, null, null);
		if(cursor!=null && cursor.getCount()>0) {
			cursor.moveToFirst();
			String sessionUsername = cursor.getString(0);
			String sessionUUID = cursor.getString(1);
			
			ns.setUsername(sessionUsername);
			ns.setUuid(sessionUUID);
			db.close();
			return ns;
		} else {
			insertSession(uuid, username);
		}
		return null;
	}
	
	public Session checkSessionExists() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(SESSION_TABLE, allSessionColumns, null, null, null, null, null);
		if(cursor!=null && cursor.getCount()>0) {
			cursor.moveToFirst();
			return ns;
		}
		ns.setUsername(null);
		ns.setUuid(null);
		return ns;
	}
	
	public void deleteSession(String uuid, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(SESSION_TABLE, SESSION_USERNAME+" = ?", new String[] { username });
		ns.setUsername(null);
		ns.setUuid(null);
		db.close();
	}
	
	/*
	 * AES TABLE
	 */
	public void insertAES(String friend, String diffie) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(AES_FRIEND, friend);
		values.put(AES_FRIEND, diffie);
		db.insert(AES_TABLE, null, values);
		db.close();
	}
	
	public boolean checkAESExists(String friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(AES_TABLE, allSessionColumns, AES_FRIEND+""+"=?", new String[] {friend}, null, null, null);
		if(cursor!=null && cursor.getCount()>0) {
			cursor.moveToFirst();
			db.close();
			return true;
		}
		db.close();
		return false;
	}
	
	public String getAES(String friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(AES_TABLE, allAESColumns, AES_FRIEND+""+"=?", new String[] {friend}, null, null, null);
		
		if(cursor!=null && cursor.getCount()>0) {
			cursor.moveToFirst();
			String friendName = cursor.getString(0);
			db.close();
			return friendName;
		} else {
			return null;
		}
	}
		
	public boolean deleteAES(String friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(AES_TABLE, AES_FRIEND+" = ?", new String[] { friend });	
		db.close();
		return true;
	}
	
	/*
	 * DIFFIE TABLE
	 */
	public void insertDiffie(String friend, String diffie) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DIFFIE_FRIEND, friend);
		values.put(DIFFIE_DIFFIE, diffie);
		db.insert(DIFFIE_TABLE, null, values);
		db.close();
	}
	
	public boolean checkDiffieExists(String friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(DIFFIE_TABLE, allDiffieColumns, DIFFIE_FRIEND+""+"=?", new String[] {friend}, null, null, null);
		if(cursor!=null && cursor.getCount()>0) {
			cursor.moveToFirst();
			db.close();
			return true;
		}
		db.close();
		return false;
	}
	
	public String getDiffie(String friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(AES_TABLE, allSessionColumns, DIFFIE_FRIEND+""+"=?", new String[] {friend}, null, null, null);
		
		if(cursor!=null && cursor.getCount()>0) {
			cursor.moveToFirst();
			String friendName = cursor.getString(0);
			db.close();
			return friendName;
		} else {
			return null;
		}
	}
		
	public boolean deleteDiffie(String friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(AES_TABLE, DIFFIE_FRIEND+" = ?", new String[] { friend });	
		db.close();
		return true;
	}
}