package com.example.smartbin007;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.example.smartbin007.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class LocateBin extends Fragment {
	View rootView = null;
	SearchViewHandlers svh = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if ( null == rootView)	{

			rootView = inflater.inflate(R.layout.bins, container, false);

			//Enter the Location to be searched.
			SearchView search = (SearchView)(rootView.findViewById(R.id.searchview));
			search.setQueryHint("Enter SmartBin Location");
			svh = new SearchViewHandlers(getActivity(), rootView);

			search.setOnQueryTextListener(svh);
			setHasOptionsMenu(true);
		}

		SessionManager Session = new SessionManager(getActivity());    
		String Area = Session.getLocation();

		String urlLoc = Area;

		try {
			urlLoc = URLEncoder.encode(urlLoc, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		urlLoc = urlLoc.replaceAll(" ","%20");

		GetServerData gsd = new GetServerData(getActivity(), rootView,urlLoc);
		gsd.execute();
		return rootView;
	}  


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		ActionBar actionbar = getActivity().getActionBar();
		// Clear old menu.
		menu.clear();
		// Inflate new menu.
		inflater.inflate(R.menu.main, menu);
		// Set actionbar title and icon.
		actionbar.setTitle("Fill Levels");
		// actionbar.setIcon(R.drawable.ic_fragment);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){

		case R.id.set:
			Fragment settings = new Settings_menu();
			FragmentTransaction transaction = ((Activity) rootView.getContext()).getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, settings);
			transaction.addToBackStack(null);
			transaction.commit(); 	
			return true;
		case R.id.dashboard:
			Fragment rgbin = new Register_Bin();
			FragmentTransaction transaction1 = ((Activity) rootView.getContext()).getFragmentManager().beginTransaction();
			transaction1.replace(R.id.fragment_container, rgbin);
			transaction1.addToBackStack(null);
			transaction1.commit(); 	
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}    
}