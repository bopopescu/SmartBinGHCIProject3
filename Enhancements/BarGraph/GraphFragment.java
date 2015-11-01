package com.example.smartbin007_edited;

import com.example.smartbin007_edited.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class GraphFragment extends Fragment {
	private View rootView = null;
	private Integer binid;
	private String Area;
	
	public GraphFragment(Integer binid, String Area){
		this.binid = binid;
		this.Area =  Area;

	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if ( null == rootView)	{
			rootView = inflater.inflate(R.layout.graph, container, false);
			GetServerDataforGraphs gsd = new GetServerDataforGraphs(getActivity(), rootView, binid, Area);
     		gsd.execute();

		}
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
