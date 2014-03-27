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
	//KeysTable
	public static final String KEYS_TABLE = "keys";
	//KeysColumns
	public static final String KEYS_USERNAME = "keys_username";
	public static final String KEYS_FRIEND = "keys_friend_uuid";
	public static final String KEYS_DIFFIE = "keys_diffie";
	//KeysTable - Create Statement
	public static final String CREATE_TABLE_KEYS = "CREATE TABLE IF NOT EXISTS "
				+KEYS_TABLE+ "(" +KEYS_USERNAME + " TEXT PRIMARY KEY,"
				+KEYS_FRIEND+ " TEXT, " +KEYS_DIFFIE+ " TEXT" + ")";
	private String[] allSessionColumns = new String[] { SESSION_USERNAME, SESSION_UUID };
	private String[] allKeyColumns = new String[] { KEYS_USERNAME, KEYS_FRIEND, KEYS_DIFFIE }; 
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SESSION);
		db.execSQL(CREATE_TABLE_KEYS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + KEYS_TABLE);
		onCreate(db);	
	}
	
	public void createTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		//DROP TABLES - used for testing
		//db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
		//db.execSQL("DROP TABLE IF EXISTS " + KEYS_TABLE);
		db.execSQL(CREATE_TABLE_SESSION);
		db.execSQL(CREATE_TABLE_KEYS);
	}
	
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
	
	public void insertKey(String username, String friend, String diffie) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEYS_USERNAME, username);
		values.put(KEYS_FRIEND, friend);
		values.put(KEYS_FRIEND, diffie);
		db.insert(KEYS_TABLE, null, values);
		db.close();
	}
	
	public boolean checkKeyExists(String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(SESSION_TABLE, allSessionColumns, "+KEYS_USERNAME+"+"=?", new String[] {username}, null, null, null);
		if(cursor!=null && cursor.getCount()>0) {
			cursor.moveToFirst();
			db.close();
			return true;
		}
		db.close();
		return false;
	}
}