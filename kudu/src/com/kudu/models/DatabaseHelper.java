package com.kudu.models;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.Context;
import android.provider.BaseColumns;
import android.util.Log;

/** Helper to the database, manages versions and creation */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "kudu.db";
	private static final int DATABASE_VERSION = 1;

	// Table name
	public static final String TABLE = "keys";

	// Columns
	public static final String UUID = "uuid";
	public static final String key = "key";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE + "( " + UUID + " text PRIMARY KEY, "
				+ key + " text not null);";
		Log.d("EventsData", "onCreate: " + sql);
		db.execSQL(sql);
		sql = "create table session (username text PRIMARY KEY, UUID text);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion)
			return;

		String sql = null;
		if (oldVersion == 1)
			sql = "alter table " + TABLE + " add note text;";
		if (oldVersion == 2)
			sql = "";

		Log.d("EventsData", "onUpgrade : " + sql);
		if (sql != null)
			db.execSQL(sql);
	}

}