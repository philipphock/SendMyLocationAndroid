package de.philipphock.android.sendmylocation;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SendLocation extends Activity {

	private Location loc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_location);
		
		if (getIntent().getExtras()==null)return;
		//if (getIntent().getExtras().getParcelable("location")==null)return;
		loc = getIntent().getExtras().getParcelable("location");
		TextView v = (TextView) findViewById(R.id.locationText);
		v.setText(getLocationString());
	}

	private String getLocationString(){
		StringBuilder v = new StringBuilder();
		v.append("Ich bin gerade hier: ");
		v.append("\n");
		v.append("lat: ");
		v.append(loc.getLatitude()+"");
		v.append("\n");
		
		v.append("long: ");
		v.append(loc.getLongitude()+"");
		v.append("\n");
		
		v.append("alt: ");
		v.append(loc.getAltitude()+"");
		v.append("\n");
		
		v.append("http://maps.google.com/maps?q="+loc.getLatitude()+","+loc.getLongitude());
		
		return v.toString();
	}
	
	
	public void snd_out(View v){
		TextView tv = (TextView) findViewById(R.id.locationText);
		String body = tv.getText().toString();
		
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, body);
		sendIntent.setType("text/plain");
		startActivity(sendIntent);

	}
	public void snd_gmaps(View v){
		String label = "my location";
		String uriBegin = "geo:" + loc.getLatitude() + "," + loc.getLongitude();
		String query = loc.getLatitude() + "," + loc.getLongitude() + "(" + label + ")";
		String encodedQuery = Uri.encode(query);
		String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}	

}
