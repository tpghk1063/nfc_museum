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
	private String[] kor_era = { "�ô뼱��", "û����", "����", "����", "�Ŷ�", "���ϽŶ�","���", "����", "��������", "��������", "��������", "�ô�̻�" };
	private String[] ch_era = { "�ô뼱��", "������", "û", "�ô�̻�" };

	private Spinner spinner2 = null;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exhibit_list);

		String[] category_selector = null;
		String[] nation = { "��������", "�ѱ�", "�߱�", "��Ÿ" };
		String[] texture = { "��������","����", "��", "����","����","�ݼ�","���ڱ�","��","��Ÿ"};
		String[] use = {"�뵵����","���/��Ȱ","��ȸ��Ȱ","���б��","��ȭ����","��","��","��","��Ÿ"};
		String[] design = {"���缱��","���ڹ�","������","�����","���Ϲ�","�Ĺ���"};
		
		
		Intent intent = getIntent();
		search_result = intent.getStringExtra("SEARCH_TEXT");
		search_range = intent.getStringExtra("RANGE_TEXT");
		category = intent.getStringExtra("CATEGORY");		

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter;
		
		if(category!=null){
			if(category.equals("����")) category_selector = texture;
			else if(category.equals("�뵵")) category_selector = use;
			else if(category.equals("����")) category_selector = design;
			else category_selector = nation;
		
		
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, category_selector); // ����Ϳ� ����Ʈ ����
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ����͸�
																						// ��Ӵٿ�
																						// �信
																						// �޾���

		spinner.setAdapter(adapter); // ����͸� ���ǳʿ� ����
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
				
				String[] list = null;
				
				switch(pos){
				case 0:					
					break;					
				case 1:
					cmp_country = "�ѱ�";					cmp_texture = "����";	
					cmp_use = "���/��Ȱ";					cmp_design = "���ڹ�";
					list = kor_era;					
					break;
				case 2:
					cmp_country = "�߱�";					cmp_texture = "��";
					cmp_use = "��ȸ��Ȱ";					cmp_design = "������";
					list = ch_era;					
					break;
				case 3:
					cmp_country = "��Ÿ";					cmp_texture = "����";
					cmp_use = "���б��";					cmp_design = "�����";
					break;
				case 4:
					cmp_texture = "����";					cmp_use = "��ȭ����";
					cmp_design = "�����";				
					break;
				case 5:
					cmp_texture = "�ݼ�";					cmp_use = "��";
					cmp_design = "���Ϲ�";
					break;
				case 6:
					cmp_texture = "���ڱ�";				cmp_use = "��";
					cmp_design = "�Ĺ���";
					break;
				case 7:
					cmp_texture = "��";					cmp_use = "��";
					break;
				case 8:
					cmp_texture = "��Ÿ";					cmp_use = "��Ÿ";
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
			// �ƹ���ȭ ����
		}else { // �Է� ���� �˻���ư

			// search_result�� �̿��ؼ� �˻� �� ����Ʈ ����� ȭ�鿡 �����ֱ�
			rearrange_list2(); //�˻� ����� �̿��� ����Ʈ �����
			if(display_Data.isEmpty()== true){ //����Ʈ�� �ƹ��͵� ���� ���
				Toast.makeText(getApplicationContext(), "�˻������ �����ϴ�.", Toast.LENGTH_SHORT).show();				
			}
			else display_list(); //����Ʈ ǥ��
			search_result = null;
		}

	}

	// ���ǳʿ��� ���� �ÿ� ����Ʈ�� ���� �������

	public void rearrange_list() {
		display_Data = new ArrayList<exhibit_data>();
		for (int i = 0; i < Data.size(); i++) {
			if (category.equals("����")
					&& Data.get(i).getCountry().equals(cmp_country)) {
				if (cmp_age == null)
					display_Data.add(Data.get(i));
				else if (Data.get(i).getAge().equals(cmp_age))
					display_Data.add(Data.get(i));
			} else if (category.equals("����")
					&& Data.get(i).getTexture().equals(cmp_texture))
				display_Data.add(Data.get(i));
			else if (category.equals("�뵵") 
					&& Data.get(i).getUse().matches(".*"+cmp_use+".*"))
				display_Data.add(Data.get(i));
			else if (category.equals("����")&& Data.get(i).getDesign()!=null
					&& Data.get(i).getDesign().matches(".*"+cmp_design+".*"))
				display_Data.add(Data.get(i));
			else
				;
		}
	}

	// list2 : �˻�� �̿��ؼ� Data���� ����,�ô�,����,���,������ ��� ��ġ�ϴ� ����� ã�Ƽ� ���ο� ����Ʈ�� �������
	public void rearrange_list2() {
		display_Data = new ArrayList<exhibit_data>();

		if (search_range.equals("��ü�˻�")) {
			for (int i = 0; i < Data.size(); i++) {

				//�˻����� ������ �Ϻη� �����ϴ� �׸� ���ؼ� �˻������ �ݿ�
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
		}else if( search_range.equals("�����˻�")){ //���� �ʵ常 �˻�
			for (int i = 0; i < Data.size(); i++) {
				if ((Data.get(i).getCountry().matches(".*" + search_result
						+ ".*"))){
					display_Data.add(Data.get(i));
				}
			}
		}else if( search_range.equals("�ô�˻�")){
			for (int i = 0; i < Data.size(); i++) {
				if ((Data.get(i).getAge().matches(".*" + search_result
						+ ".*"))){
					display_Data.add(Data.get(i));
				}
			}
		}else if(search_range.equals("�̸��˻�")){
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

	// �ι�° ���ǳ� Ȱ��
	public void enable_spinner2(String[] list, final int selector) {	

		spinner2 = (Spinner) findViewById(R.id.spinner2);

		if (selector == 0 || !category.equals("����") || list == null) {
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

								// �̹��� ��������
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
