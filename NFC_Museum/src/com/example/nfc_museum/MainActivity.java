package com.example.nfc_museum;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button intro_btn;
	private Button exhib_btn;
	private Button pinfo_btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}
	
	@Override
	public void onStart(){
		super.onStart();				
	}
	
	@Override
	public void onResume(){
		super.onResume();
		setContentView(R.layout.activity_main);		
		
		intro_btn = (Button) findViewById(R.id.introduction);
		exhib_btn = (Button) findViewById(R.id.exhibition);
		pinfo_btn = (Button) findViewById(R.id.personal_info);
		//dsaldsf
		
		intro_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this, Introduction.class);
				startActivity(intent);
			}
		});
		
		exhib_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this, Exhibition.class);
				startActivity(intent);
			}
		});
		
		
		pinfo_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this, Personal_info.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
