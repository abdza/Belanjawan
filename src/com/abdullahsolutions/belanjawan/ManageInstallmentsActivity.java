package com.abdullahsolutions.belanjawan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ManageInstallmentsActivity extends Activity {
private BelanjawanData belanjawandata;
	
	private static int[] TO = {R.id.rowname,R.id.rowstart,R.id.rowend,R.id.rowmonthly};
	private static String[] FROM = { "name","installmentstart","installmentend","monthly"};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manageinstallments);
        belanjawandata = new BelanjawanData(this);
        updateLists();
    }
	
	public void addInstallments(View view){
		startActivity(new Intent(this,EditBudgetActivity.class));		
	}
	
	public void updateLists(){
		SQLiteDatabase db = belanjawandata.getReadableDatabase();		
		Cursor installments = db.rawQuery("select _id,name,date(installmentstart) as installmentstart,date(installmentend) as installmentend, monthly from installments", null);
		startManagingCursor(installments);
		SimpleCursorAdapter installmentsadapter = new SimpleCursorAdapter(this,R.layout.installmentitem,installments,FROM,TO);
		ListView installmentslist = (ListView)findViewById(R.id.installmentslist);
		installmentslist.setAdapter(installmentsadapter);		
	}
}
