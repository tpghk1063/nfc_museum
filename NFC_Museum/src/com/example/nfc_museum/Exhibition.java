package com.example.nfc_museum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;



public class Exhibition extends Activity {

	private Button country_btn;
	private Button texture_btn;
	private Button use_btn;
	private Button design_btn;
	private EditText edit;

	String country[];
	private String search_range = "전체검색"; //디폴트로 전체검색
	private String[] range = {"전체검색","국가검색","시대검색","이름검색"};	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		setContentView(R.layout.exhibition);

		country_btn = (Button) findViewById(R.id.country_btn);
		texture_btn = (Button) findViewById(R.id.texture_btn);
		use_btn = (Button) findViewById(R.id.use_btn);
		design_btn = (Button) findViewById(R.id.design_btn);

		edit = (EditText) findViewById(R.id.edit_search);
		
		//----
		Spinner spinner = (Spinner) findViewById(R.id.search_spinner);
		ArrayAdapter<String> range_adapter;
		range_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, range); // 어댑터에 리스트 연결
		range_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 어댑터를
																							// 드롭다운
																							// 뷰에
																							// 달아줌

		spinner.setAdapter(range_adapter); // 어댑터를 스피너에 설정

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (pos == 0) {
					search_range = "전체검색"; //검색 범위 문자열 세팅
				} else if (pos == 1) {
					search_range = "국가검색";
				} else if (pos == 2) {
					search_range = "시대검색";
				} else if (pos == 3) {
					search_range = "이름검색";
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		//---
		
		

		ImageButton search_btn = (ImageButton) findViewById(R.id.ib_search);
		search_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edit.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "검색어를 입력하세요",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(Exhibition.this,
							Exhibit_list.class);
					intent.putExtra("SEARCH_TEXT", edit.getText().toString()); //검색어 전달
					intent.putExtra("RANGE_TEXT",search_range); // 검색 범위 전달
					startActivity(intent);
				}

			}
		});

		country_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Exhibition.this, Exhibit_list.class);
				intent.putExtra("CATEGORY","국가"); // 검색 범위 전달
				startActivity(intent);

			}
		});

		texture_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Exhibition.this, Exhibit_list.class);
				intent.putExtra("CATEGORY","재질"); // 검색 범위 전달
				startActivity(intent);

			}
		});

		use_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Exhibition.this, Exhibit_list.class);
				intent.putExtra("CATEGORY","용도"); // 검색 범위 전달
				startActivity(intent);

			}
		});

		design_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Exhibition.this, Exhibit_list.class);
				intent.putExtra("CATEGORY","문양"); // 검색 범위 전달
				startActivity(intent);

			}
		});
	}
}
