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

public class AndroidUIFactActivity extends ListActivity {
	private ArrayAdapter<String> mInfoAdapter;
	private ArrayList pointOfInterestData;
	
	private static final String TAG_FACT = "Fact";
	private static final String TAG_FACTS = "Facts";
	private static final String TAG_TEXT = "Text";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_uifact);
		
		pointOfInterestData = new ArrayList();
		
		mInfoAdapter = new ArrayAdapter<String>(this, R.layout.row, pointOfInterestData);
		this.setListAdapter(mInfoAdapter);
		int poi_id = getIntent().getIntExtra("poi_id", -1);
		
		DownloadTask dTask = new DownloadTask();
		dTask.execute(poi_id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.android_uifact, menu);
		return true;
	}

	public void setNewPOIData(ArrayList data){
		pointOfInterestData = data;
		mInfoAdapter = new ArrayAdapter<String>(this, R.layout.row, pointOfInterestData);
		this.setListAdapter(mInfoAdapter);
		
		//Used for debugging purposes
		Toast.makeText(getBaseContext(), "POI data downloaded successfully", Toast.LENGTH_SHORT).show();
	}
	
	private class DownloadTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... params) {
			ArrayList data = null;
			try{
				data = sendPOIData(params[0]);
				setNewPOIData(data);
				return 0;
					
			}
			catch(Exception e){
				Log.d("Background Task", e.toString());
				//data.add("Connection error");
				return 0;
			}
		}
		/**
		 * Method to send a location once it has been found by the gps/location services
		 * @param poi ID of the point of interest
		 * @return A list of the facts associated with the point of interest
		 */
		private ArrayList sendPOIData(int poi_id) {
			BufferedReader rd = null;
			StringBuilder sb = null;
			String line = null;
			ArrayList<String> data = new ArrayList<String>();
			try{
				URL url = new URL(getString(R.string.poi_url));
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
				dos.writeBytes("point of interest:" + poi_id + "\r\n");
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
		
		
		public ArrayList parseJSON(String json, int parseType){
			
			try {
				JSONArray jsonObj = new JSONArray(json);
				//create an arraylist to hold the locations
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
			}catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing JSON");
				e.printStackTrace();
				return null;
			}
		}
	}
	
	
	
}
