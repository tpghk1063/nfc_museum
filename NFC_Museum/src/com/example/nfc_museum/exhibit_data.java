package com.example.nfc_museum;

import android.graphics.Bitmap;


public class exhibit_data {
	private Bitmap image;
	private String name;
	private String comment;
	private String country;	
	private String texture;
	private String use;
	private String location;
	private String id;
	private String age;
	private String design;
	
	public exhibit_data(Bitmap img, String name, String comm, String cntry, String txtr, String use, String loc, String id, String age, String design){
		this.image = img;
		this.name = name;
		this.comment = comm;
		this.country = cntry;
		this.texture = txtr;
		this.use = use;
		this.location = loc;
		this.id = id;
		this.age = age;
		this.design = design;
		
	}
	
	public Bitmap getImage(){
		return image;
	}
	
	public String getName(){
		return name;
	}
	
	public String getComment(){
		return comment;
	}
	
	public String getCountry(){
		return country;
	}
	
	public String getTexture(){
		return texture;
	}
	
	public String getUse(){
		return use;		
	}
	
	public String getLocation(){
		return location;
	}
	
	public String getId(){
		return id;
	}
	
	public String getDesign(){
		return design;
	}
	
	public String getAge(){
		return age;
	}
}
