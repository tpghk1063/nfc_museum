package com.example.nfc_museum;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import edu.stanford.junction.SwitchboardConfig;
import edu.stanford.junction.android.AndroidJunctionMaker;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class NfcStart extends Activity {
	
	private static final int REQUEST_CODE = 1001;
	private LinearLayout lL;
	private exhibit_data Data = null;
	NfcAdapter mNfcAdapter;
    PendingIntent mNfcPendingIntent;
    	
	private JunctionActor receiver;
	private String Host = "163.239.24.212";
	private String Session = "exhibition";
	private boolean Session_Connected = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);		
		lL = (LinearLayout) findViewById(R.id.StartLayout);	
	
		lL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),MainActivity.class);			
				startActivityForResult(intent,REQUEST_CODE);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		// Check to see that the Activity started due to an Android Beam
		
		if(Session_Connected==false){
			try {			
				SwitchboardConfig config = new XMPPSwitchboardConfig("163.239.24.212");	
				URI uri = new URI("junction://"+Host+"/"+Session);
				receiver = new Receiver();
				AndroidJunctionMaker maker = AndroidJunctionMaker.getInstance(config);			
				maker.newJunction(uri, receiver);
				Session_Connected = true;			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {	
			Intent intent = new Intent(getBaseContext(), Exhibit_content.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

			Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(
					NfcAdapter.EXTRA_NDEF_MESSAGES);
			// only one message sent during the beam
			NdefMessage msg = (NdefMessage) rawMsgs[0];
			// record 0 contains the MIME type, record 1 is the AAR, if present
			String s = new String(msg.getRecords()[0].getPayload());			
			
			findData(s);
				
			JSONObject message = new JSONObject();
			try {
				message.put("id", Data.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
				
			receiver.sendMessageToSession(message);					
											
		}
	}
   
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE) {
			this.finish();
		}
	}
   
	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		setIntent(intent);
	}
	
	public void findData(final String s){
		
				
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
					Boolean flag = false;

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
								if(id.equals(s))
									flag = true;
								else
									flag = false;
							} else if (tag.equals("age")) {
								parser.next();
								age = parser.getText();
							} else if (tag.equals("design")) {
								parser.next();
								design = parser.getText();
							}
							break;
						case XmlPullParser.END_TAG:
							if (tag.equals("data") && flag == true){
								Data = new exhibit_data(image, name, comment, country, texture, use, location, id, age, design);		
								
							}
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
	
 class Receiver extends JunctionActor {
		public Receiver() {
			super("receiver");
		}

		
		@Override
		public void onMessageReceived(MessageHeader header, JSONObject inbound) {
			System.out.println(this + " got: " + inbound);
			Intent intent = new Intent(getBaseContext(), Exhibit_content.class);
			try {
				findData(inbound.getString("id").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			intent.putExtra("name", Data.getName());
			intent.putExtra("country", Data.getCountry());
			intent.putExtra("age", Data.getAge());
			intent.putExtra("texture", Data.getTexture());
			intent.putExtra("use", Data.getUse());
			intent.putExtra("location", Data.getLocation());
			intent.putExtra("id", Data.getId());
			intent.putExtra("image",Data.getImage());
			intent.putExtra("comment", Data.getComment());						
			startActivityForResult(intent,REQUEST_CODE);
			
		}
	}
}