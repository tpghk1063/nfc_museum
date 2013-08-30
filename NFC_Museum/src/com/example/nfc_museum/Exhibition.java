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
	private String search_range = "��ü�˻�"; //����Ʈ�� ��ü�˻�
	private String[] range = {"��ü�˻�","�����˻�","�ô�˻�","�̸��˻�"};	

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
				android.R.layout.simple_spinner_item, range); // ����Ϳ� ����Ʈ ����
		range_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ����͸�
																							// ��Ӵٿ�
																							// �信
																							// �޾���

		spinner.setAdapter(range_adapter); // ����͸� ���ǳʿ� ����

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (pos == 0) {
					search_range = "��ü�˻�"; //�˻� ���� ���ڿ� ����
				} else if (pos == 1) {
					search_range = "�����˻�";
				} else if (pos == 2) {
					search_range = "�ô�˻�";
				} else if (pos == 3) {
					search_range = "�̸��˻�";
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
					Toast.makeText(getApplicationContext(), "�˻�� �Է��ϼ���",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(Exhibition.this,
							Exhibit_list.class);
					intent.putExtra("SEARCH_TEXT", edit.getText().toString()); //�˻��� ����
					intent.putExtra("RANGE_TEXT",search_range); // �˻� ���� ����
					startActivity(intent);
				}

			}
		});

		country_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Exhibition.this, Exhibit_list.class);
				intent.putExtra("CATEGORY","����"); // �˻� ���� ����
				startActivity(intent);

			}
		});

		texture_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Exhibition.this, Exhibit_list.class);
				intent.putExtra("CATEGORY","����"); // �˻� ���� ����
				startActivity(intent);

			}
		});

		use_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Exhibition.this, Exhibit_list.class);
				intent.putExtra("CATEGORY","�뵵"); // �˻� ���� ����
				startActivity(intent);

			}
		});

		design_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Exhibition.this, Exhibit_list.class);
				intent.putExtra("CATEGORY","����"); // �˻� ���� ����
				startActivity(intent);

			}
		});
	}
}
