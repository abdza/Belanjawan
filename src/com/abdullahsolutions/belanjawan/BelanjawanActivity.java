package com.abdullahsolutions.belanjawan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
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
    
    private boolean dumpdata(){
    	SQLiteDatabase db = belanjawandata.getReadableDatabase();
		final Cursor expenses = db.rawQuery("select bt._id as _id, date(bt.transactiondate) as transactiondate, b.name as name, bt.amount as amount from budget as b, budget_transaction as bt where bt.budget_id=b._id and b.budgettype='expenses' order by bt.transactiondate", null);
		startManagingCursor(expenses);
		File sdDir = Environment.getExternalStorageDirectory();
		File rootdir = null;
		if( sdDir.exists() && sdDir.canWrite()){
			rootdir = new File(sdDir.getAbsolutePath() + "/belanjawan");
			if(!rootdir.exists()){
				rootdir.mkdirs();
			}
		}
		FileOutputStream fos = null;
		try {
			getApplicationContext();
			if(rootdir != null){
				fos = new FileOutputStream(new File(rootdir.getAbsolutePath() + "/expenses.csv"));
			}
			else{
				fos = openFileOutput("expenses.csv",Context.MODE_PRIVATE);
			}
			while(expenses.moveToNext()){
				fos.write((expenses.getString(1) + "," + expenses.getString(2) + "," + expenses.getDouble(3) + "\n").getBytes());				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if( fos != null ){
				try {
					fos.flush();
					fos.close();
				}
				catch (IOException e) {
					
				}
			}
		}
		
		final Cursor incomes = db.rawQuery("select bt._id as _id, date(bt.transactiondate) as transactiondate, b.name as name, bt.amount as amount from budget as b, budget_transaction as bt where bt.budget_id=b._id and b.budgettype='income' order by bt.transactiondate", null);
		startManagingCursor(incomes);
		fos = null;
		try {
			getApplicationContext();
			if(rootdir != null){
				fos = new FileOutputStream(new File(rootdir.getAbsolutePath() + "/incomes.csv"));
			}
			else{
				fos = openFileOutput("incomes.csv",Context.MODE_PRIVATE);
			}
			while(incomes.moveToNext()){
				fos.write((incomes.getString(1) + "," + incomes.getString(2) + "," + incomes.getDouble(3) + "\n").getBytes());				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if( fos != null ){
				try {
					fos.flush();
					fos.close();
				}
				catch (IOException e) {
					
				}
			}
		}
		
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()){
    	case R.id.managebudget:
    		startActivity(new Intent(this,ManageBudgetActivity.class));
    		return true;
    	case R.id.manageinstallments:
    		startActivity(new Intent(this,ManageInstallmentsActivity.class));
    		return true;
    	case R.id.dumpdata:
    		if(dumpdata()){
    			 new AlertDialog.Builder(this)
    		      	.setMessage("Data is dumped")
    		      	.setPositiveButton("OK", null)
    		      	.show();
    			return true;	
    		}
    		else{
    			new AlertDialog.Builder(this)
  		      		.setMessage("Data failed to dump")
  		      		.setPositiveButton("OK", null)
  		      		.show();
    			return false;	
    		}
    		
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