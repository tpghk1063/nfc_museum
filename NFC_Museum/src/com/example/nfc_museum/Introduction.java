package com.example.nfc_museum;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class Introduction extends Activity{
	private Button Location_btn;
	private Button History_btn;
	private Button Guide_btn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
	}
	
	@Override
	public void onStart(){
		super.onStart();
		setContentView(R.layout.introduction);
		
		Location_btn = (Button)findViewById(R.id.map);
		History_btn = (Button)findViewById(R.id.history);
		Guide_btn = (Button)findViewById(R.id.guide);
		
		Location_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(Introduction.this, Location.class);
				startActivity(intent);
			}
		});
		
		History_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(Introduction.this, History.class);
				startActivity(intent);
			}
		});
		
		Guide_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(Introduction.this, Guidence.class);
				startActivity(intent);
			}
		});
		
	}
}
