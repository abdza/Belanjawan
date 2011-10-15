package com.abdullahsolutions.belanjawan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ManageBudgetActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managebudget);
    }
	
	public void addIncome(View view){
		startActivity(new Intent(this,EditBudgetActivity.class).putExtra("action", "addIncome"));		
	}
	
	public void addExpenses(View view){
		startActivity(new Intent(this,EditBudgetActivity.class).putExtra("action", "addExpenses"));		
	}
}
