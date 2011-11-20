package com.abdullahsolutions.belanjawan;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BelanjawanData extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "belanjawan.db";
	private static final int DATABASE_VERSION = 1;
	
	public BelanjawanData(Context ctx){
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table budget (" + _ID +" integer primary key autoincrement, name text not null, budgettype text not null, amount real, budgetstart integer, budgetend integer, installment_id integer);");		
		db.execSQL("create table budget_transaction (" + _ID +" integer primary key autoincrement, amount real, transactiondate integer, budget_id integer not null, description text);");
		db.execSQL("create table installments (" + _ID +" integer primary key autoincrement, name text not null, amount real, installmentstart integer, installmentend integer, monthly real, budget_id integer);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists budget");
		db.execSQL("drop table if exists budget_transaction");
		db.execSQL("drop table if exists installments");
		onCreate(db);
	}

}
