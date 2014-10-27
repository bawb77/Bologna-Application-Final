package com.example.checkout;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlLiteYouMeanIt extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "EmoDB";
	private static final String TABLE_RESULTS = "Results";	
	private static final String P_KEY = "id";

	public SqlLiteYouMeanIt(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_RESULTS_TABLE = "CREATE TABLE " + TABLE_RESULTS + "(" +
				P_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				")";
		db.execSQL(CREATE_RESULTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_RESULTS);
		this.onCreate(db);
	}
	public void addResult(Items It)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		//values.put(DAY, It.day);

		
		db.insert(TABLE_RESULTS, null, values);
		db.close();
	}

}
