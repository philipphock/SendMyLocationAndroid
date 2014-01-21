package de.philipphock.android.sendmylocation;


import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import de.philipphock.android.sendmylocation.TouchMapView.OnTapListener;

public class MainActivity extends Activity  implements OnTapListener{

	private LocationManager locManager;
	private LocationListener locListener;
	private String latitude;
	private String longitude;
	private TouchMapView mapView;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mapView = (TouchMapView)findViewById(R.id.map);
		mapView.setClickable(true);
		mapView.setTileSource(TileSourceFactory.CYCLEMAP);
		mapView.setBuiltInZoomControls(true);
		
		mapView.setOnTapListener(this);
		
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		
		
		
		mapView.setMapListener(new MapListener() {
			
			@Override
			public boolean onZoom(ZoomEvent arg0) {
				Rect r = mapView.getProjection().getScreenRect();
				IGeoPoint nullP = mapView.getProjection().fromPixels(0, 0);
				IGeoPoint endP =  mapView.getProjection().fromPixels(r.width(),r.height());
				float[] result = new float[3];
				Location.distanceBetween(nullP.getLatitude(), nullP.getLongitude(), endP.getLatitude(), endP.getLongitude(), result);
				
				if (result[0] == 0.0f) return false;
				return false;
			}
			
			@Override
			public boolean onScroll(ScrollEvent arg0) {
				return false;
			}
		});
		
		
		mapView.getController().setZoom(18);

		locListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}

			@Override
			public void onLocationChanged(Location location) {
				location.getLatitude();
				location.getLongitude();

				latitude = String.valueOf(location.getLatitude());
				longitude = String.valueOf(location.getLongitude());

				double newLat = Double.parseDouble(latitude);
				double newLon = Double.parseDouble(longitude);
				GeoPoint here = new GeoPoint(newLat, newLon);
				mapView.getController().setCenter(here);
				mapView.placeMarker(here);
				mapView.setRadius((int)location.getAccuracy());
				
				
			}
		};

		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				4000, 0, locListener);
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				4000, 0, locListener);


	}
	
	public void disableLocationUpdate(){
		locManager.removeUpdates(locListener);

	}

	public void onOkBtnClicked(View v){
		//Toast.makeText(this, mapView.getGeoPoint().toString()+ " "+mapView.getRadius(), Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this,SendLocation.class);
		Location l = new Location("");
		l.setLatitude(mapView.getGeoPoint().getLatitude());
		l.setLongitude(mapView.getGeoPoint().getLongitude());
		l.setAltitude(mapView.getGeoPoint().getAltitude());
		i.putExtra("location", l);
		startActivity(i);
	}
	
	
	
	/**
	 * @interface OnTapListener 
	 */
	@Override
	public void onTab(MotionEvent e, int x, int y) {
		mapView.setRadius(0);
		disableLocationUpdate();
	}
	
	
}