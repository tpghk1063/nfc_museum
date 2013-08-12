package com.example.nfc_museum;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
	
	//국가 선택 대화 상자 추가
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
		case DIALOG_YES_OR_NO_MESSAGE:
			final CharSequence[] items = {"China","Korea","Japan"};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select contry");
			builder.setItems(items, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog,int item){
					Intent in = new Intent(Exhibition.this,Exhibit_list.class);
					in.putExtra("country",items[item]);
					startActivity(in);
				}
			});
			AlertDialog alert = builder.create();
			return alert;
		}
		return null;
	}
	
	
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
				showDialog(DIALOG_YES_OR_NO_MESSAGE); //버튼 클릭시 대화 상자 뜸
			}
		});
		
	}
}
