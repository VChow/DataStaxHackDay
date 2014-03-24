package com.kudu.models;

import java.io.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteQueryBuilder;

public final class DatabaseModel {
	String dbpassword;
	SQLiteDatabase db;
	DatabaseHelper help;
	Activity activity;
	
	public DatabaseModel(Context context, String pass, Activity active)
	{
		SQLiteDatabase.loadLibs(context);
		help =  new DatabaseHelper(context);
		activity = active;
		
		dbpassword = pass;
		
		db = help.getWritableDatabase(dbpassword);
		
	}
	
	public boolean addKey(String UUID, String key)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.UUID, UUID);
		values.put(DatabaseHelper.key, key);
		db.insert(DatabaseHelper.TABLE, null, values);
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public String getKeys(String UUID)
	{
		Cursor cursor = db.query(DatabaseHelper.TABLE, new String[]{DatabaseHelper.key}, "UUID=?", new String[]{UUID}, null, null, null);
		
		activity.startManagingCursor(cursor);
		
		return cursor.getString(0);
		
	}
	
	

}
