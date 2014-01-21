package de.philipphock.android.sendmylocation;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class TouchMapView extends MapView {
	
	private ItemizedOverlay<OverlayItem> overlays;
	private ArrayList<OverlayItem> itemList;
	private GeoPoint gpsMarkerLocation=null;
	
	private int lastEventId = -1;	
	private int radius = 50;
	
	private OnTapListener tapListener;
	public void setOnTapListener(OnTapListener l){
		tapListener=l; 
	}
	public TouchMapView(Context context, int tileSizePixels) {
		super(context, tileSizePixels);

	}
	
	public TouchMapView(Context c, AttributeSet a){
		super(c,a);
	}
	
	public void setRadius(int radiusInMeter){
		radius=radiusInMeter;
		invalidate();
	}
	
	public int getRadius(){
		return radius;
	}

	public GeoPoint getGeoPoint(){
		return gpsMarkerLocation;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == 1 && lastEventId == 0 && event.getPointerCount()==1){
			
			tap((int)event.getX(),(int)event.getY(),event);
		}
		lastEventId = event.getAction();
		return super.onTouchEvent(event);
	}
	
	private void tap(int x, int y,MotionEvent e){
		IGeoPoint p = TouchMapView.this.getProjection().fromPixels((int) x,
				(int) y);

		
		GeoPoint pnt = new GeoPoint(p.getLatitudeE6(),p.getLongitudeE6());
		
		placeMarker(pnt);
		
		if (tapListener != null){
			tapListener.onTab(e, x, y);
		}
		
		
	}
	
	public void placeMarker(GeoPoint pnt){
		gpsMarkerLocation = pnt;
		OverlayItem markerHere = new OverlayItem("location", "", pnt);
		
		getOverlays().clear();
		
		itemList = new ArrayList<OverlayItem>();
		itemList.add(markerHere);
		overlays = new RadiusItemizedOverlay(getContext(),itemList);
		getOverlays().add(overlays);
		
		//itemList.add(touchHere);
		this.invalidate();
	}
		
	
	private class RadiusItemizedOverlay extends ItemizedIconOverlay<OverlayItem>{

		public RadiusItemizedOverlay(Context pContext,List<OverlayItem> pList) {
			super(pContext,pList,new OnItemGestureListener<OverlayItem>() {

				@Override
				public boolean onItemLongPress(int arg0, OverlayItem arg1) {
					Log.d("touch","whyyyyyyyyyyyyyy");

					return false;
				}

				@Override
				public boolean onItemSingleTapUp(int arg0, OverlayItem arg1) {
					Log.d("touch","ääääääääääääää");

					return false;
				}
			});
			
		}
		
		@Override
		protected void draw(Canvas arg0, MapView arg1, boolean arg2) {
			if (itemList.size()>0 && gpsMarkerLocation != null){
				float projectionMeter=getProjection().metersToEquatorPixels(radius);
				
				Point pnt = new Point();
				getProjection().toPixels(gpsMarkerLocation,pnt);
				arg0.drawCircle(pnt.x, pnt.y, projectionMeter, getInnerPaint());
			}
			super.draw(arg0, arg1, arg2);

			
			
		}
		
		

		private Paint innerPaint;

		private Paint getInnerPaint() {
		    if (innerPaint == null) {
		        innerPaint = new Paint();
		        innerPaint.setARGB(100, 0, 255, 0); // gray
		        innerPaint.setAntiAlias(true);
		    }
		    return innerPaint;
		}

		

	}
	
	public interface OnTapListener{
		public void onTab(MotionEvent e,int x, int y);
		
	}

}