package com.abdullahsolutions.belanjawan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class BelanjawanActivity extends Activity {
	private BelanjawanData belanjawandata;
	
	private static int[] TO = {R.id.rowname,R.id.rowamount};
	private static String[] FROM = { "name","bdgt_amount"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        belanjawandata = new BelanjawanData(this);
        updateLists();
    }
    
    private OnItemClickListener transactionClickedHandler = new OnItemClickListener() {
        @SuppressWarnings("unchecked")
		public void onItemClick(AdapterView parent, View v, int position, long id)
        {
            // Display a messagebox.            
        	startActivity(new Intent(parent.getContext(),TransactionActivity.class).putExtra("budget_id", (Integer)v.getTag()));
        }
    };
    
    private OnItemLongClickListener transactionLongClickedHandler = new OnItemLongClickListener () {
    	public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
    		startActivity(new Intent(parent.getContext(),ListTransactionActivity.class).putExtra("budget_id", (Integer)v.getTag()));
            return true;
    	} 
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()){
    	case R.id.managebudget:
    		startActivity(new Intent(this,ManageBudgetActivity.class));
    		return true;
    	}
    	return false;
    }
    
    public void updateLists(){
		SQLiteDatabase db = belanjawandata.getReadableDatabase();
		//Cursor income = db.query("budget", FROM, "budgettype='income'", null, null, null, null);
		final Cursor income = db.rawQuery("select _id,name,amount - (select sum(bt.amount) from budget_transaction as bt where bt.budget_id=budget._id and strftime('%m %Y',bt.transactiondate)=strftime('%m %Y','now') ) as bdgt_amount from budget where budgettype='income'", null);
		startManagingCursor(income);
		SimpleCursorAdapter incomeadapter = new SimpleCursorAdapter(this,R.layout.budgetitem,income,FROM,TO){
		    @Override
		    public View getView (int position, View convertView, ViewGroup parent) {
		        View view = super.getView(position, convertView, parent);
		        view.setTag(income.getInt(0));
		        return view;
		    }
		};

		ListView incomelist = (ListView)findViewById(R.id.mainincomelist);
		incomelist.setAdapter(incomeadapter);		
		incomelist.setOnItemClickListener(transactionClickedHandler);
		incomelist.setOnItemLongClickListener(transactionLongClickedHandler); 
		final Cursor expense = db.rawQuery("select _id,name,amount - (select sum(bt.amount) from budget_transaction as bt where bt.budget_id=budget._id and strftime('%m %Y',bt.transactiondate)=strftime('%m %Y','now') ) as bdgt_amount from budget where budgettype='expenses'", null);
		startManagingCursor(expense);
		SimpleCursorAdapter expenseadapter = new SimpleCursorAdapter(this,R.layout.budgetitem,expense,FROM,TO){
		    @Override
		    public View getView (int position, View convertView, ViewGroup parent) {
		        View view = super.getView(position, convertView, parent);
		        view.setTag(expense.getInt(0));
		        return view;
		    }
		};
		ListView expenselist = (ListView)findViewById(R.id.mainexpenselist);
		expenselist.setAdapter(expenseadapter);
		expenselist.setOnItemClickListener(transactionClickedHandler);
		expenselist.setOnItemLongClickListener(transactionLongClickedHandler);
	}
}