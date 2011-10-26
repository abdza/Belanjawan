package com.abdullahsolutions.belanjawan;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditBudgetActivity extends Activity {
	private BelanjawanData belanjawandata;
	private String budgettype;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editbudget);
        Bundle extras = getIntent().getExtras();        
        if(extras !=null)
        {
        	budgettype = extras.getString("action");
        }
        TextView title = (TextView)findViewById(R.id.editbudget_title);
        title.setText(budgettype);
        belanjawandata = new BelanjawanData(this);
    }
	
	public void saveBudget(View view){
		SQLiteDatabase db = belanjawandata.getWritableDatabase();
		ContentValues values = new ContentValues();		
		values.put("name", ((EditText)findViewById(R.id.budget_name)).getText().toString());
		values.put("amount", ((EditText)findViewById(R.id.budget_amount)).getText().toString());
		if(budgettype.equals("addIncome")){
			values.put("budgettype", "income");
		}
		else if(budgettype.equals("addExpenses")){
			values.put("budgettype", "expenses");
		}
		db.insertOrThrow("budget", null, values);	
		finish();
	}
}
