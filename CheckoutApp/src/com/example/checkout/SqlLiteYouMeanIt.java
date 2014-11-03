package com.example.checkout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SqlLiteYouMeanIt extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "EmoDB";
	private static final String TABLE_RESULTS = "Results";	
	private static final String P_KEY = "id";
	private static final String ITEM = "item";
	private static final String PRICE = "price";
	private static final String ID = "itemId";
	private static final String GROUP = "groupId";
	private static final String PIC = "pic";
	
	private static final String TABLE_LOGS = "Logs";	
	private static final String KEY_LID = "id";
	private static final String KEY_VALUE = "value";
	private static final String KEY_DATE = "day";
	private static final String KEY_USER = "user";
	
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
				ID + " INTEGER, " +
				GROUP + " INTEGER, " +
				PIC + " BLOB" +
				")";
		db.execSQL(CREATE_RESULTS_TABLE);
		
		String CREATE_LOGS_TABLE = "CREATE TABLE " + TABLE_LOGS + "(" +
				KEY_LID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				KEY_VALUE + " STRING," +
				KEY_DATE + " STRING, " +
				KEY_USER + " INTEGER" +
				")";
		db.execSQL(CREATE_LOGS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
		this.onCreate(db);
	}
	
	public void addResult(Items It)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(ITEM, It.item);
		values.put(PRICE, It.price);
		values.put(ID, It.itemId);
		values.put(GROUP, It.group);
		values.put(PIC, getArrayFromBitmap(It.pic));
		
		db.insert(TABLE_RESULTS, null, values);
		db.close();
	}
	
	private byte[] getArrayFromBitmap(Bitmap pic){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		
		return byteArray;
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
				byte[] bitmapdata = cursor.getBlob(5);
				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);
				item = new Items(cursor.getString(1), cursor.getDouble(2), cursor.getInt(3), bitmap, cursor.getInt(4));
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
			values.put(GROUP, temp.group);
			values.put(PIC, getArrayFromBitmap(temp.pic));
			
			db.insert(TABLE_RESULTS, null, values);
		}
		db.close();
	}
	
	public void changeItem(Items item, String name, double price, int group, Bitmap pic){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ITEM, name);
		values.put(PRICE, price);
		values.put(GROUP, group);
		values.put(PIC, getArrayFromBitmap(pic));
		
		db.update(TABLE_RESULTS, values, ID +"="+ item.itemId, null);
		
		db.close();
	}
	
	public Integer getLastId(){
		Integer id = 0;
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		String sql = "SELECT last_insert_rowid()";
		Cursor cursor = db.rawQuery(sql, null);
		
		if (cursor.moveToFirst()) {
		       id = Integer.parseInt(cursor.getString(0));
		   }
		
		return id;
	}
	
	public void deleteItem(Integer id) {
		 
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(TABLE_RESULTS,
                ID +" = ?", 
                new String[] { String.valueOf(id) });
 
        db.close();
    }
	
	public void addLog(LogItem log)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(KEY_VALUE, log.value);
		values.put(KEY_DATE, log.date);
		values.put(KEY_USER, log.user_type);
		
		db.insert(TABLE_LOGS, null, values);
		db.close();
	}
	
	public ArrayList<LogItem> getAllLogs()
	{
		ArrayList<LogItem> tempArray = new ArrayList<LogItem>();
		String query = "SELECT * From " + TABLE_LOGS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		LogItem log = null;
		if(cursor.moveToFirst())
		{
			do
			{
				log = new LogItem(cursor.getString(0), cursor.getString(1), cursor.getInt(2));
				tempArray.add(log);
			}while(cursor.moveToNext());
		}
		return tempArray;
	}
	
	public ArrayList<LogItem> getLogsOfDay(String date){
		ArrayList<LogItem> tempArray = new ArrayList<LogItem>();
		String query = "SELECT * From " + TABLE_LOGS + " WHERE " + KEY_DATE + " = " + date;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		LogItem log = null;
		if(cursor.moveToFirst())
		{
			do
			{
				log = new LogItem(cursor.getString(0), cursor.getString(1), cursor.getInt(2));
				tempArray.add(log);
			}while(cursor.moveToNext());
		}
		return tempArray;
	}
}
