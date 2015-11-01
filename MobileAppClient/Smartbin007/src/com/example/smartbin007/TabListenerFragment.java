package com.example.smartbin007;

import com.example.smartbin007.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.Menu;


public class TabListenerFragment implements ActionBar.TabListener {
	Menu menu;
	private Fragment fragment;

	public TabListenerFragment(Fragment fragment) {
		// TODO Auto-generated constructor stub
		this.fragment = fragment;
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		ft.replace(R.id.fragment_container,fragment);

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		ft.remove(fragment);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}