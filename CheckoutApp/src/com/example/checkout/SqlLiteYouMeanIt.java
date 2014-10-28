package com.example.checkout;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlLiteYouMeanIt extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "EmoDB";
	private static final String TABLE_RESULTS = "Results";	
	private static final String P_KEY = "id";
	private static final String ITEM = "item";
	private static final String PRICE = "price";
	private static final String ID = "itemId";

	public SqlLiteYouMeanIt(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_RESULTS_TABLE = "CREATE TABLE " + TABLE_RESULTS + "(" +
				P_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				ITEM + " STRING," +
				PRICE + " DOUBLE, " +
				ID + "  INTEGER" +
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
		
		values.put(ITEM, It.item);
		values.put(PRICE, It.price);
		values.put(ID, It.itemId);

		
		db.insert(TABLE_RESULTS, null, values);
		db.close();
	}
	public ArrayList<Items> getAllItems()
	{
		ArrayList<Items> tempArray = new ArrayList<Items>();
		String query = "SELECT * From " + TABLE_RESULTS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		Items item = null;
		if(cursor.moveToFirst())
		{
			do
			{
				item = new Items(cursor.getString(1),Double.parseDouble(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
				tempArray.add(item);
			}while(cursor.moveToNext());
		}
		return tempArray;
	}
	public void addGroupResults(ArrayList<Items> aTemp)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		for(Items temp : aTemp)
		{
			values.put(ITEM, temp.item);
			values.put(PRICE, temp.price);
			values.put(ID, temp.itemId);
			db.insert(TABLE_RESULTS, null, values);
		}
		db.close();
	}
}
