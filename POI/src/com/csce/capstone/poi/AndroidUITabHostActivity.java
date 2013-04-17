package com.csce.capstone.poi;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class AndroidUITabHostActivity extends TabActivity {

	private TabHost th;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_uitab_host);
		setTitle("PLU Points of Interest");
        th = (TabHost) findViewById(android.R.id.tabhost);
		
		TabSpec t1 = th.newTabSpec("tab_id_1");
		TabSpec t2 = th.newTabSpec("tab_id_2");
		t1.setIndicator("Home").setContent(new Intent(this, AndroidUIHomeActivity.class));
		t2.setIndicator("All Locations").setContent(new Intent(this, AndroidUILocationsListActivity.class));
		th.addTab(t1);
		th.addTab(t2);
	}

	

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.android_uitab_host, menu);
//		return true;
//	}
	
	
	
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			// This ID represents the Home or Up button. In the case of this
//			// activity, the Up button is shown. Use NavUtils to allow users
//			// to navigate up one level in the application structure. For
//			// more details, see the Navigation pattern on Android Design:
//			//
//			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
//			//
//			NavUtils.navigateUpFromSameTask(this);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

}
