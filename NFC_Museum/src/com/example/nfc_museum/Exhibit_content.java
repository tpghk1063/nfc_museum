package com.example.nfc_museum;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Exhibit_content extends Activity{
	private ImageView iv;
	private TextView tv_name;
	private TextView tv_comment;
	private TextView tv_country;	
	private TextView tv_texture;
	private TextView tv_use;
	private TextView tv_location;
	private TextView tv_id;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		setContentView(R.layout.exhibit_content);
		
		iv = (ImageView)findViewById(R.id.img);
		tv_name = (TextView)findViewById(R.id.name);
		tv_comment = (TextView)findViewById(R.id.content);
		tv_country = (TextView)findViewById(R.id.country);		
		tv_texture = (TextView)findViewById(R.id.texture);
		tv_use = (TextView)findViewById(R.id.use);
		tv_location = (TextView)findViewById(R.id.location);
		tv_id = (TextView)findViewById(R.id.id);		
		
		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		String comment = intent.getStringExtra("comment");
		String country = intent.getStringExtra("country");
		String age = intent.getStringExtra("age");
		String texture = intent.getStringExtra("texture");
		String use = intent.getStringExtra("use");
		String location = intent.getStringExtra("location");
		String id = intent.getStringExtra("id");		
		Bitmap image = (Bitmap) intent.getExtras().get("image");
			
		
		country = country + "/" + age;
		
		iv.setImageBitmap(image);
		tv_name.setText(name);
		tv_comment.setText(comment);
		tv_country.setText(country);		
		tv_texture.setText(texture);
		tv_use.setText(use);
		tv_location.setText(location);
		tv_id.setText(id);
		
		
	}
}
