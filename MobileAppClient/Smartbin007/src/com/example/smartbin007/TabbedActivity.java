package com.example.smartbin007;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class TabbedActivity extends Activity {
	// Declare Tab Variable
	ActionBar.Tab Tab1, Tab2, Tab3;
	Fragment fragmentTab1;
	Fragment fragmentTab2;
	Fragment fragmentTab3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 

		if ( null == fragmentTab1)
			fragmentTab1 = new LocateBin();
		if ( null == fragmentTab2)
			fragmentTab2 = new GoogleMapTab();
		if ( null == fragmentTab3)
			fragmentTab3 = new AnalyzeTab();


		ActionBar actionBar = getActionBar();

		// Hide Actionbar Icon
		actionBar.setDisplayShowHomeEnabled(false);

		// Hide Actionbar Title
		actionBar.setDisplayShowTitleEnabled(true);

		// Create Actionbar Tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set Tab Icon and Titles
		//Tab1 = actionBar.newTab().setIcon(R.drawable.tab1);
		//Tab1 = actionBar.newTab().setText("GMAP");
		Tab1 = actionBar.newTab().setIcon(R.drawable.ic_action);
		Tab2 = actionBar.newTab().setIcon(R.drawable.ic_bin);
		//Tab3 = actionBar.newTab().setText("ANALYZE");
		Tab3 = actionBar.newTab().setIcon(R.drawable.ic_analyze);

		// Set Tab Listeners
		Tab2.setTabListener(new TabListenerFragment(fragmentTab1));
		Tab1.setTabListener(new TabListenerFragment(fragmentTab2));
		Tab3.setTabListener(new TabListenerFragment(fragmentTab3));	

		// Add tabs to actionbar
		actionBar.addTab(Tab1);
		actionBar.addTab(Tab2);
		actionBar.addTab(Tab3);
	}

}

