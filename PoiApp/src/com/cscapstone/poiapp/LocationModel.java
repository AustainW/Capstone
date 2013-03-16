/**
 * 
 */
package com.cscapstone.poiapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.json.*;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Austin
 *
 */
public class LocationModel{

	private static final int DELTA_MINUTES = 1000 * 60 * 5;
	
	private static final String TAG = "LocationModel";
	private LocationManager locationManager;
	
	private ArrayList<PointOfInterest> locationsList;
	private Map<GeoPair, POI> locationsMap;
	private Context appContext;
	private boolean notificationCheck;
	private String poi_url;
	
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds

	
	private static final String TAG_ID = "ID";
	private static final String TAG_NAME = "Name";
	private static final String TAG_TYPE = "Type";
	private static final String TAG_LOCATION = "Location";
	private static final String TAG_LOCATIONS = "Locations";
	private static final String TAG_LONGITUDE = "Longitude";
	private static final String TAG_LATITUDE = "Latitude";
	private static final String TAG_FACT = "Fact";
	private static final String TAG_FACTS = "Facts";
	private static final String TAG_TEXT = "Text";

	public static final String TAG_GEOPOINTS = "Geopairs";
	
	public static final String PROXIMITY_INTENT_ACTION = new String("com.cscapstone.poiapp.action.PROXIMITY_ALERT");
	public static final String POI_NAME = "poi_name";
	
	public LocationModel(Context applicationContext, boolean checked, String url){
		this.appContext = applicationContext;
		this.notificationCheck = checked;
		this.poi_url = url;
		
		locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATE,
				MINIMUM_DISTANCECHANGE_FOR_UPDATE,
				new CustomLocationListener()
				);
				
//		DownloadTask dtask = new DownloadTask();
//		dtask.setModel(this);
//		dtask.execute("GetCoordinates");
		
		GeoPair temp1 = new GeoPair();
		temp1.setLatitude(47.14567);
		temp1.setLongitude(-122.44678);
		POI b1 = new PointOfInterest("TestOne", 1, 47.14567, -122.44678, "PointOfInterest");
		ArrayList<POI> temp = new ArrayList<POI>();
		temp.add(b1);
		
		
		createProximityAlerts(temp);
		
	}
	
	
	
	private void setDataForView(ArrayList data){
		if(data.get(0) == "Connection error"){
			Toast.makeText(appContext, "Connection Error", Toast.LENGTH_SHORT).show();
		}
		else{
			System.out.println("Creating new Intent");
			Intent i = new Intent(appContext, InfoActivity.class);
			i.putStringArrayListExtra("DataArray", data);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			appContext.startActivity(i);
		}
	}
	
	private void addCoordinatesToPOIList(ArrayList data){
		createProximityAlerts(data);
	}

	private void createProximityAlerts(ArrayList locationsArray){
		GeoPair LatLongPair = new GeoPair();
		ArrayList<GeoPair> points = new ArrayList<GeoPair>();
		ArrayList<String> names = new ArrayList<String>();
		
		for(int i = 0; i < locationsArray.size(); i++){
			POI poi = (POI) locationsArray.get(i);
			points.addAll(poi.getPointsArray());
			for(int p = 0; p < poi.getPointsListSize(); p++){
				names.add(poi.getName());
			}
			
		}
		for(int j = 0; j < points.size(); j++){
			LatLongPair.setLatitude(points.get(j).getLatitude());
			LatLongPair.setLongitude(points.get(j).getLongitude());
			String name = names.get(j);
			setProximityAlert(LatLongPair.getLatitude(), LatLongPair.getLongitude(), name, j+1, j);
		}
		//loop through each of the POIs
		//create a proximity alert with the lat/long of the current POI
		//MAKE SURE EACH ALERT HAS A UNIQUE ID
		//make a receiver.
	}
	
	private void setProximityAlert(double lat, double lon, String name, final long eventID, int requestCode){
		//Radius of 1 meter
		int radius = 1;
		//10 minute expiration time (10mins*60sec*1000milliseconds)
		//long expiration = 600000;
		//no expiration time
		long expiration = -1;
		
		
		
		Intent intent = new Intent(PROXIMITY_INTENT_ACTION);
		intent.putExtra(POI_NAME, name);
		intent.putExtra(ProximityAlert.EVENT_ID_INTENT_EXTRA, eventID);
		PendingIntent pendingIntent = 
				PendingIntent.getBroadcast(appContext, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		locationManager.addProximityAlert(lat, lon, radius, expiration, pendingIntent);
	
	}

	private class DownloadTask extends AsyncTask<String, Integer, Integer>{

		LocationModel model;
		public void setModel(LocationModel lModel){
			this.model = lModel;
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			ArrayList data = null;
			if(params[0] == "locationFound"){
				try{
					data = sendPOIData(params[0]);
					model.setDataForView(data);
					return 1;
					
				}
				catch(Exception e){
					Log.d("Background Task", e.toString());
					//data.add("Connection error");
					return 0;
				}
			}
			else if(params[0] == "GetCoordinates"){
				try{
					data = getCoordinates();
					model.addCoordinatesToPOIList(data);
					return 1;
				}
				catch(Exception e){
					Log.d("Background Task", e.toString());
					//data.add("Connection error");
					return 0;
				}
			}
			return 0;
			
		}
		/**
		 * Method to send a location once it has been found by the gps/location services
		 * @param poi ID of the point of interest
		 * @return A list of the facts associated with the point of interest
		 */
		private ArrayList sendPOIData(String poi) {
			BufferedReader rd = null;
			StringBuilder sb = null;
			String line = null;
			ArrayList<String> data = new ArrayList<String>();
			try{
				URL url = new URL(poi_url);
				//Create the Connection
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				//Allow input
				urlConnection.setDoInput(true);
				// Allow outputs
				urlConnection.setDoOutput(true);
				//Connect to the site
				urlConnection.connect();
				// Sending request to server
				DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream()); 
				dos.writeBytes("point of interest:" + poi + "\r\n");
				dos.flush();
				
				//Create the Readers for the InputStream
				rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				sb = new StringBuilder();
				
				//Add each line to a String Builder
				while((line = rd.readLine()) != null){
					data.add(line);
				}
				urlConnection.getInputStream().close();
				System.out.println("body=" + data.get(1).toString());
				//Close the connection
				urlConnection.disconnect();
				//Parse the returned JSON
				data = parseJSON(sb.toString(), 2);
			}
			catch(Exception e){
				Log.d("Exception while downloading facts info from url", e.toString());
				return null;
			}
			return data;
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
				URL url = new URL(poi_url);
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
				data = parseJSON(sb.toString(), 1);
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
		public ArrayList parseJSON(String json, int parseType){
			JSONArray jsonObj;
			try {
				//Create a new JSON Object of the input
				//jsonObj = new JSONObject(json);
				jsonObj = new JSONArray(json);
				//If the request came from the getLocations method.
				if(parseType == 1){
					//create an arraylist to hold the locations
					ArrayList<POI> locations = new ArrayList<POI>();
					//Create a JOSN array of the data. Each of the locations should be contained in it.
					//JSONArray l = jsonObj.getJSONArray(TAG_LOCATIONS);
					for(int i = 0; i < jsonObj.length(); i++){
						//Get the next JSON object
						JSONObject o = jsonObj.getJSONObject(i);
						//Put data into Strings
						String name = o.getString(TAG_NAME);
						String id = o.getString(TAG_ID);//I think this is a number of some sort
						String type = o.getString(TAG_TYPE);
						if(type.equalsIgnoreCase("poi")){
							JSONArray obj = o.getJSONArray(TAG_GEOPOINTS);
							
							ArrayList<GeoPair> listCoords = new ArrayList<GeoPair>();
							for(int j = 0; j < obj.length(); j++){
								double longi = Double.parseDouble(o.getString(TAG_LONGITUDE));
								double lati = Double.parseDouble(o.getString(TAG_LATITUDE));
								GeoPair geo = new GeoPair(longi, lati);
								listCoords.add(geo);
							}
							//Create a new Point of Interest Building object
							POI poi = new Building(name, Integer.parseInt(id),
								listCoords, type);
							//Add the object to the arraylist
							locations.add(poi);
							
						}
						else if(type.equalsIgnoreCase("Area")){
							JSONArray obj = o.getJSONArray(TAG_GEOPOINTS);
							//Implement GeoPair class.
							ArrayList<GeoPair> listCoords = new ArrayList<GeoPair>();
							for(int j = 0; j < obj.length(); j++){
								double longi = Double.parseDouble(o.getString(TAG_LONGITUDE));
								double lati = Double.parseDouble(o.getString(TAG_LATITUDE));
								GeoPair geo = new GeoPair(longi, lati);
								listCoords.add(geo);
							}
							//Create a new Point of Interest Building object
							POI poi = new AreaOfInterest(name, Integer.parseInt(id),
								listCoords, type);
							//Add the object to the arraylist
							locations.add(poi);
						}
						else if(type.equalsIgnoreCase("Point")){
							String longi = o.getString(TAG_LONGITUDE);
							String lati = o.getString(TAG_LATITUDE);
							//Create a new Point of Interest object
							POI poi = new PointOfInterest(name, Integer.parseInt(id),
									Double.parseDouble(lati), Double.parseDouble(longi), type);
							//Add the object to the arraylist
							locations.add(poi);
						}
						
					}
					return locations;
				}
				//If the request came from the sendPOIData method
				else if(parseType == 2){
					//create an arraylsit to hold the locations
					ArrayList<String> facts = new ArrayList<String>();
					//Create a JOSN array of the data. Each of the facts should be contained in it.
					//JSONArray l = jsonObj.getJSONArray(TAG_FACTS);
					for(int i = 0; i < jsonObj.length(); i++){
						//Get the next JSON Object
						JSONObject o = jsonObj.getJSONObject(i);
						//Put the fact into a string
						String fact = o.getString(TAG_TEXT);
						//Add the fact to the arraylist
						facts.add(fact);
					}
					
					return facts;
				}
				
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing JSON");
				e.printStackTrace();
				return null;
			}
			return null;
		}
		
		
	}
	//Might not be needed if Proximity Alerts work correctly.
	public class CustomLocationListener implements LocationListener{

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
