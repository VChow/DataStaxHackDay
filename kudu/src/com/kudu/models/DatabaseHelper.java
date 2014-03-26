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
	private String[] allColumns = new String[] { SESSION_USERNAME, SESSION_UUID };

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SESSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
		onCreate(db);	
	}
	
	public void createTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		//DROP TABLES - used for testing
		//db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
		db.execSQL(CREATE_TABLE_SESSION);
	}
	
	public void insertSession(String uuid, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SESSION_UUID, uuid);
		values.put(SESSION_USERNAME, username);
		db.insert(SESSION_TABLE, null, values);
		ns.setUsername(username);
		ns.setUuid(uuid);
		Log.v("Session:", "insertSession: "+ns.getUsername());
		Log.v("Session:", "insertSession: "+ns.getUuid());
		Log.v("Session:", "insertSession");
		db.close();
	}
	
	public Session getSession(String uuid, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(SESSION_TABLE, allColumns, null, null, null, null, null);
		if(cursor!=null && cursor.getCount()>0) {
			cursor.moveToFirst();
			String sessionUsername = cursor.getString(0);
			String sessionUUID = cursor.getString(1);
			
			ns.setUsername(sessionUsername);
			ns.setUuid(sessionUUID);
			//Testing
			Log.v("Session:", "getSession: "+ns.getUsername());
			Log.v("Session:", "getSession: "+ns.getUuid());
			db.close();
			return ns;
		} else {
			insertSession(uuid, username);
		}
		return null;
	}
	
	public Session checkSessionExists() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(SESSION_TABLE, allColumns, null, null, null, null, null);
		if(cursor!=null && cursor.getCount()>0) {
			cursor.moveToFirst();
			Log.v("Session:", "cse1: "+ns.getUsername());
			Log.v("Session:", "cse1: "+ns.getUuid());
			return ns;
		}
		ns.setUsername(null);
		ns.setUuid(null);
		Log.v("Session:", "cse2: "+ns.getUuid());
		Log.v("Session:", "cse2: "+ns.getUsername());
		return ns;
	}
	
	public void deleteSession(String uuid, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(SESSION_TABLE, SESSION_USERNAME+" = ?", new String[] { username });
		ns.setUsername(null);
		ns.setUuid(null);
		Log.v("Session:", "deleteSession");
		db.close();
	}
}