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

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Exhibit_list extends Activity{
	
	private ImageView iv;
	private Button bt;	
	private ListView list;
	private Exhibit_Adapter adapter;
	private ArrayList<exhibit_data> Data;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		
	}
	
	@Override
	public void onStart(){
		super.onStart();	
		setContentView(R.layout.exhibit_list);
		
		
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
			
		Intent getIntent = getIntent();
		String cmp_country = getIntent.getStringExtra("country");
		String cmp_texture = getIntent.getStringExtra("texture");
		String cmp_use = getIntent.getStringExtra("use");
		String cmp_design = getIntent.getStringExtra("design");
		
		setData(cmp_country,cmp_texture,cmp_use,cmp_design);
		
		adapter = new Exhibit_Adapter(this, Data);
		list = (ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
	}
	
	
	public void setData(final String cmp_country,final String cmp_texture,final String cmp_use,final String cmp_design){

		Data = new ArrayList<exhibit_data>();
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				
		try{
			HttpClient client = new DefaultHttpClient();
			
			HttpGet method = new HttpGet("http://163.239.24.212:8000/index.php");
			
			HttpResponse response = client.execute(method);
			InputStream is = response.getEntity().getContent();

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
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
			boolean flag = false;
			
			while(status != XmlPullParser.END_DOCUMENT){
				tag = parser.getName();	
								
				switch(status){
				case XmlPullParser.START_TAG:
					if(tag.equals("image")){
						parser.next();
						url = parser.getText();									
							
						//이미지 가져오기
						URL address = new URL("http://163.239.24.212:8000/img/"+url);
						HttpURLConnection conn = (HttpURLConnection)address.openConnection();
						conn.setDoInput(true);
						conn.connect();
						InputStream input_img = conn.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(input_img);
						image = BitmapFactory.decodeStream(bis);
						bis.close();
						is.close();				
						
					}
					else if(tag.equals("name")){
						parser.next();
						name = parser.getText();
					}
					else if(tag.equals("comment")){
						parser.next();
						comment = parser.getText(); 
					}
					else if(tag.equals("country")){
						parser.next();
						country = parser.getText();		
						
					}
					else if(tag.equals("texture")){
						parser.next();
						texture = parser.getText();
					}
					else if(tag.equals("use")){
						parser.next();
						use = parser.getText();						
					}
					else if(tag.equals("location")){
						parser.next();
						location = parser.getText();
					}
					else if(tag.equals("id")){
						parser.next();
						id = parser.getText();
					}
					else if(tag.equals("age")){
						parser.next();
						age = parser.getText();
					}
					else if(tag.equals("design")){
						parser.next();
						design = parser.getText();
					}
					break;				
				case XmlPullParser.END_TAG:
					if(tag.equals("data"))
						Data.add(new exhibit_data(image, name,comment, country, texture, use, location, id));					
					break;
				}
				parser.next();
				status = parser.getEventType();
			}
			
			
			
						
		}
		catch (Exception e){	
			e.printStackTrace();
		}	
			}
		});

		thread.start();		
	}

}
