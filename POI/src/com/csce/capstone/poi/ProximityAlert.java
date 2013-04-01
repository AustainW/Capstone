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
	public static final String POI_ID = "poi_id";
	//debugging purposes
	private static final String TAG = "Proximity Alert";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		long eventID = intent.getLongExtra(EVENT_ID_INTENT_EXTRA, -1);
		String poi_name = intent.getStringExtra(POI_NAME);
		int poi_id = intent.getIntExtra("ID", -1);
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		
		Boolean entering = intent.getBooleanExtra(key, false);
		if(entering){
			
			Intent factIntent = new Intent(context, AndroidUIFactActivity.class);
			factIntent.putExtra(POI_NAME, poi_name);
			factIntent.putExtra(POI_ID, poi_id);
			factIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			
			PendingIntent factPendingIntent = PendingIntent.getActivity(context, 0, factIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			builder.setContentIntent(factPendingIntent);
			
			//notification fire
			NotificationManager mNotification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			int notifyId = 1;
			builder.setContentTitle("Location Found!");
			builder.setContentText("You've found " + poi_name + ". Would you like to view " +
					"information about " + poi_name + "?");
			builder.setAutoCancel(true);
			builder.setSmallIcon(R.drawable.ic_notification);
			//Create new Activity to get the facts from the server and send them to 
			//the infoactivity
			mNotification.notify(0, builder.build());
		}
		Log.i(TAG, "Alert Recieved!");
	}
}
