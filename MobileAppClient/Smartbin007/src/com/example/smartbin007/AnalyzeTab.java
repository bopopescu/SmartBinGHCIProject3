package com.example.smartbin007;

import com.example.smartbin007.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

public class AnalyzeTab extends Fragment {

	Button b1,b2,b3;
	SearchViewHandlers svh = null;
	TextView txt;
	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.analyze, container, false);
		SearchView search = (SearchView)(rootView.findViewById(R.id.searchview));
		search.setQueryHint("Enter SmartBin Location");
		svh = new SearchViewHandlers(getActivity(), rootView);
		search.setOnQueryTextListener(svh);
		txt = (TextView) rootView.findViewById(R.id.fromdate);
		txt.setText("Oct 1, 2015");
		txt = (TextView) rootView.findViewById(R.id.todate);
		txt.setText("Oct 31, 2015");

		setHasOptionsMenu(true);

		b1 = (Button) rootView.findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment(rootView, 1);
				newFragment.show(getFragmentManager(), "datePicker");
				// Perform action on click

			}
		});

		b2 = (Button) rootView.findViewById(R.id.button2);
		b2.setEnabled(false);
		b1.setEnabled(false);
		b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment(rootView, 2);
				newFragment.show(getFragmentManager(), "datePicker");
				// Perform action on click

			}
		});
		b3 = (Button) rootView.findViewById(R.id.button3);
		b3.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				GetServerDataForGraphs gsd = new GetServerDataForGraphs(getActivity().getBaseContext(), rootView);
				gsd.execute();

			}
		});

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
		actionbar.setTitle("Analyze");
		// actionbar.setIcon(R.drawable.ic_fragment);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){

		case R.id.set:
			Fragment settings = new Settings_menu();
			FragmentTransaction trans= ((Activity) rootView.getContext()).getFragmentManager().beginTransaction();
			trans.replace(R.id.fragment_container, settings);
			trans.addToBackStack(null);
			trans.commit(); 	
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