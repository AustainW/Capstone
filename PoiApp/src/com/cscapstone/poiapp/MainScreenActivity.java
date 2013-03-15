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
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainScreenActivity extends Activity {

	LocationModel lModel;
	CheckBox noteBox;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		
		noteBox = (CheckBox) findViewById(R.id.recieveNotsCheckBox);
		lModel = new LocationModel(getApplicationContext(), noteBox.isChecked() ,getString(R.string.poi_url));
		
		 //mIntentFilter = new IntentFilter(PROXIMITY_INTENT_ACTION);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_screen, menu);
		return true;
	}
	
}
