/**
 * 
 */
package com.csce.capstone.poi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

/**
 * @author Capstone
 *
 */
public class ProximityAlert extends BroadcastReceiver{

	public static final String EVENT_ID_INTENT_EXTRA = "EventIDIntentExtraKey";
	public static final String POI_NAME = "poi_name";
	//debugging purposes
	private static final String TAG = "Proximity Alert";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		long eventID = intent.getLongExtra(EVENT_ID_INTENT_EXTRA, -1);
		String poi_name = intent.getStringExtra(POI_NAME);
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		
		Boolean entering = intent.getBooleanExtra(key, false);
		if(entering){
			
			Intent factIntent = new Intent(context, AndroidUIFactActivity.class);
			factIntent.putExtra(POI_NAME, poi_name);
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			
			stackBuilder.addParentStack(AndroidUIFactActivity.class);
			PendingIntent factPendingIntent = stackBuilder.getPendingIntent(0,
					PendingIntent.FLAG_UPDATE_CURRENT);
			
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			builder.setContentIntent(factPendingIntent);
			
			//notification fire
			NotificationManager mNotification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			int notifyId = 1;
			builder.setContentTitle("Location Found!");
			builder.setContentText("You've found " + poi_name);
			//builder.setSmallIcon();
			//Create new Activity to get the facts from the server and send them to 
			//the infoactivity
		}
		Log.i(TAG, "Alert Recieved!");
	}
}
