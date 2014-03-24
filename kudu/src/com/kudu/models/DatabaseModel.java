package com.kudu.models;

import java.io.File;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteQueryBuilder;

public final class DatabaseModel {
	String dbpassword;
	SQLiteDatabase db;
	DatabaseHelper help;
	
	public DatabaseModel(Context context, String pass)
	{
		SQLiteDatabase.loadLibs(context);
		help =  new DatabaseHelper(context);
		
		dbpassword = pass;
	}
	
	

}
