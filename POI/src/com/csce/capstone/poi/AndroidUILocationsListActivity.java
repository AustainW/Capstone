package com.csce.capstone.poi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class AndroidUILocationsListActivity extends ListActivity implements OnTaskCompleted {
	private ArrayAdapter<String> mInfoAdapter;
	private ArrayList<POI> pointOfInterestData;
	private ArrayList<String> namesList;
	
	private static final String TAG_ID = "ID";
	private static final String TAG_NAME = "Name";
	private static final String TAG_TYPE = "Type";
	private static final String TAG_META = "Meta Tags";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_uilocations_list);
		
		pointOfInterestData = new ArrayList<POI>();
		namesList = new ArrayList<String>();

		this.setTitle("All Locations");
		
		mInfoAdapter = new ArrayAdapter<String>(this, R.layout.row, namesList);
		this.setListAdapter(mInfoAdapter);
//		DownloadTask dTask = new DownloadTask();
//		dTask.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.android_uilocations_list, menu);
		return true;
	}
	
	@Override
	public void onTaskCompleted(ArrayList data) {
		setNewListData(data);
		
	}
	
	public void setNewListData(ArrayList data){
		pointOfInterestData = data;
		namesList = new ArrayList<String>();
		if(data == null){
			namesList.add("Error - No Locations");
			mInfoAdapter = new ArrayAdapter<String>(this, R.layout.row, namesList);
			this.setListAdapter(mInfoAdapter);
		}else{
			for(POI poi : pointOfInterestData){
				namesList.add(poi.getName());
				//add small row for meta tags
				
			}
			mInfoAdapter = new ArrayAdapter<String>(this, R.layout.row, namesList);
			this.setListAdapter(mInfoAdapter);
		}
		
		
		//Used for debugging purposes
		Toast.makeText(getBaseContext(), "POI data downloaded successfully", Toast.LENGTH_SHORT).show();
	}

	private class DownloadTask extends AsyncTask<String, Integer, ArrayList>{
		private OnTaskCompleted listener;
		
		public DownloadTask(OnTaskCompleted listener){
			this.listener = listener;
		}
		
		
		@Override
		protected ArrayList doInBackground(String... params) {
			ArrayList data = null;
			try{
				data = getCoordinates();
				setNewListData(data);
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
					if(type.equalsIgnoreCase("Bld")){
						//Create a new Point of Interest Building object
						POI poi = new Building(name, Integer.parseInt(id), type);
						//Add the object to the arraylist
						locations.add(poi);
							
					}
					else if(type.equalsIgnoreCase("Area")){
						
						//Create a new Point of Interest Building object
						POI poi = new Area(name, Integer.parseInt(id), type);
						//Add the object to the arraylist
						locations.add(poi);
					}
					else if(type.equalsIgnoreCase("SP")){
						//Create a new Point of Interest object
						POI poi = new Point(name, Integer.parseInt(id), type);
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
	
}
