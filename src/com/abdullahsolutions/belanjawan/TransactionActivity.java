package com.abdullahsolutions.belanjawan;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TransactionActivity extends Activity {
	private BelanjawanData belanjawandata;
	private Integer budget_id;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dotransaction);
        Bundle extras = getIntent().getExtras();        
        if(extras !=null)
        {
        	budget_id = extras.getInt("budget_id");
        }                
        belanjawandata = new BelanjawanData(this);
    }
	
	public void saveTransaction(View view){
		SQLiteDatabase db = belanjawandata.getWritableDatabase();
		ContentValues values = new ContentValues();		
		values.put("amount", ((EditText)findViewById(R.id.transaction_amount)).getText().toString());
		values.put("description", ((EditText)findViewById(R.id.transaction_desc)).getText().toString());
		values.put("budget_id", budget_id);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		values.put("transactiondate", dateFormat.format(date));
		db.insertOrThrow("budget_transaction", null, values);	
		finish();
	}
}
