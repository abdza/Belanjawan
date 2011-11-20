package com.abdullahsolutions.belanjawan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ManageBudgetActivity extends Activity {
	private BelanjawanData belanjawandata;
	
	private static int[] TO = {R.id.rowname,R.id.rowamount};
	private static String[] FROM = { "name","amount"};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managebudget);
        belanjawandata = new BelanjawanData(this);
        updateLists();
    }
	
	public void addIncome(View view){
		startActivity(new Intent(this,EditBudgetActivity.class).putExtra("action", "addIncome"));		
	}
	
	public void addExpenses(View view){
		startActivity(new Intent(this,EditBudgetActivity.class).putExtra("action", "addExpenses"));		
	}
	
	public void updateLists(){
		SQLiteDatabase db = belanjawandata.getReadableDatabase();
		Cursor income = db.rawQuery("select _id,name,amount from budget where budgettype='income'", null);
		startManagingCursor(income);
		SimpleCursorAdapter incomeadapter = new SimpleCursorAdapter(this,R.layout.budgetitem,income,FROM,TO);
		ListView incomelist = (ListView)findViewById(R.id.incomelist);
		incomelist.setAdapter(incomeadapter);
		Cursor expense = db.rawQuery("select _id,name,amount from budget where budgettype='expenses'", null);
		startManagingCursor(expense);
		SimpleCursorAdapter expenseadapter = new SimpleCursorAdapter(this,R.layout.budgetitem,expense,FROM,TO);
		ListView expenselist = (ListView)findViewById(R.id.expenselist);
		expenselist.setAdapter(expenseadapter);
	}
}
