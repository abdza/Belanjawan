package com.abdullahsolutions.belanjawan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EditBudgetActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editbudget);
        Bundle extras = getIntent().getExtras();
        String value = "Nothing";
        if(extras !=null)
        {
        	value = extras.getString("action");
        }
        TextView title = (TextView)findViewById(R.id.editbudget_title);
        title.setText(value);
    }
	
	public void saveBudget(View view){
		
	}
}
