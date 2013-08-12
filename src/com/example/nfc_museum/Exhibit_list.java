package com.example.nfc_museum;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Exhibit_list extends Activity{
	
	private ImageView iv[];
	private Button tv[];
	private String name[];
	private String country[];
	private String age[];
	private String texture[];
	private String use[];
	private String location[];
	private String id[];	
	private String comment[];
	private Bitmap image[];
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStart(){
		super.onStart();	
		setContentView(R.layout.exhibit_list);
		
		iv = new ImageView[2];
		tv = new Button[2];
		name = new String[2];	
		country = new String[2];
		age = new String[2];		
		texture = new String[2];
		use = new String[2];
		location = new String[2];
		id = new String[2];
		comment = new String[2];
		image = new Bitmap[2];
		
		
		iv[0] = (ImageView) findViewById(R.id.list_img_01);
		iv[1] = (ImageView) findViewById(R.id.list_img_02);
		tv[0] = (Button) findViewById(R.id.list_name_01);
		tv[1] = (Button) findViewById(R.id.list_name_02);
		
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
			int num = 0;
			String tag = "";
			String url = "";			
			
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
						image[num] = BitmapFactory.decodeStream(bis);
						bis.close();
						is.close();	
						iv[num].setImageBitmap(image[num]);
						num++;
					}
					else if(tag.equals("name")){
						parser.next();
						name[num] = parser.getText();
						tv[num].setText(name[num]);
					}
					else if(tag.equals("comment")){
						parser.next();
						comment[num] = parser.getText(); 
					}
					else if(tag.equals("country")){
						parser.next();
						country[num] = parser.getText();
					}
					else if(tag.equals("texture")){
						parser.next();
						texture[num] = parser.getText();
					}
					else if(tag.equals("use")){
						parser.next();
						use[num] = parser.getText();						
					}
					else if(tag.equals("location")){
						parser.next();
						location[num] = parser.getText();
					}
					else if(tag.equals("id")){
						parser.next();
						id[num] = parser.getText();
					}
					else if(tag.equals("age")){
						parser.next();
						age[num] = parser.getText();
					}
					break;				
				case XmlPullParser.END_TAG:
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
		
		tv[0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(Exhibit_list.this, Exhibit_content.class);
				intent.putExtra("name",name[0]);
				intent.putExtra("country", country[0]);
				intent.putExtra("age", age[0]);
				intent.putExtra("texture", texture[0]);
				intent.putExtra("use", use[0]);
				intent.putExtra("location",location[0]);
				intent.putExtra("id",id[0]);
				intent.putExtra("image",image[0]);
				intent.putExtra("comment", comment[0]);
				startActivity(intent);
			}
		});	
		
		tv[1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(Exhibit_list.this, Exhibit_content.class);
				intent.putExtra("name",name[1]);
				intent.putExtra("country", country[1]);
				intent.putExtra("age", age[1]);
				intent.putExtra("texture", texture[1]);
				intent.putExtra("use", use[1]);
				intent.putExtra("location",location[1]);
				intent.putExtra("id",id[1]);
				intent.putExtra("image",image[1]);
				intent.putExtra("comment", comment[1]);
				startActivity(intent);
			}
		});	
		
		
	}
	

}
