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
import android.content.Intent;
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
import android.widget.Toast;

public class Exhibit_list extends Activity {

	private ListView list;
	private Exhibit_Adapter adapter;
	private ArrayList<exhibit_data> Data = null;
	private ArrayList<exhibit_data> display_Data = null;
	private String cmp_country = null;
	private String cmp_age = null;
	private String cmp_texture = null;
	private String cmp_use = null;
	private String cmp_design = null;
	private String search_result = null;
	private String search_range = null;

	private String category;		
	private String[] kor_era = { "시대선택", "청동기", "고구려", "백제", "신라", "통일신라","고려", "조선", "대한제국", "일제강점", "광복이후", "시대미상" };
	private String[] ch_era = { "시대선택", "남북조", "청", "시대미상" };

	private Spinner spinner2 = null;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exhibit_list);

		String[] category_selector = null;
		String[] nation = { "국가선택", "한국", "중국", "기타" };
		String[] texture = { "재질선택","토제", "지", "사직","나무","금속","도자기","석","기타"};
		String[] use = {"용도선택","산업/생활","사회생활","과학기술","문화예술","의","식","주","기타"};
		String[] design = {"문양선택","문자문","동물문","산수문","기하문","식물문"};
		
		
		Intent intent = getIntent();
		search_result = intent.getStringExtra("SEARCH_TEXT");
		search_range = intent.getStringExtra("RANGE_TEXT");
		category = intent.getStringExtra("CATEGORY");		

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter;
		
		if(category!=null){
			if(category.equals("재질")) category_selector = texture;
			else if(category.equals("용도")) category_selector = use;
			else if(category.equals("문양")) category_selector = design;
			else category_selector = nation;
		
		
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, category_selector); // 어댑터에 리스트 연결
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 어댑터를
																						// 드롭다운
																						// 뷰에
																						// 달아줌

		spinner.setAdapter(adapter); // 어댑터를 스피너에 설정
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
				
				String[] list = null;
				
				switch(pos){
				case 0:					
					break;					
				case 1:
					cmp_country = "한국";					cmp_texture = "토제";	
					cmp_use = "산업/생활";					cmp_design = "문자문";
					list = kor_era;					
					break;
				case 2:
					cmp_country = "중국";					cmp_texture = "지";
					cmp_use = "사회생활";					cmp_design = "동물문";
					list = ch_era;					
					break;
				case 3:
					cmp_country = "기타";					cmp_texture = "사직";
					cmp_use = "과학기술";					cmp_design = "산수문";
					break;
				case 4:
					cmp_texture = "나무";					cmp_use = "문화예술";
					cmp_design = "산수문";				
					break;
				case 5:
					cmp_texture = "금속";					cmp_use = "의";
					cmp_design = "기하문";
					break;
				case 6:
					cmp_texture = "도자기";				cmp_use = "식";
					cmp_design = "식물문";
					break;
				case 7:
					cmp_texture = "석";					cmp_use = "주";
					break;
				case 8:
					cmp_texture = "기타";					cmp_use = "기타";
					break;				
				}
				if(pos!=0){
					rearrange_list();
					display_list();
				}				
				enable_spinner2(list, pos);	
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		}

		setData();

		if (search_result == null) {
			// 아무변화 없음
		}else { // 입력 있이 검색버튼

			// search_result를 이용해서 검색 및 리스트 만들고 화면에 보여주기
			rearrange_list2(); //검색 결과를 이용한 리스트 만들기
			if(display_Data.isEmpty()== true){ //리스트에 아무것도 없을 경우
				Toast.makeText(getApplicationContext(), "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();				
			}
			else display_list(); //리스트 표시
			search_result = null;
		}

	}

	// 스피너에서 선택 시에 리스트를 새로 만들어줌

	public void rearrange_list() {
		display_Data = new ArrayList<exhibit_data>();
		for (int i = 0; i < Data.size(); i++) {
			if (category.equals("국가")
					&& Data.get(i).getCountry().equals(cmp_country)) {
				if (cmp_age == null)
					display_Data.add(Data.get(i));
				else if (Data.get(i).getAge().equals(cmp_age))
					display_Data.add(Data.get(i));
			} else if (category.equals("재질")
					&& Data.get(i).getTexture().equals(cmp_texture))
				display_Data.add(Data.get(i));
			else if (category.equals("용도") 
					&& Data.get(i).getUse().matches(".*"+cmp_use+".*"))
				display_Data.add(Data.get(i));
			else if (category.equals("문양")&& Data.get(i).getDesign()!=null
					&& Data.get(i).getDesign().matches(".*"+cmp_design+".*"))
				display_Data.add(Data.get(i));
			else
				;
		}
	}

	// list2 : 검색어를 이용해서 Data에서 나라,시대,재질,사용,디자인 등과 일치하는 결과를 찾아서 새로운 리스트를 만들어줌
	public void rearrange_list2() {
		display_Data = new ArrayList<exhibit_data>();

		if (search_range.equals("전체검색")) {
			for (int i = 0; i < Data.size(); i++) {

				//검색어의 내용을 일부로 포함하는 항목에 대해서 검색결과로 반영
				if ((Data.get(i).getCountry().matches(".*" + search_result
						+ ".*"))
						|| (Data.get(i).getName().matches(".*" + search_result
								+ ".*"))
						|| (Data.get(i).getAge().matches(".*" + search_result
								+ ".*"))
						|| (Data.get(i).getTexture().matches(".*"
								+ search_result + ".*"))
						|| (Data.get(i).getUse().matches(".*" + search_result
								+ ".*"))
						|| ((Data.get(i).getDesign() != null) && (Data.get(i)
								.getDesign().matches(".*" + search_result
								+ ".*")))) {
					display_Data.add(Data.get(i));

				}
			}
		}else if( search_range.equals("국가검색")){ //국가 필드만 검색
			for (int i = 0; i < Data.size(); i++) {
				if ((Data.get(i).getCountry().matches(".*" + search_result
						+ ".*"))){
					display_Data.add(Data.get(i));
				}
			}
		}else if( search_range.equals("시대검색")){
			for (int i = 0; i < Data.size(); i++) {
				if ((Data.get(i).getAge().matches(".*" + search_result
						+ ".*"))){
					display_Data.add(Data.get(i));
				}
			}
		}else if(search_range.equals("이름검색")){
			for (int i = 0; i < Data.size(); i++) {
				if ((Data.get(i).getName().matches(".*" + search_result
						+ ".*"))){
					display_Data.add(Data.get(i));
				}
			}
		}
	}

	public void display_list() {

		adapter = new Exhibit_Adapter(this, display_Data);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);


	}

	// 두번째 스피너 활성
	public void enable_spinner2(String[] list, final int selector) {	

		spinner2 = (Spinner) findViewById(R.id.spinner2);

		if (selector == 0 || !category.equals("국가") || list == null) {
			spinner2.setEnabled(false);
			return;
		} else {
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
				if(pos != 0) {
					if (selector == 1) {					
						cmp_age = kor_era[pos];				
					} 
					else if (selector == 2) {					
						cmp_age = ch_era[pos];	
					}
				}
				rearrange_list();
				display_list();
				cmp_age = null;
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void setData() {
		
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
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
