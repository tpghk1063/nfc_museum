package com.example.nfc_museum;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;





public class Exhibition extends Activity{
	
	private Button country_btn;
	private Button texture_btn;
	private Button use_btn;
	private Button design_btn;	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}
	
	@Override
	public void onStart(){
		super.onStart();
		setContentView(R.layout.exhibition);
		
		country_btn = (Button) findViewById(R.id.country_btn);
		texture_btn = (Button) findViewById(R.id.texture_btn);
		use_btn = (Button) findViewById(R.id.use_btn);
		design_btn = (Button) findViewById(R.id.design_btn);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				Toast.makeText(getApplicationContext(),"선택 항목 : "+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
			}


			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});		
	
		country_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(Exhibition.this, Exhibit_list.class);
				startActivity(intent);
			}
		});
		
	}
}
