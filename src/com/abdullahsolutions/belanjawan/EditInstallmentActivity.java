package com.abdullahsolutions.belanjawan;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

public class EditInstallmentActivity extends Activity {
	private BelanjawanData belanjawandata;	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editinstallment);        
        belanjawandata = new BelanjawanData(this);
    }
	
	public void saveInstallment(View view){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		SQLiteDatabase db = belanjawandata.getWritableDatabase();
		ContentValues values = new ContentValues();		
		values.put("name", ((EditText)findViewById(R.id.budget_name)).getText().toString());
		values.put("amount", ((EditText)findViewById(R.id.budget_amount)).getText().toString());
		DatePicker dp=(DatePicker)findViewById(R.id.installmentstart);
		String date = dp.getYear() + "-" + dp.getMonth() + "-" + dp.getDayOfMonth();
		values.put("installmentstart", dateFormat.format(date));
		dp=(DatePicker)findViewById(R.id.installmentend);
		date = dp.getYear() + "-" + dp.getMonth() + "-" + dp.getDayOfMonth();
		values.put("installmentend", dateFormat.format(date));
		db.insertOrThrow("budget", null, values);	
		finish();
	}
}
