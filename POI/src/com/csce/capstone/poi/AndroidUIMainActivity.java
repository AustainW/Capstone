package com.csce.capstone.poi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AndroidUIMainActivity extends Activity implements OnTaskCompleted {

	private static final String TAG_ID = "ID";
	private static final String TAG_NAME = "Name";
	private static final String TAG_TYPE = "Type";
	private static final String TAG_LOCATION = "Location";
	private static final String TAG_LOCATIONS = "Locations";
	private static final String TAG_LONGITUDE = "Long";
	private static final String TAG_LATITUDE = "Lat";
	

	public static final String TAG_GEOPOINTS = "GeoPairs";
	
	private static final int DELTA_MINUTES = 1000 * 60 * 5;
	
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
	
	private TextView loading;
	private Button allLocations;
	
	
	private static final String PROXIMITY_INTENT_ACTION = new String("com.csce.capstone.poi.action.PROXIMITY_ALERT");
	private static final String POI_NAME = "poi_name";
	private static final String POI_ID = "poi_id";
	private LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_uimain);
		this.setTitle("PLU Points Of Interest");
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATE,
				MINIMUM_DISTANCECHANGE_FOR_UPDATE, 
				new CustomLocationListener());
		
		loading = (TextView) findViewById(R.id.loading_text);
		allLocations = (Button) findViewById(R.id.all_locations_button);
		allLocations.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), AndroidUILocationsListActivity.class);
				startActivity(i);
			}
		});
		
		DownloadTask dTask = new DownloadTask(this);
		dTask.execute();
		
//		POI b1 = new Point("TestOne", 1, 47.14567, -122.44678, "Point");
//		POI b2 = new Point("TestTwo", 2, 47.14678, -122.44044, "Point");
//		ArrayList<GeoPair> points = new ArrayList<GeoPair>();
//		GeoPair g1 = new GeoPair(47.14123, -122.44123);
//		GeoPair g2 = new GeoPair(47.14345, -122.44634);
//		GeoPair g3 = new GeoPair(47.14098, -122.44087);
//		points.add(g1);
//		points.add(g2);
//		points.add(g3);
//		POI b3 = new Building("TestThree", 3, points, "Bld");
//		
//		ArrayList<POI> temp = new ArrayList<POI>();
//		temp.add(b1);
//		temp.add(b2);
//		temp.add(b3);
//		setProximityAlerts(temp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.android_uimain, menu);
		return true;
	}
	
	@Override
	public void onTaskCompleted(ArrayList data) {
		setProximityAlerts(data);
		
	}
	
	public void setProximityAlerts(ArrayList locationsArray){
		GeoPair LatLongPair = new GeoPair();
		ArrayList<GeoPair> points = new ArrayList<GeoPair>();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		for(int i = 0; i < locationsArray.size(); i++){
			POI poi = (POI) locationsArray.get(i);
			points.addAll(poi.getPointsArray());
			for(int p = 0; p < poi.getPointsListSize(); p++){
				names.add(poi.getName());
				ids.add(poi.getId());
			}
			
		}
		for(int j = 0; j < points.size(); j++){
			LatLongPair.setLatitude(points.get(j).getLatitude());
			LatLongPair.setLongitude(points.get(j).getLongitude());
			String name = names.get(j);
			int id = ids.get(j);
			setProximityAlert(LatLongPair.getLatitude(), LatLongPair.getLongitude(), name, id, j+1, j);
		}
		
		loading.setText("Points Loaded. Go explore the Campus!");
		//loop through each of the POIs
		//create a proximity alert with the lat/long of the current POI
		//MAKE SURE EACH ALERT HAS A UNIQUE ID
		//make a receiver.
	}
	
	private void setProximityAlert(double lat, double lon, String name, int id, final long eventID, int requestCode){
		//Radius of 1 meter
		int radius = 1;
		//10 minute expiration time (10mins*60sec*1000milliseconds)
		//long expiration = 600000;
		//no expiration time
		long expiration = -1;
		
		
		
		Intent intent = new Intent(PROXIMITY_INTENT_ACTION);
		intent.putExtra(POI_NAME, name);
		intent.putExtra(POI_ID, id);
		intent.putExtra(ProximityAlert.EVENT_ID_INTENT_EXTRA, eventID);
		PendingIntent pendingIntent = 
				PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		locationManager.addProximityAlert(lat, lon, radius, expiration, pendingIntent);
	
	}
	
	
	private class DownloadTask extends AsyncTask<String, Integer, ArrayList>{
		private OnTaskCompleted listener;
		
		public DownloadTask(OnTaskCompleted listener){
			this.listener = listener;
		}
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			loading.setText("Points are being loaded...");
		}
		
		@Override
		protected ArrayList doInBackground(String... params) {
			ArrayList data = null;
			try{
				data = getCoordinates();
				return data;
			}
			catch(Exception e){
				Log.d("Background Task", e.toString());
				//data.add("Connection error");
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(ArrayList Result){
			listener.onTaskCompleted(Result);
//			loading.setText("Points failed to load.");
			
			
		}
		
		/**
		 * Method to get all of the Points of Interest from the server
		 * @return A list of all the Points of Interest
		 */
		private ArrayList getCoordinates() {
			BufferedReader rd = null;
			StringBuilder sb = null;
			String line = null;
			ArrayList<String> data = new ArrayList<String>();
			
			try{
				URL url = new URL(getString(R.string.poi_url));
				// Creating an http connection to communicate with url 
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				// Allow inputs 
				urlConnection.setDoInput(true);
				// Allow outputs
				urlConnection.setDoOutput(true);
				
				urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
				urlConnection.setRequestProperty("Accept","*/*");
				// Set Request Type GET
				urlConnection.setRequestMethod("GET");
				// Connecting to url 
				urlConnection.connect();
				// Sending request to server
				DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
				dos.writeBytes("cmd=GetLoc");
				dos.flush();
				
				//Create the Readers for the InputStream
				rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				sb = new StringBuilder();
				
				//Add each line to a String Builder
				while((line = rd.readLine()) != null){
					//data.add(line);
					sb.append(line);
				}
				
				
				//Close the connection
				urlConnection.disconnect();
				//Parse the returned JSON
				data = parseJSON(sb.toString());
			}
			catch(Exception e){
				Log.d("Exception while downloading the locations info from url", e.toString());
				e.printStackTrace();
				return null;
			}
			return data;
			
		}
		
		/**
		 * Method to parse JSON returned from the server
		 * @param json A string of JSON Arrays and Objects 
		 * @param parseType Where the request to parse came from.
		 * @return An ArrayList of the data that was to be parsed.
		 */
		public ArrayList parseJSON(String json){
			JSONArray jsonObj;
			try {
				//Create a new JSON Object of the input
				//jsonObj = new JSONObject(json);
				jsonObj = new JSONArray(json);
				//create an arraylist to hold the locations
				ArrayList<POI> locations = new ArrayList<POI>();
				//Create a JOSN array of the data. Each of the locations should be contained in it.
				//JSONArray l = jsonObj.getJSONArray(TAG_LOCATIONS);
				for(int i = 0; i < jsonObj.length(); i++){
					//Get the next JSON object
					JSONObject o = jsonObj.getJSONObject(i);
					//Put data into Strings
					String name = o.getString(TAG_NAME);
					int id = o.getInt(TAG_ID);//I think this is a number of some sort
					String type = o.getString(TAG_TYPE);
					if(type.equalsIgnoreCase("Bld")){
						JSONArray obj = o.getJSONArray(TAG_GEOPOINTS);
							
						ArrayList<GeoPair> listCoords = new ArrayList<GeoPair>();
						for(int j = 0; j < obj.length(); j++){
							JSONObject pointObj = obj.getJSONObject(j);
							double longi = pointObj.getDouble(TAG_LONGITUDE);
							double lati = pointObj.getDouble(TAG_LATITUDE);
							GeoPair geo = new GeoPair(lati, longi);
							listCoords.add(geo);
						}
						//Create a new Point of Interest Building object
						POI poi = new Building(name, id,
							listCoords, type);
						//Add the object to the arraylist
						locations.add(poi);
							
					}
					else if(type.equalsIgnoreCase("Area")){
						JSONArray obj = o.getJSONArray(TAG_GEOPOINTS);
						//Implement GeoPair class.
						ArrayList<GeoPair> listCoords = new ArrayList<GeoPair>();
						for(int j = 0; j < obj.length(); j++){
							JSONObject pointObj = obj.getJSONObject(j);
							double longi = pointObj.getDouble(TAG_LONGITUDE);
							double lati = pointObj.getDouble(TAG_LATITUDE);
							GeoPair geo = new GeoPair(lati, longi);
							listCoords.add(geo);
						}
						//Create a new Point of Interest Building object
						POI poi = new Area(name, id,
							listCoords, type);
						//Add the object to the arraylist
						locations.add(poi);
					}
					else if(type.equalsIgnoreCase("Point")){
						JSONArray obj = o.getJSONArray(TAG_GEOPOINTS);
						//Implement GeoPair class.
						ArrayList<GeoPair> listCoords = new ArrayList<GeoPair>();
						double longi = 0, lati = 0;
						for(int j = 0; j < obj.length(); j++){
							JSONObject pointObj = obj.getJSONObject(j);
							longi = pointObj.getDouble(TAG_LONGITUDE);
							lati = pointObj.getDouble(TAG_LATITUDE);
							
							
						}
						//Create a new Point of Interest object
						POI poi = new Point(name, id,
								lati, longi, type);
						//Add the object to the arraylist
						locations.add(poi);
					}
						
				}
				return locations;
			
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing JSON");
				e.printStackTrace();
				return null;
			}
		}
	
	}

	//Might not be needed if Proximity Alerts work correctly.
		public class CustomLocationListener implements LocationListener{

			private static final String TAG = "";
			@Override
			public void onLocationChanged(Location currentLocation) {
				Location bestLocation = getBestLocation(currentLocation);
				//search locations comparing lat/long coordinates
				//if found fire off a notification loaded with a 
			}
			@Override
			public void onProviderDisabled(String arg0) {
			}
			@Override
			public void onProviderEnabled(String arg0) {
			}
			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}
			private Location getBestLocation(Location currentLocation){
				long minTime = new Date().getTime() - DELTA_MINUTES; // The last 5
				// minutes
				Location bestResult = null;
				long bestTime = currentLocation.getTime();
				float bestAccuracy = currentLocation.getAccuracy();

				List<String> matchingProviders = locationManager.getAllProviders();
				for (String provider : matchingProviders) {
					Location location = locationManager.getLastKnownLocation(provider);
					if (location != null) {
						float accuracy = location.getAccuracy();
						long time = location.getTime();
						Log.i(TAG, "TIME= " + time + ", minTime= " + minTime + ", bestTime= " + bestTime + ", accuracy= "
								+ accuracy + ", bestAccuracy= " + bestAccuracy);
						if ((time > minTime && accuracy < bestAccuracy)) {
							bestResult = location;
							bestAccuracy = accuracy;
							bestTime = time;
						} else if (time < minTime && bestAccuracy == currentLocation.getAccuracy() && time > bestTime) {
							bestResult = location;
							bestTime = time;
						}
					}
				}
				return bestResult;
			}
		}

		
	
	
}
