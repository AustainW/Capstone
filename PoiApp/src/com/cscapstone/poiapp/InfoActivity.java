package com.cscapstone.poiapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class InfoActivity extends ListActivity {

	private ArrayAdapter<String> mInfoAdapter;
	private ArrayList pointOfInterestData;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		
		pointOfInterestData = new ArrayList();
		
		mInfoAdapter = new ArrayAdapter<String>(this, R.layout.row, pointOfInterestData);
		this.setListAdapter(mInfoAdapter);
		
		setNewPOIData(getIntent().getStringArrayListExtra("DataArray"));
//		PointOfInterestTask poiTask = new PointOfInterestTask();
//		poiTask.execute("Morken");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_info, menu);
		return true;
	}
	
	public void setNewPOIData(ArrayList data){
		pointOfInterestData = data;
		mInfoAdapter = new ArrayAdapter<String>(this, R.layout.row, pointOfInterestData);
		this.setListAdapter(mInfoAdapter);
		
		//Used for debugging purposes
		Toast.makeText(getBaseContext(), "POI data downloaded successfully", Toast.LENGTH_SHORT).show();
	}

//	private class PointOfInterestTask extends AsyncTask<String, Integer, ArrayList>{
//
//		private ProgressDialog progressDialog;
//		
//		
//		
//		protected void onPreExecute(){
//			progressDialog = new ProgressDialog(InfoActivity.this);
//			progressDialog.setMessage("Loading...");
//			progressDialog.show();
//		}
//		
//		@Override
//		protected ArrayList doInBackground(String... poi) {
//			ArrayList data = null;
//			try{
//				//data = sendPOIData(poi[0]);
//			}
//			catch(Exception e){
//				Log.d("Background Task", e.toString());
//			}
//			if(!data.isEmpty()){
//				return data;
//			}
//			else{
//				return null;
//			}
//		}
//
//		
//		
//		protected void onPostExecute(ArrayList data){
//			progressDialog.dismiss();
//			setNewPOIData(data);
//		}
//		
//	}
	
	
}
