package com.example.nfc_museum;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import android.app.Activity;
//import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class Exhibit_list extends Activity {
	
	private ListView list;
	private Exhibit_Adapter adapter;
	private ArrayList<exhibit_data> Data =null;
	private ArrayList<exhibit_data> display_Data =null;
	private String cmp_country = null;
	private String cmp_age = null;
	private String cmp_texture = null;
	private String cmp_use = null;
	private String cmp_design = null;

	private String[] nation = { "국가선택", "한국", "중국", "기타" };
	private String[] kor_era = { "시대선택","청동기","고구려","백제","신라","통일신라","고려","조선","대한제국","일제강점","광복이후","시대미상"};
	private String[] ch_era = { "시대선택", "남북조","청","시대미상" };
	
	private Spinner spinner2 = null;
	private int country_num = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exhibit_list);


		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> country_adapter;
		country_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, nation); // 어댑터에 리스트 연결
		country_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 어댑터를
																							// 드롭다운
																							// 뷰에
																							// 연결
		spinner.setAdapter(country_adapter); // 어댑터를 스피너에 설정

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (pos == 0) {
					enable_spinner2(null,3);
				} else if (pos == 1) {
					cmp_country = "한국";
					rearrange_list();
					display_list();
					enable_spinner2(kor_era,1);
				} else if (pos == 2) {
					cmp_country = "중국";
					rearrange_list();
					display_list();
					enable_spinner2(ch_era,2);
				} else if (pos == 3) {
					cmp_country = "기타";
					rearrange_list();
					display_list();
					enable_spinner2(null,3);
				}				

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		// Intent getIntent = getIntent();
		// String cmp_country = getIntent.getStringExtra("country");
		// String cmp_texture = getIntent.getStringExtra("texture");
		// String cmp_use = getIntent.getStringExtra("use");
		// String cmp_design = getIntent.getStringExtra("design");

		setData(cmp_country, cmp_texture, cmp_use, cmp_design);


	}
	
	//리스트 출력
	
	public void rearrange_list(){
		display_Data = new ArrayList<exhibit_data>();	
		for(int i = 0; i<Data.size(); i++){
			if(cmp_country!=null&&Data.get(i).getCountry().equals(cmp_country)){
				if(cmp_age==null)
					display_Data.add(Data.get(i));
				else if(Data.get(i).getAge().equals(cmp_age))
					display_Data.add(Data.get(i));
			}
			else if(cmp_texture!=null&&Data.get(i).getTexture().equals(cmp_texture))
				display_Data.add(Data.get(i));
			else if(cmp_use!=null&&Data.get(i).getUse().equals(cmp_use))
				display_Data.add(Data.get(i));
			else if(cmp_design!=null&&Data.get(i).getDesign().equals(cmp_design))
				display_Data.add(Data.get(i));
			else
				;
		}		
	}
	
	public void display_list(){

		adapter = new Exhibit_Adapter(this, display_Data);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
	
	}
	
	//두번째 스피너 활성
	public void enable_spinner2(String[] list,int num){
		country_num = num;

		spinner2 = (Spinner) findViewById(R.id.spinner2);
		
		if(country_num == 3){
			spinner2.setEnabled(false);
			return;
		}else{
			spinner2.setEnabled(true);
		}
		
		ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter);

		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				if (country_num == 1) {

					if (pos == 0) {
					} else if (pos == 1) {
						cmp_age="청동기";
						rearrange_list();
						display_list();
						//
					} else if (pos == 2) {
						cmp_age="고구려";
						rearrange_list();
						display_list();
						//
					} else {
						cmp_age="";
						rearrange_list();
						display_list();
						//
					}
				}else if(country_num == 2){
					if (pos == 0) {
					} else if (pos == 1) {
						cmp_age="남북조";
						rearrange_list();
						display_list();
						//
					} else if (pos == 2) {
						cmp_age="청";
						rearrange_list();
						display_list();
						//
					} else {
						cmp_age="시대미상";
						rearrange_list();
						display_list();
						//
					}
				}
				cmp_age = null;
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void setData(final String cmp_country, final String cmp_texture,
			final String cmp_use, final String cmp_design) {

		Data = new ArrayList<exhibit_data>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					HttpClient client = new DefaultHttpClient();

					HttpGet method = new HttpGet(
							"http://163.239.24.212:8000/index.php");

					HttpResponse response = client.execute(method);
					InputStream is = response.getEntity().getContent();

					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					XmlPullParser parser = factory.newPullParser();

					parser.setInput(is, "utf-8");

					int status = parser.getEventType();
					String name = "";
					String country = "";
					String age = "";
					String texture = "";
					String use = "";
					String location = "";
					String id = "";
					String comment = "";
					Bitmap image = null;
					String design = "";
					String tag = "";
					String url = "";					

					while (status != XmlPullParser.END_DOCUMENT) {
						tag = parser.getName();

						switch (status) {
						case XmlPullParser.START_TAG:
							if (tag.equals("image")) {
								parser.next();
								url = parser.getText();

								// 이미지 가져오기
								URL address = new URL(
										"http://163.239.24.212:8000/img/" + url);
								HttpURLConnection conn = (HttpURLConnection) address
										.openConnection();
								conn.setDoInput(true);
								conn.connect();
								InputStream input_img = conn.getInputStream();
								BufferedInputStream bis = new BufferedInputStream(
										input_img);
								image = BitmapFactory.decodeStream(bis);
								image = Bitmap.createScaledBitmap(image, 100,
										100, true);
								bis.close();

							} else if (tag.equals("name")) {
								parser.next();
								name = parser.getText();
							} else if (tag.equals("comment")) {
								parser.next();
								comment = parser.getText();
							} else if (tag.equals("country")) {
								parser.next();
								country = parser.getText();

							} else if (tag.equals("texture")) {
								parser.next();
								texture = parser.getText();
							} else if (tag.equals("use")) {
								parser.next();
								use = parser.getText();
							} else if (tag.equals("location")) {
								parser.next();
								location = parser.getText();
							} else if (tag.equals("id")) {
								parser.next();
								id = parser.getText();
							} else if (tag.equals("age")) {
								parser.next();
								age = parser.getText();
							} else if (tag.equals("design")) {
								parser.next();
								design = parser.getText();
							}
							break;
						case XmlPullParser.END_TAG:
							if (tag.equals("data"))
								Data.add(new exhibit_data(image, name, comment,
										country, texture, use, location, id,
										age, design));
							break;
						}
						parser.next();
						status = parser.getEventType();
					}
					is.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}

}
