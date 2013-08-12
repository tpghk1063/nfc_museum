package com.example.nfc_museum;

import android.os.Bundle;
import android.app.Activity;

public class Personal_info extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}
	
	@Override
	public void onStart(){
		super.onStart();
		setContentView(R.layout.exhibition);
	}
}
