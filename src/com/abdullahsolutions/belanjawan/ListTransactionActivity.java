package com.abdullahsolutions.belanjawan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ListTransactionActivity extends Activity {
	private BelanjawanData belanjawandata;
	
	private static int[] TO = {R.id.rowdate, R.id.rowname,R.id.rowamount};
	private static String[] FROM = { "transactiondate", "name", "amount"};
	
	private Integer budget_id;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();        
        if(extras !=null)
        {
        	budget_id = extras.getInt("budget_id");
        }
        setContentView(R.layout.listtransaction);
        belanjawandata = new BelanjawanData(this);
        SQLiteDatabase db = belanjawandata.getReadableDatabase();
        Cursor transactions = db.rawQuery("select _id,name from budget where _id=" + budget_id,null);
        transactions.moveToFirst();
        this.setTitle("Transactions for " + transactions.getString(1));
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
    
    public void updateLists(){
		SQLiteDatabase db = belanjawandata.getReadableDatabase();
		final Cursor transactions = db.rawQuery("select bt._id as _id, date(bt.transactiondate) as transactiondate, b.name as name, bt.amount as amount from budget as b, budget_transaction as bt where bt.budget_id=b._id and b._id=" + budget_id + "  and strftime('%m %Y',bt.transactiondate)=strftime('%m %Y','now')", null);
		startManagingCursor(transactions);
		SimpleCursorAdapter transactionadapter = new SimpleCursorAdapter(this,R.layout.transactionitem,transactions,FROM,TO){
		    @Override
		    public View getView (int position, View convertView, ViewGroup parent) {
		        View view = super.getView(position, convertView, parent);
		        view.setTag(transactions.getInt(0));
		        return view;
		    }
		};

		ListView transactionlist = (ListView)findViewById(R.id.transactionlist);
		transactionlist.setAdapter(transactionadapter);		
		transactionlist.setOnItemClickListener(transactionClickedHandler);
	}
}
