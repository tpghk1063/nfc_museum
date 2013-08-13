package com.example.nfc_museum;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;


public class Exhibit_Adapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<exhibit_data> Data;
	private LayoutInflater inflater;
	
	public Exhibit_Adapter(Context c, ArrayList<exhibit_data> d){
		this.context = c;
		this.Data = d;
		inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount(){
		return Data.size();
	}
	
	public View getView(final int position, View convertView, ViewGroup parent){
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.list, parent, false);
		}
		
		ImageView image = (ImageView) convertView.findViewById(R.id.list_img);
		Button button = (Button) convertView.findViewById(R.id.list_name);
		
		image.setImageBitmap(Data.get(position).getImage());		
		button.setText(Data.get(position).getName());
		button.setOnClickListener(new OnClickListener(){
			public void onClick(View v){				
				Intent intent = new Intent(v.getContext(),Exhibit_content.class);
				intent.putExtra("name", Data.get(position).getName());
				intent.putExtra("country", Data.get(position).getCountry());
				intent.putExtra("age", Data.get(position).getAge());
				intent.putExtra("texture", Data.get(position).getTexture());
				intent.putExtra("use", Data.get(position).getUse());
				intent.putExtra("location", Data.get(position).getLocation());
				intent.putExtra("id", Data.get(position).getId());
				intent.putExtra("image",Data.get(position).getImage());
				intent.putExtra("comment", Data.get(position).getComment());
				context.startActivity(intent);
			}
		});		
		
		return convertView;		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
