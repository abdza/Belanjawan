package com.abdullahsolutions.belanjawan;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BelanjawanData extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "belanjawan.db";
	private static final int DATABASE_VERSION = 2;
	
	public BelanjawanData(Context ctx){
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table budget (" + _ID +" integer primary key autoincrement, name text not null, budgettype text not null, amount real, budgetstart integer, budgetend integer);");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists budget");
		onCreate(db);
	}

}
