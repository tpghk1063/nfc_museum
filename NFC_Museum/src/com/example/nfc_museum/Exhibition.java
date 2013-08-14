package com.example.nfc_museum;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;





public class Exhibition extends Activity{
	
	private Button country_btn;
	private Button texture_btn;
	private Button use_btn;
	private Button design_btn;	
	
	private static final int DIALOG_YES_OR_NO_MESSAGE=1;
	String country[];
	
	
	
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
		
		
	
		country_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(Exhibition.this,Exhibit_list.class);
				startActivity(intent);
				
			}
		});
		
	}
}
