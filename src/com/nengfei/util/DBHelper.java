package com.nengfei.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
	private static String dbName;
	private static String olddb="";
  @SuppressWarnings("unused")
private static final int VERSION = 4;
  private static SQLiteDatabase db = null;

  static SQLiteDatabase openOrGetDB(Context context,String mdbName) {
	  dbName=mdbName;
  if (db == null) {
      db = new DBHelper(context,dbName).getWritableDatabase();
     }
  else{
	  if(olddb.equals(dbName)==false){
		  olddb=dbName;
		  db = new DBHelper(context,dbName).getWritableDatabase();
	  }
	  
  }
  return db;
  }

  private DBHelper(Context context,String dbName) {
	 
    this(context,dbName  , null, 4);
    
  }

  public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);
  }

  public void onCreate(SQLiteDatabase db)
  {
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
  }
}