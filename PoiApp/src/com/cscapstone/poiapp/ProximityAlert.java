/**
 * 
 */
package com.cscapstone.poiapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Capstone
 * Class structure from:
 * https://github.com/gauntface/Android-Proximity-Alerts-Example/blob/master/src/co/uk/gauntface/android/proximityalerts/receivers/ProximityAlert.java
 * Matthew Gaunt
 */
public class ProximityAlert extends BroadcastReceiver{

	public static final String EVENT_ID_INTENT_EXTRA = "EventIDIntentExtraKey";
	public static final String POI_NAME = "poi_name";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		long eventID = intent.getLongExtra(EVENT_ID_INTENT_EXTRA, -1);
		String poi_name = intent.getStringExtra(POI_NAME);
	}

}
