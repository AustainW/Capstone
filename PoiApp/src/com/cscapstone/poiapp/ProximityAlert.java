/**
 * 
 */
package com.cscapstone.poiapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

/**
 * @author Capstone
 * Class structure from:
 * https://github.com/gauntface/Android-Proximity-Alerts-Example/blob/master/src/co/uk/gauntface/android/proximityalerts/receivers/ProximityAlert.java
 * Matthew Gaunt
 */
public class ProximityAlert extends BroadcastReceiver{

	public static final String EVENT_ID_INTENT_EXTRA = "EventIDIntentExtraKey";
	public static final String POI_NAME = "poi_name";
	//debugging purposes
	private static final String TAG = "LocationModel";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		long eventID = intent.getLongExtra(EVENT_ID_INTENT_EXTRA, -1);
		String poi_name = intent.getStringExtra(POI_NAME);
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		
		Boolean entering = intent.getBooleanExtra(key, false);
		if(entering){
			
			Intent factIntent = new Intent();
			
			//notification fire
			NotificationManager mNotification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			//Create new Activity to get the facts from the server and send them to 
			//the infoactivity
			
			
		}
		Log.i(TAG, "Alert Recieved!");
	}

}
